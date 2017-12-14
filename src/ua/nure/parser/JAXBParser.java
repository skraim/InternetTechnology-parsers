package ua.nure.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import jdk.nashorn.internal.runtime.SpillProperty;
import org.xml.sax.SAXException;
import ua.nure.sportinventory.Inventory;
import ua.nure.sportinventory.InventoryList;

public class JAXBParser {

	public InventoryList parser(FileInputStream is) throws JAXBException, SAXException {
		JAXBContext con = JAXBContext.newInstance(Const.OBJECT_FACTORY);
		Unmarshaller unMarsh = con.createUnmarshaller();
		SchemaFactory schFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schFactory.newSchema(new File(Const.XSD_FILE));
		unMarsh.setSchema(schema);
		unMarsh.setEventHandler(new ValidationEventHandler() {
			@Override
			public boolean handleEvent(ValidationEvent event) {
				System.err.println("ERROR!");
				return false;
			}
		});
		InventoryList inventoryList = (InventoryList) unMarsh.unmarshal(is);
		return inventoryList;
	}

	public static void saveOrders(InventoryList inventory, String xmlFile, String xsdFile, Class<?> objectFactory) throws JAXBException, SAXException {
		JAXBContext jc = JAXBContext.newInstance(objectFactory);
		Marshaller marshaller = jc.createMarshaller();

		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		if (Const.XSD_FILE != null) {
			Schema schema = sf.newSchema(new File(Const.XSD_FILE));

			marshaller.setSchema(schema);
			marshaller.setEventHandler(new ValidationEventHandler() {
				@Override
				public boolean handleEvent(ValidationEvent event) {
					System.err.println("====================================");
					System.err.println("JAXBMarshallerResult.xml" + " is NOT valid against "
							+ Const.XSD_FILE + ":\n" + event.getMessage());
					System.err.println("====================================");
					return false;
				}
			});
		}

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, Const.SCHEMA_LOCATION__URI);
		marshaller.marshal(inventory, new File("JAXBMarshallerResult.xml"));
	}

	public static void main(String[] args) throws JAXBException, SAXException, FileNotFoundException {
		JAXBParser jbp = new JAXBParser();
		InventoryList invs = jbp.parser(new FileInputStream(Const.XML_FILE));

		System.out.println("--== JAXB Parser ==--");
		System.out.println("====================================");
		System.out.println("Here is the inventory list: \n" + invs.getInventory());
		System.out.println("====================================");

		try {
			saveOrders(invs, "JAXBMarshallerResult.xml", Const.XSD_FILE, Const.OBJECT_FACTORY);
		} catch (Exception ex) {
			System.err.println("====================================");
			System.err.println("Object tree not valid against XSD.");
			System.err.println(ex.getClass().getName());
			System.err.println("====================================");
		}
	}
}
