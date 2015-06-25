/*
 * Copyright (c) 2014 by Salesforce.com Inc.  All Rights Reserved.
 * This file contains proprietary information of Salesforce.com Inc.
 * Copying, use, reverse engineering, modification or reproduction of
 * this file without prior written approval is prohibited.
 *
 */

package com.elementum.webdriver.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;


public class WebSiteBasePage {

    protected WebDriver driver;
    private final int WAIT_TIMEOUT = 30;
    public WebDriverWait wait ;
    public enum SELECT_TYPE {
        INDEX, VALUE, TEXT
    };
    public WebSiteBasePage (WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, WAIT_TIMEOUT),
                this);
    }

    public void waitForElementShowUp(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public WebElement waitForElementShowXpath(String xPathElem) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathElem)));
        return element;
    }
    
    public WebElement waitForElementShowId(String idElem) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(idElem)));
        return element;
    }
    
    public void waitForElementClickableXpath(String xPath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
    }
    
    public void waitForElementClickableId(String id) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(id)));
    }

    public String getMessage(WebElement element) {
        return element.getText();
    }

    public String getAttribute(WebElement element) {
        return element.getAttribute("value");
    }

    public void selectOption(WebElement element, String option, SELECT_TYPE type) {
        Select select = new Select(element);
        switch (type) {
            case INDEX:
                select.selectByIndex(Integer.parseInt(option));
                break;
            case VALUE:
                select.selectByValue(option);
                break;
            case TEXT:
                select.selectByVisibleText(option);
                break;
        }
    }

    public String getCurrentSelection(WebElement element) {
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();

    }

    public WebElement waitForElementToShowUp(final By locator, long waitTime) {
        WebDriverWait wdw = new WebDriverWait(driver, waitTime);
        return wdw.until(presenceOfElementLocated(locator));
    }

    private Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return new Function<WebDriver, WebElement>() {

            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    public void clickBackButton() {
        driver.navigate().back();
    }

    public void clickForwardButton() {
        driver.navigate().forward();
    }
}
