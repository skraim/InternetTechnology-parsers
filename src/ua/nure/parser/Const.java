package ua.nure.parser;

import ua.nure.sportinventory.*;

public interface Const {

	String TAG_INVLIST = "inventoryList";
	String TAG_INV = "inventory";
	String ATTR_ID = "id";
	String ATTR_SCALE = "scale";
	String ATTR_CURRENCY = "currency";
	String TAG_TYPE = "type";
	String TAG_ORIGINCOUNTRY ="originCountry";
	String TAG_CONCERN = "concern";
	String TAG_MODEL = "model";
	String TAG_YEAR = "year";
	String TAG_GENDER = "gender";
	String TAG_SIZE = "size";
	String TAG_PRICEHOUR = "priceInHour";
	String TAG_PRICEDAY = "priceInADay";

	String XML_FILE = "SportInventory.xml";
	String XSD_FILE = "xsd/sportInventory.xsd";
	Class<?> OBJECT_FACTORY = ObjectFactory.class;
	
	String SCHEMA_LOCATION__ATTR_NAME = "schemaLocation";
	String SCHEMA_LOCATION__ATTR_FQN = "xsi:schemaLocation";
	String XSI_SPACE__PREFIX = "xsi";
	String SCHEMA_LOCATION__URI = "http://nure.ua/sportInventory xsd/sportInventory.xsd";

	// validation features
	public static final String FEATURE__TURN_VALIDATION_ON = 
			"http://xml.org/sax/features/validation";
	public static final String FEATURE__TURN_SCHEMA_VALIDATION_ON = 
			"http://apache.org/xml/features/validation/schema";

}
