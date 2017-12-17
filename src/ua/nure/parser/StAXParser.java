package ua.nure.parser;

import ua.nure.sportinventory.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.transform.stream.StreamSource;

public class StAXParser {
    private InventoryList inventoriList;

    public static void main(String[] args) throws XMLStreamException {
        StAXParser staxParser = new StAXParser();
        staxParser.parse(Const.XML_FILE);
        staxParser.getInventoriList().getInventory().forEach(System.out::println);
        System.out.println();
    }

    public InventoryList getInventoriList() {
        return inventoriList;
    }

    public void parse(String fileName) throws XMLStreamException {
        Inventory inventory = null;
        PriceType priceInHour = null;
        PriceType priceInADay = null;
        Size size = null;
        String currentElement = null;
        XMLEvent event;
        StartElement startElement;
        Characters characters;
        EndElement endElement;
        String localName;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        XMLEventReader reader = factory.createXMLEventReader(new StreamSource(fileName));

        while (reader.hasNext()) {
            event = reader.nextEvent();
            if (event.isCharacters() && event.asCharacters().isWhiteSpace()) {
                continue;
            }
            if (event.isStartElement()) {
                startElement = event.asStartElement();
                currentElement = startElement.getName().getLocalPart();

                if (currentElement.equals(Const.TAG_INVLIST)) {
                    inventoriList = new InventoryList();
                    continue;
                }
                if (currentElement.equals(Const.TAG_INV)) {
                    inventory = new Inventory();
                    Attribute attribute = startElement.getAttributeByName(new QName(Const.ATTR_ID));
                    inventory.setId(Integer.parseInt(attribute.getValue()));
                    continue;
                }
                if (currentElement.equals(Const.TAG_SIZE)) {
                    size = new Size();
                    Attribute attribute = startElement.getAttributeByName(new QName(Const.ATTR_SCALE));
                    size.setScale(attribute.getValue());
                    continue;
                }
                if (currentElement.equals(Const.TAG_PRICEHOUR)) {
                    priceInHour = new PriceType();
                    Attribute attribute = startElement.getAttributeByName(new QName(Const.ATTR_CURRENCY));
                    priceInHour.setCurrency(attribute.getValue());
                    continue;
                }
                if (currentElement.equals(Const.TAG_PRICEDAY)) {
                    priceInADay = new PriceType();
                    Attribute attribute = startElement.getAttributeByName(new QName(Const.ATTR_CURRENCY));
                    priceInADay.setCurrency(attribute.getValue());
                }
            }
            if (event.isCharacters()) {
                characters = event.asCharacters();
                if (currentElement.equals(Const.TAG_TYPE)) {
                    inventory.setType(characters.getData());
                    continue;
                }
                if (currentElement.equals((Const.TAG_ORIGINCOUNTRY))) {
                    inventory.setOriginCountry(characters.getData());
                    continue;
                }
                if (currentElement.equals(Const.TAG_CONCERN)) {
                    inventory.setConcern(characters.getData());
                    continue;
                }
                if (currentElement.equals(Const.TAG_MODEL)) {
                    inventory.setModel(characters.getData());
                    continue;
                }
                if (currentElement.equals(Const.TAG_YEAR)) {
                    inventory.setYear(Integer.parseInt(characters.getData()));
                    continue;
                }
                if (currentElement.equals(Const.TAG_GENDER)) {
                    inventory.setGender(Gender.fromValue(characters.getData()));
                    continue;
                }
                if (currentElement.equals(Const.TAG_SIZE)) {
                    if (checkSize(characters.getData()))
                    size.setValue(characters.getData());
                    continue;
                }
                if (currentElement.equals(Const.TAG_PRICEHOUR)) {
                    priceInHour.setValue(Double.parseDouble(characters.getData()));
                    continue;
                }
                if (currentElement.equals(Const.TAG_PRICEDAY)) {
                    priceInADay.setValue(Double.parseDouble(characters.getData()));
                }
            }
            if (event.isEndElement()) {
                endElement = event.asEndElement();
                localName = endElement.getName().getLocalPart();

                if (localName.equals(Const.TAG_INV)) {
                    inventoriList.getInventory().add(inventory);
                    continue;
                }
                if (localName.equals(Const.TAG_SIZE)) {
                    inventory.setSize(size);
                    continue;
                }
                if (localName.equals(Const.TAG_PRICEHOUR)) {
                    inventory.setPriceInHour(priceInHour);
                    continue;
                }
                if (localName.equals(Const.TAG_PRICEDAY)) {
                    inventory.setPriceInADay(priceInADay);
                }
            }
        }
        reader.close();
    }

    private boolean checkSize(String textContent) {
        String[] sizes = {"XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL"};
        for (int i=0; i<sizes.length; i++) {
            if (textContent.equals(sizes[i])) {
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
