package ua.nure.parser;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import ua.nure.sportinventory.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class DOMmarsh {

    private List<Inventory> inventories;
    private Element inv;
    private Element type;
    private Element origCountry;
    private Element concern;
    private Element model;
    private Element year;
    private Element gender;
    private Element size;
    private Element priceInHour;
    private Element priceInADay;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, JAXBException, TransformerException {
        DOMmarsh marshaller = new DOMmarsh();
        marshaller.parseBefore();
        marshaller.makeWork();
    }

    public void parseBefore() throws IOException, SAXException, ParserConfigurationException {
        DOMParser domParser = new DOMParser();
        inventories = domParser.parse(new FileInputStream("SportInventory.xml"));
    }

    public void makeWork() throws ParserConfigurationException, JAXBException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element invList = document.createElement("inventoryList");
        document.appendChild(invList);
        for (Inventory thisInventory : inventories) {
            inv = document.createElement("inventory");
            Attr attrInventory = document.createAttribute("id");
            attrInventory.setValue(thisInventory.getId()+"");
            inv.setAttributeNode(attrInventory);
            invList.appendChild(inv);
            type = document.createElement("type");
            type.appendChild(document.createTextNode(thisInventory.getType()));
            inv.appendChild(type);
            origCountry = document.createElement("originCountry");
            origCountry.appendChild(document.createTextNode(thisInventory.getOriginCountry()));
            inv.appendChild(origCountry);
            concern = document.createElement("concern");
            concern.appendChild(document.createTextNode(thisInventory.getConcern()));
            inv.appendChild(concern);
            model = document.createElement("model");
            model.appendChild(document.createTextNode(thisInventory.getModel()));
            inv.appendChild(model);
            year = document.createElement("year");
            year.appendChild(document.createTextNode(thisInventory.getYear()+""));
            inv.appendChild(year);
            gender = document.createElement("gender");
            gender.appendChild(document.createTextNode(thisInventory.getGender().value()));
            inv.appendChild(gender);
            size = document.createElement("size");
            Attr attrSize = document.createAttribute("scale");
            attrSize.setValue(thisInventory.getSize().getScale());
            size.setAttributeNode(attrSize);
            size.appendChild(document.createTextNode(thisInventory.getSize().getValue()));
            inv.appendChild(size);
            priceInHour = document.createElement("priceInHour");
            Attr attrPriceInHour = document.createAttribute("currency");
            attrPriceInHour.setValue(thisInventory.getPriceInHour().getCurrency());
            priceInHour.setAttributeNode(attrPriceInHour);
            priceInHour.appendChild(document.createTextNode(thisInventory.getPriceInHour().getValue()+""));
            inv.appendChild(priceInHour);
            priceInADay = document.createElement("priceInADay");
            Attr attrPriceInADay = document.createAttribute("currency");
            attrPriceInADay.setValue(thisInventory.getPriceInADay().getCurrency());
            priceInADay.setAttributeNode(attrPriceInADay);
            priceInADay.appendChild(document.createTextNode(thisInventory.getPriceInADay().getValue()+""));
            inv.appendChild(priceInADay);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.newTransformer().transform(new DOMSource(document),
                new StreamResult(new File("DOMMarshallerResult.xml")));
    }
}
