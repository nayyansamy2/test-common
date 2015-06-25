
package com.elementum.webdriver;

public class Constants {

    // DB constants
    public static final String ADDRESS_LINE = "address_line";
    public static final String ADDRESS_LINE_2 = "address_line2";
    public static final String HQ_LOCATION_ID = "hq_location_id";
    public static final String CITY = "city";
    public static final String ZIP = "zip";
    public static final String LOCATION_ID = "location_id";
    public static final String CONTACT_ID = "contact_id";
    public static final String VERSION = "version";
    public static final String POINTS_COLUMN = "points";
    public static final String PHONE = "phone";
    public static final String STATUS_ID = "status_id";
    public static final String EMAIL_DOMAIN = "email_domain";
    public static final String PHONE_LOCATION_TYPE = "phone_location_type";
    public static final String DIRECT_DIAL_PHONE = "0";
    public static final String HQ_PHONE = "2";
    public static final String EVENT_TYPE_ID = "event_type_id";
    public static final String COMMENTS = "comments";
    public static final String APPROX_REVENUE = "approx_revenue";
    public static final String NAME = "name";
    public static final String APPROX_EMPLOYEE_COUNT = "approx_employee_count";
    public static final Integer DIRECT_DIAL_TXN_TYPE = 22;
    public static final Integer ADD_CONTACT_TXN_TYPE = 4;
    public static final Integer UPDATE_CONTACT_TXN_TYPE = 3;
    public static final String CONTACT_STATUS_ACTIVE = "0";
    public static final String CONTACT_STATUS_SUPPRESSED = "1";
    public static final String CONTACT_STATUS_BOGUS = "2";
    public static final String CONTACT_STATUS_TBN = "3";
    public static final String COMPANY_ID = "company_id";
    public static final String CONTENT = "content";

    // Utility constants
    public static final Long MICRO_WAIT_TIME = 100L;
    public static final Long VERY_SHORT_WAIT_TIME = 500L;
    public static final Long SHORT_WAIT_TIME = 1000L;
    public static final Long MEDIUM_WAIT_TIME = 5000L;
    public static final Long LONG_WAIT_TIME = 15000L;
    public static final Long VERY_LONG_WAIT_TIME = 30000L;
    public static final Long EXTREMELY_LONG_WAIT_TIME = 190000L;

    // UI constants
    public static final Integer POINTS = 5;
    public static final Integer DIRECT_DIAL_POINTS = 10;
    public static final String USA = "United States";
    public static final String TRACKINGFIELDVALUE = "trackingname";
    public static final int GUID_LENGTH = 22;

    // Comma separator
    public static final String COMMA_SEPARATOR = ",";

    // CSRF Token
    public static final String EXPIRED_CSRF_TOKEN = "1915b9d6f1a08346d103042bd48484e28bdaaf8acbfcbd1e037477c99e68b074";

    // Screenshot folder-name for testNG suite results
    public static final String SCREENSHOT_FOLDER_NAME = "screenshot";
    public static final String IMG_EXTENSION = ".png";
    public static final String SCREENSHOT_LINK_NAME = "Click here to View Screenshot";
    public static final String QE_PASS = "Test1234";

    // Classic web UI constants
/*    public static final String CLASSICACTIVATEURLPARTNAME = "Activate.xhtml";

    public static enum CREDIT_CARD_TYPE {
        VISA, MASTERCARD, AMEX, DISCOVER
    }

    public static final String VISA = "4111111111111111";
    public static final String MASTERCARD = "5555555555554444";
    public static final String AMEX = "378282246310005";
    public static final String DISCOVER = "6011111111111117";
    public static final String CVV = "1111";
    public static final String EXPIRY_YEAR = "2020";

    public static final String FULLNAME = "Roger Summers";
    public static final String ADDRESS1 = "777 Mariners Island Blvd";
    public static final String CITY1 = "San Mateo";
    public static final String STATE1 = "CA";
    public static final String ZIP1 = "94404";*/

}
