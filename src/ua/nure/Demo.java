package ua.nure;

import org.xml.sax.SAXException;
import ua.nure.parser.Const;
import ua.nure.parser.DOMParser;
import ua.nure.parser.JAXBParser;
import ua.nure.parser.SAXParser;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, JAXBException {
        System.out.println("-=DOM Parser=-   (\\/)(;,,,;)(\\/)");
        DOMParser domParser = new DOMParser();
        System.out.println(domParser.parse(Const.XML_FILE));
        System.out.println("-=SAX Parser=-   (\\/)(;,,,;)(\\/)");
        SAXParser saxParser = new SAXParser();
        System.out.println(saxParser.parse(Const.XML_FILE));
        System.out.println("-=JAXB Parser=-   (\\/)(;,,,;)(\\/)");
        JAXBParser jaxbParser = new JAXBParser();
        System.out.println(jaxbParser.parser(Const.XML_FILE));
    }
}
