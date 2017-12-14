package ua.nure;

import org.xml.sax.SAXException;
import ua.nure.parser.DOMParser;
import ua.nure.parser.JAXBParser;
import ua.nure.parser.SAXParser;
import ua.nure.sportinventory.Inventory;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Demo {
    public static List<Inventory> inventory;
    public static InputStream in;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, JAXBException {
        System.out.println("-=DOM Parser=-   (\\/)(;,,,;)(\\/)");
        DOMParser domParser = new DOMParser();
        in = new FileInputStream("SportInventory.xml");
        inventory = domParser.parse(in);
        System.out.println(inventory);
        System.out.println("-=SAX Parser=-   (\\/)(;,,,;)(\\/)");
        SAXParser saxParser = new SAXParser();
        System.out.println(saxParser.parse(new FileInputStream("SportInventory.xml")));
        System.out.println("-=JAXB Parser=-   (\\/)(;,,,;)(\\/)");
        JAXBParser jaxbParser = new JAXBParser();
        System.out.println(jaxbParser.parser(new FileInputStream("SportInventory.xml")).getInventory());
    }
}
