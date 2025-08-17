package com.sinse.xmlappdemo.model.pharmacy;

import com.sinse.xmlappdemo.domain.Pharmacy;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class PharmacyHandler extends DefaultHandler {

    @Getter
    private List<Pharmacy> list = new ArrayList<>();

    private Pharmacy pharmacy;
    private boolean isName, isState, isTelno, isAddr, isX, isY;

    @Override
    public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
        if(tag.equals("row")) pharmacy = new Pharmacy();
        else if(tag.equals("BIZPLC_NM")) isName = true;
        else if(tag.equals("BSN_STATE_NM")) isState = true;
        else if(tag.equals("LOCPLC_FACLT_TELNO")) isTelno = true;
        else if(tag.equals("REFINE_ROADNM_ADDR")) isAddr = true;
        else if(tag.equals("X_CRDNT_VL")) isX = true;
        else if(tag.equals("Y_CRDNT_VL")) isY = true;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length);
        if(isName) pharmacy.setName(content);
        if(isState) pharmacy.setState(content);
        if(isTelno) pharmacy.setTelno(content);
        if(isAddr) pharmacy.setRoad_addr(content);
        if(isX) pharmacy.setGpsx(Double.parseDouble(content));
        if(isY) pharmacy.setGpsy(Double.parseDouble(content));
    }

    @Override
    public void endElement(String uri, String localName, String tag) throws SAXException {
        if(tag.equals("row")) list.add(pharmacy);
        else if(tag.equals("BIZPLC_NM")) isName = false;
        else if(tag.equals("BSN_STATE_NM")) isState = false;
        else if(tag.equals("LOCPLC_FACLT_TELNO")) isTelno = false;
        else if(tag.equals("REFINE_ROADNM_ADDR")) isAddr = false;
        else if(tag.equals("X_CRDNT_VL")) isX = false;
        else if(tag.equals("Y_CRDNT_VL")) isY = false;
    }
}
