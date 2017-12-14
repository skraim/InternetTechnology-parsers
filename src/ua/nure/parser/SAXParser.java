package ua.nure.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ua.nure.sportinventory.*;

public class SAXParser extends DefaultHandler {

    private String current;
    private List<Inventory> invList;
    private Inventory inv;
    private Size size;
    private PriceType priceInHour;
    private PriceType priceInDay;


    public List<Inventory> getInvList() {
        return invList;
    }

    @Override
    public void error(org.xml.sax.SAXParseException e) throws SAXException {
        throw e;
    }

    public List<Inventory> parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(Const.FEATURE__TURN_VALIDATION_ON, true);
        factory.setFeature(Const.FEATURE__TURN_SCHEMA_VALIDATION_ON, true);
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        parser.parse(in, this);
        return invList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        current = localName;

        if (localName.equals(Const.TAG_INVLIST)) {
            invList = new ArrayList<>();
        } else if (Const.TAG_INV.equals(localName)) {
            inv = new Inventory();
            inv.setId(Integer.parseInt(attributes.getValue(Const.ATTR_ID)));
        } else if (Const.TAG_GENDER.equals(localName)) {
            inv.setGender(Gender.ANY);
        } else if (Const.TAG_SIZE.equals(localName)) {
            if (attributes.getLength() == 1) {
                size = new Size();
                size.setScale(attributes.getValue(Const.ATTR_SCALE));
            }
        } else if (Const.TAG_PRICEHOUR.equals(localName)) {
            if (attributes.getLength() == 1) {
                priceInHour = new PriceType();
                priceInHour.setCurrency(attributes.getValue(Const.ATTR_CURRENCY));
                inv.setPriceInHour(priceInHour);
            }
        } else if (Const.TAG_PRICEDAY.equals(localName)) {
            if (attributes.getLength() == 1) {
                priceInDay = new PriceType();
                priceInDay.setCurrency(attributes.getValue(Const.ATTR_CURRENCY));
                inv.setPriceInADay(priceInDay);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (current.equals(Const.TAG_TYPE)) {
            inv.setType(new String(ch, start, length));
        } else if (current.equals(Const.TAG_ORIGINCOUNTRY)) {
            inv.setOriginCountry(new String(ch, start, length));
        } else if (current.equals(Const.TAG_CONCERN)) {
            inv.setConcern(new String(ch, start, length));
        } else if (current.equals(Const.TAG_MODEL)) {
            inv.setModel(new String(ch, start, length));
        } else if (current.equals(Const.TAG_YEAR)) {
            inv.setYear(Integer.parseInt(new String(ch, start, length)));
        } else if (current.equals(Const.TAG_GENDER)) {
            inv.setGender(Gender.fromValue(new String(ch, start, length)));
        } else if (current.equals(Const.TAG_SIZE)) {
            size.setValue(new String(ch, start, length));
        } else if (current.equals(Const.TAG_PRICEHOUR)) {
            priceInHour.setValue(Double.parseDouble(new String(ch, start, length)));
        } else if (current.equals(Const.TAG_PRICEDAY)) {
            priceInDay.setValue(Double.parseDouble(new String(ch, start, length)));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals(Const.TAG_INV)) {
            invList.add(inv);
        } else if (Const.TAG_SIZE.equals(localName)) {
            inv.setSize(size);
        } else if (Const.TAG_PRICEHOUR.equals(localName)) {
            inv.setPriceInHour(priceInHour);
        } else if (Const.TAG_PRICEDAY.equals(localName)) {
            inv.setPriceInADay(priceInDay);
        }
    }
}
