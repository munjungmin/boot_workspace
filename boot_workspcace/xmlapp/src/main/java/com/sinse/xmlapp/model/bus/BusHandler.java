package com.sinse.xmlapp.model.bus;

import com.sinse.xmlapp.domain.Item;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusHandler extends DefaultHandler {

    @Getter
    private List<Item> itemList;

    private Item item;
    private boolean isId, isName, isNo, isX, isY, isType;

    @Override
    public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
        if(tag.equals("items")){
            itemList = new ArrayList<>();
        } else if (tag.equals("item")) {
            item = new Item();
        } else if (tag.equals("bstopid")) {
            isId = true;
        } else if (tag.equals("bstopnm")) {
            isName = true;
        } else if (tag.equals("arsno")) {
            isNo = true;
        } else if (tag.equals("gpsx")) {
            isX = true;
        } else if (tag.equals("gpsy")) {
            isY = true;
        } else if (tag.equals("stoptype")) {
            isType = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length);

        if (isId) {
            item.setBstopid(Integer.parseInt(content));
        } else if (isName) {
            item.setBstopnm(content);
        } else if (isNo) {
            item.setArsno(content);
        } else if (isX) {
            item.setGpsx(Double.parseDouble(content));
        } else if (isY) {
            item.setGpsy(Double.parseDouble(content));
        } else if (isType) {
            item.setStoptype(content);
        }
    }

    @Override
    public void endElement(String uri, String localName, String tag) throws SAXException {
        if (tag.equals("item")) {
            itemList.add(item);
        } else if (tag.equals("bstopid")) {
            isId = false;
        } else if (tag.equals("bstopnm")) {
            isName = false;
        } else if (tag.equals("arsno")) {
            isNo = false;
        } else if (tag.equals("gpsx")) {
            isX = false;
        } else if (tag.equals("gpsy")) {
            isY = false;
        } else if (tag.equals("stoptype")) {
            isType = false;
        }
    }
}
