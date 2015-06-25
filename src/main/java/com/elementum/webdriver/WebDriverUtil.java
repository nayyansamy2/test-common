package com.elementum.webdriver;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebDriverUtil {

    /**
     * Sleep for given time
     *
     * @param sleepTime
     *            in milli seconds
     */
    public static void sleepForGivenTime(long sleepTime) {

        try {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getNoOfOccurrenceOfGivenText(String origText, String findText) {
        return origText.toLowerCase().split(findText.toLowerCase()).length - 1;
    }

    public static String getTimeStamp(String timeStampFormat) {

        DateFormat df = null;
        if (timeStampFormat != null) {
            df = new SimpleDateFormat(timeStampFormat);
        }
        else {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        }
        Date today = Calendar.getInstance().getTime();
        return df.format(today);

    }

    public static String removeNonAlphaNumerics(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "");
    }

    public static String replaceSpecialChars(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "_");
    }

    public static String removeDuplicateWhitespace(String inputStr) {
        String patternStr = "\\s+";
        String replaceStr = " ";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.replaceAll(replaceStr);
    }


    public static String extractEmailTextFromDomain(String email) {
        String[] emailContent = null;
        String emailPrefix = "";
        try {
            emailContent = email.split("@");
            emailPrefix = emailContent[0];
        } catch (ArrayIndexOutOfBoundsException ae) {
            ae.printStackTrace();
        }
        return emailPrefix;
    }

    public static String getCompanyNameFromWebsite(String website) {
        String[] websiteContent = null;
        String cmpName = "";
        try {
            websiteContent = website.split("\\.");
            if (website.startsWith("www")) {
                cmpName = websiteContent[1];
            }
            else {
                cmpName = websiteContent[0];
            }
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            ae.printStackTrace();
        }
        return cmpName;
    }

    public static String toCamelCase(String value) {
        return String.valueOf(value.charAt(0)).toUpperCase().concat(value.substring(1));
    }

}
