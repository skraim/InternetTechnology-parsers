package ua.nure.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ua.nure.sportinventory.*;

public class SAXParser extends DefaultHandler {

    private String currentElement;
    private InventoryList invList;
    private Inventory inv;
    private Size size;
    private PriceType priceInHour;
    private PriceType priceInDay;


    public InventoryList getInvList() {
        return invList;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        SAXParser saxParser = new SAXParser();
        saxParser.parse(Const.XML_FILE);
        saxParser.getInvList().getInventory().forEach(System.out::println);
    }

    @Override
    public void error(org.xml.sax.SAXParseException e) throws SAXException {
        throw e;
    }

    public InventoryList parse(String fileName) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(fileName);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(Const.FEATURE__TURN_VALIDATION_ON, true);
        factory.setFeature(Const.FEATURE__TURN_SCHEMA_VALIDATION_ON, true);
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        parser.parse(is, this);
        return invList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = localName;

        switch (localName) {
            case Const.TAG_INVLIST:
                invList = new InventoryList();
                break;
            case Const.TAG_INV:
                inv = new Inventory();
                inv.setId(Integer.parseInt(attributes.getValue(Const.ATTR_ID)));
                break;
            case Const.TAG_GENDER:
                inv.setGender(Gender.ANY);
                break;
            case Const.TAG_SIZE:
                if (attributes.getLength() == 1) {
                    size = new Size();
                    size.setScale(attributes.getValue(Const.ATTR_SCALE));
                }
                break;
            case Const.TAG_PRICEHOUR:
                if (attributes.getLength() == 1) {
                    priceInHour = new PriceType();
                    priceInHour.setCurrency(attributes.getValue(Const.ATTR_CURRENCY));
                }
                break;
            case Const.TAG_PRICEDAY:
                if (attributes.getLength() == 1) {
                    priceInDay = new PriceType();
                    priceInDay.setCurrency(attributes.getValue(Const.ATTR_CURRENCY));
                }
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start,length);

        switch (currentElement) {
            case Const.TAG_TYPE:
                inv.setType(content);
                break;
            case Const.TAG_ORIGINCOUNTRY:
                inv.setOriginCountry(content);
                break;
            case Const.TAG_CONCERN:
                inv.setConcern(content);
                break;
            case Const.TAG_MODEL:
                inv.setModel(content);
                break;
            case Const.TAG_YEAR:
                inv.setYear(Integer.parseInt(content));
                break;
            case Const.TAG_GENDER:
                inv.setGender(Gender.fromValue(content));
                break;
            case Const.TAG_SIZE:
                if (checkSize(content)) {
                    size.setValue(content);
                }
                break;
            case Const.TAG_PRICEHOUR:
                priceInHour.setValue(Double.parseDouble(content));
                break;
            case Const.TAG_PRICEDAY:
                priceInDay.setValue(Double.parseDouble(content));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName) {
            case Const.TAG_INV:
                invList.getInventory().add(inv);
                break;
            case Const.TAG_SIZE:
                inv.setSize(size);
                break;
            case Const.TAG_PRICEHOUR:
                inv.setPriceInHour(priceInHour);
                break;
            case Const.TAG_PRICEDAY:
                inv.setPriceInADay(priceInDay);
                break;
        }
    }

    private boolean checkSize(String textContent) {
        String[] sizes = {"XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL"};
        for (String size1 : sizes) {
            if (textContent.equals(size1)) {
                return true;
            }
        }
        try {
            if (Double.parseDouble(textContent)>0) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}
