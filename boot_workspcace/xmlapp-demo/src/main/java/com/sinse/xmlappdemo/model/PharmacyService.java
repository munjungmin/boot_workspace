package com.sinse.xmlappdemo.model;

import com.sinse.xmlappdemo.domain.Pharmacy;
import com.sinse.xmlappdemo.model.pharmacy.PharmacyParser;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Service
public class PharmacyService {

    private PharmacyParser pharmacyParser;

    public PharmacyService(PharmacyParser pharmacyParser) {
        this.pharmacyParser = pharmacyParser;
    }

    public List<Pharmacy> parse() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        return pharmacyParser.parse();
    }
}
