package ua.nure.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ua.nure.sportinventory.*;

public class DOMParser {

	public List<Inventory> parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		dbf.setFeature(Const.FEATURE__TURN_VALIDATION_ON, true);
		dbf.setFeature(Const.FEATURE__TURN_SCHEMA_VALIDATION_ON, true);

		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new DefaultHandler() {
			@Override
			public void error(SAXParseException e) throws SAXException {
				throw e; // <-- throw exception if XML document is NOT valid
			}
		});

		Document root = db.parse(in);
		
		List<Inventory> inventories = new ArrayList<>();

		Element e = root.getDocumentElement();
		NodeList xmlInventories = e.getElementsByTagNameNS("*",Const.TAG_INV);
		for (int i = 0; i < xmlInventories.getLength(); i++) {
			inventories.add(parseInventory(xmlInventories.item(i)));
		}
		return inventories;
	}
	
	private Inventory parseInventory(Node node) {
		NamedNodeMap attrs;
		Inventory inventory = new Inventory();
		if (node.hasAttributes()) {
			attrs = node.getAttributes();
			Node item = attrs.getNamedItem(Const.ATTR_ID);
			inventory.setId(Integer.parseInt(item.getTextContent()));
		}
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			if (Const.TAG_TYPE.equals(item.getLocalName())) {
				inventory.setType(item.getTextContent());
			} else if (Const.TAG_ORIGINCOUNTRY.equals(item.getLocalName())) {
				inventory.setOriginCountry(item.getTextContent());
			} else if (Const.TAG_CONCERN.equals(item.getLocalName())) {
				inventory.setConcern(item.getTextContent());
			} else if (Const.TAG_MODEL.equals(item.getLocalName())) {
				inventory.setModel(item.getTextContent());
			} else if (Const.TAG_YEAR.equals(item.getLocalName())) {
				inventory.setYear(Integer.parseInt(item.getTextContent()));
			} else if (Const.TAG_GENDER.equals(item.getLocalName())) {
				inventory.setGender(parseGender(item));
			} else if (Const.TAG_SIZE.equals(item.getLocalName())) {
				if (checkSize(item.getTextContent())) {
					inventory.setSize(parseSize(item));
					if (item.hasAttributes()) {
						attrs = item.getAttributes();
						Node temp = attrs.getNamedItem(Const.ATTR_SCALE);
						inventory.getSize().setScale(temp.getTextContent());
					}
				} else {
					System.out.println("Size value "+item.getTextContent()+" is incorrect. Please, change this value in your *.xml");
				}
			} else if (Const.TAG_PRICEHOUR.equals(item.getLocalName())) {
				inventory.setPriceInHour(parsePriceType(item));
				if (item.hasAttributes()) {
					attrs = item.getAttributes();
					Node temp = attrs.getNamedItem(Const.ATTR_CURRENCY);
					inventory.getPriceInHour().setCurrency(temp.getTextContent());
				}
			} else if (Const.TAG_PRICEDAY.equals(item.getLocalName())) {
				inventory.setPriceInADay(parsePriceType(item));
				if (item.hasAttributes()) {
					attrs = item.getAttributes();
					Node temp = attrs.getNamedItem(Const.ATTR_CURRENCY);
					inventory.getPriceInADay().setCurrency(temp.getTextContent());
				}
			}
		}
		return inventory;
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

	private PriceType parsePriceType(Node node) {
		PriceType price = new PriceType();
		price.setValue(Double.parseDouble(node.getTextContent()));
		return price;
	}

	private Size parseSize(Node node) {
		Size size = new Size();
		size.setValue(node.getTextContent());
		return size;
	}

	private Gender parseGender(Node node) {
		Gender gender = Gender.fromValue(node.getTextContent());
		return gender;
	}
}
