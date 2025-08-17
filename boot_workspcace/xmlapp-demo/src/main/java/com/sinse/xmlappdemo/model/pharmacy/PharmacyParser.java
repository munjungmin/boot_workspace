package com.sinse.xmlappdemo.model.pharmacy;

import com.sinse.xmlappdemo.domain.Pharmacy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Component
public class PharmacyParser {

    @Value("${datadream.api.key}")
    private String serviceKey;

    private PharmacyHandler pharmacyHandler;

    public PharmacyParser(PharmacyHandler pharmacyHandler) {
        this.pharmacyHandler = pharmacyHandler;
    }

    public List<Pharmacy> parse() throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        String baseUrl = "https://openapi.gg.go.kr/AnimalPharmacy";

        //파라미터 설정
        String url = baseUrl + "?" +
                "KEY=" + serviceKey +
                "&pIndex=" + "1" +
                "&pSize=" + "20" +
                "&SIGUN_CD=" + "41110" +
                "&BSN_STATE_NM=" + "정상";

        HttpClient client = HttpClient.newHttpClient();

        // java 코드로 직접 HTTP 요청메시지를 보내기
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                //.header("Content-Type", "application/json")
                .GET()
                .build();

        //Open API 서버에 요청 시도
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug(response.body());

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        InputSource is = new InputSource(new StringReader(response.body()));
        saxParser.parse(is, pharmacyHandler);

        return pharmacyHandler.getList();
    }
}
