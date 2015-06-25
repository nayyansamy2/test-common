
package com.elementum.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;


public abstract class WebDriverSupport {

    private static Logger logger = Logger.getLogger(WebDriverSupport.class);

    public static String baseUrl;
    
    protected ThreadLocal<WebDriver> driver;
    protected String driverType;
    public Properties driverProperties;
    protected DesiredCapabilities gridConfig;
    protected URL gridHubUrl;
    protected boolean verifyDB;
    protected String browserType;
    protected String platformName;
    protected String browserVersion;

    // Default property resource path
    protected String propertiesResource = "com/elementum/webdriver/webdriver-config.properties";
    protected String propertiesFile = null;

    public void setUpWebDriver(String BrowserName,
                               String platform, String version) {

        // driver configuration
        driver = new InheritableThreadLocal<WebDriver>();
        loadDriverProperties();
        driverType = driverProperties.getProperty("driver", "htmlunit");
        baseUrl = driverProperties.getProperty("baseUrl", "http://localhost:8080/");
        verifyDB = Boolean.parseBoolean(driverProperties.getProperty("verifyDB", "false"));
        Reporter.log(baseUrl, true);
        if (driverType.equalsIgnoreCase("grid")) {
            browserType = BrowserName;
            platformName = platform;
            browserVersion = version;
            setGridDriverConfig();
        }
    }

    // Every test that can open a browser 
    public void setUpBeforeEachMethod() {
    	setUpBeforeTestOrMethod();
    }

    // Opens the browser before the test
    public void setUpBeforeEachTest() {
    	setUpBeforeTestOrMethod();
    }

    private void setUpBeforeTestOrMethod() {
        driver.set(getDriver());
        driver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get().get(baseUrl);
        driver.get().manage().window().maximize();
    }
    
    // Closes the browser for each test method execution
    public void cleanUpAfterEachMethod(ITestContext context, ITestResult result) {
    	cleanUpAfterEachTestOrMethod(context,result );
    }
    
    // Closes the browser after each test is run
    public void cleanUpAfterEachTest(ITestContext context, ITestResult result) {
    	cleanUpAfterEachTestOrMethod(context,result );
    }
    
    public void cleanUpAfterMethodDoNotCloseBrowser(ITestContext context, ITestResult result) {
        Boolean isScreenshot = new Boolean(driverProperties.getProperty(
                "takeScreenshotOnFailure", "true"));
        if (result != null && isScreenshot && (!result.isSuccess())) {
            takeScreenshot(context, result);
        }
    }

    private void cleanUpAfterEachTestOrMethod(ITestContext context, ITestResult result) {
        RemoteWebDriver remoteDriver;
        Boolean isScreenshot = new Boolean(driverProperties.getProperty(
                "takeScreenshotOnFailure", "true"));

        if (result != null && isScreenshot && (!result.isSuccess())) {
            takeScreenshot(context, result);
        }

        if (driver != null && !driverType.equalsIgnoreCase("grid")) {
            // TODO: Temporary fix to continue running remaining tests even if
            // some test times out
            try {
                driver.get().close();
            }
            catch (Exception e) {
                logger.info("Trying to close driver but connection still allocated");
            }
        }
        else if (driverType.equalsIgnoreCase("grid")) {
            remoteDriver = (RemoteWebDriver) driver.get();

            try {
                if (remoteDriver == null) {
                }
                else if (remoteDriver.getSessionId() == null) {
                }
                else {
                    driver.get().quit();
                }
            }
            catch (Exception e) {
                // TODO:For chrome this exception is caught. Not clear why it
                // occurs.
                logger.info(e.getMessage());
            }
        }
    }

    // This is simply to close the browser
    public void cleanUpWebDriver() {
        if (driver != null && !driverType.equalsIgnoreCase("grid")) {
            // TODO: Temporary fix to continue running remaining tests even if
            // some test times out
            try {
                driver.get().quit();
            }
            catch (Exception e) {
                logger.info("Trying to quit driver but connection still allocated");
            }

            finally {
                driver = null;
            }
        }
    }

    private void takeScreenshot(ITestContext context, ITestResult result) {
        Reporter.setCurrentTestResult(result);
        try {
            System.setProperty("org.uncommons.reportng.escape-output", "false");
            WebDriver wDriver = driver.get();
            if (driverType.equalsIgnoreCase("grid")) {
                wDriver = new Augmenter().augment(driver.get());
            }
            File screenshotFile = ((TakesScreenshot) wDriver).getScreenshotAs(OutputType.FILE);
            String outputDirPath = new File(context.getOutputDirectory()).getParent();
            File outputDir = new File(outputDirPath + File.separator + "html"
                    + File.separator + Constants.SCREENSHOT_FOLDER_NAME);
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }
            File savedFileName = new File(outputDir.getPath(), result.getName() + "_"
                    + Utils.replaceSpecialCharacters(WebDriverUtil.getTimeStamp(null))
                    + Constants.IMG_EXTENSION);
            String relativePath = Constants.SCREENSHOT_FOLDER_NAME + "/"
                    + savedFileName.getName();
            FileUtils.copyFile(screenshotFile, savedFileName);
            String linkName = "<a style=\"background-color: rgb(255,255,0)\" href=\""
                    + relativePath + "\" target=\"_blank\">"
                    + Constants.SCREENSHOT_LINK_NAME + "</a> <br>";
            Reporter.log(linkName, true);
            Reporter.log(this.getBrowserContext(), true);
            Reporter.log("APP-URL="+baseUrl, true);

        }
        catch (Exception e) {
            Reporter.log("Error while taking screenshot (" + this.getBrowserContext()
                    + ").\n" + e.getMessage(), true);
        }
    }
    protected void setPropertiesFile(String propertiesFile) {
        if (propertiesFile != null && propertiesFile.trim().length() != 0) {
            this.propertiesFile = propertiesFile.trim();
            propertiesResource = null;
        }
    }

    protected void setPropertiesResource(String propertiesResource) {
        if (propertiesResource != null && propertiesResource.trim().length() != 0) {
            this.propertiesResource = propertiesResource.trim();
            propertiesFile = null;
        }
    }

    protected void setProperties(Properties properties) {
        driverProperties = properties;
    }

    protected Properties getProperties() {
        return driverProperties;
    }

    private void loadDriverProperties() {
        if (driverProperties != null)
            return;
        driverProperties = new Properties();
        try {
            if (propertiesResource != null) {
                Resource driverPropResource = new ClassPathResource(propertiesResource);
                driverProperties.load(driverPropResource.getInputStream());
            } else if (propertiesFile != null) {
                driverProperties.load(new FileInputStream(new File(propertiesFile)));
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private WebDriver getDriver() {
        if (driverType.equalsIgnoreCase("htmlunit")) {
            return new HtmlUnitDriver();
        }
        else if (driverType.equalsIgnoreCase("ie")) {
            return new InternetExplorerDriver(gridConfig);
        }
        else if (driverType.equalsIgnoreCase("firefox")) {
            return new FirefoxDriver();
        }
        else if (driverType.equalsIgnoreCase("chrome")) {
            return new ChromeDriver();
        }
        else if (driverType.equalsIgnoreCase("grid")) {
        	RemoteWebDriver remoteWebDriver = new RemoteWebDriver(gridHubUrl, gridConfig);
            Boolean useLocalFileDetector = new Boolean(driverProperties.getProperty(
                    "useLocalFileDetector", "false"));
        	if (useLocalFileDetector) {
        		remoteWebDriver.setFileDetector(new LocalFileDetector());
        	}
            return remoteWebDriver;
        }
        else {
            throw new RuntimeException("Invalid driver, driver should be either one of "
                    + "htmlunit, firefox, ie, chrome and grid");
        }
    }

    public void createDataSource() {
        // Load webdriver configuration from property file.
        loadDriverProperties();

        // Initialize dataSource
        init(driverProperties);
    }

    public void setGridDriverConfig() {
        gridConfig = new DesiredCapabilities();
        if (browserType.equals("ie")) {
            gridConfig = DesiredCapabilities.internetExplorer();
            gridConfig.setCapability("version", browserVersion);
            gridConfig.setCapability("platform", platformName);
            gridConfig.setCapability(
                    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true);
        }
        else if (browserType.equals("firefox")) {
            gridConfig = DesiredCapabilities.firefox();
            gridConfig.setCapability("version", browserVersion);
            gridConfig.setCapability("platform", platformName);
        }
        else if (browserType.equals("chrome")) {
            // System.setProperty("webdriver.chrome.driver",
            // "path/to/chromedriver");
            gridConfig = DesiredCapabilities.chrome();
            gridConfig.setCapability("version", browserVersion);
            gridConfig.setCapability("platform", platformName);
        }
        try {
            gridHubUrl = new URL(driverProperties.getProperty("hub",
                    "http://localhost:4444/wd/hub"));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url for hub, "
                    + "please provide right url where hub is up and running");
        }
    }

    private String getBrowserContext() {
        return this.platformName + ", " + this.browserType + this.browserVersion;
    }

    // ------------------- Abstract methods -----------------------------
    protected abstract void init(Properties driverProperties);

}
