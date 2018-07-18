package com.sa.pages.common;

import com.pm.automation.logging.Logging;
import com.pm.automation.webdriver.Browser;
import com.pm.automation.webdriver.PageActions;
import com.pm.automation.webdriver.TestContext;
import com.pm.automation.webdriver.TestContext.Context;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.util.Set;

public abstract class BasePage extends PageActions implements Logging {
    protected Logger log = getLogger();
    protected WebDriver driver = null;
    protected WebDriverWait wait = null;
    protected JavascriptExecutor jsExecutor = null;
    protected Actions actions = null;

    public BasePage() {
        Context context = TestContext.get();
        driver = context.getDriver();
        wait = context.getWebDriverWait();
        jsExecutor = context.getJavascriptExecutor();
        actions = context.getActions();
        PageFactory.initElements(driver, this);

    }

    public void clickElementByAction(WebElement element) {
        actions.moveToElement(element).click().build().perform();
    }

    public void clickElementByJSExecutor(WebElement element) {

        jsExecutor.executeScript("arguments[0].click();", element);
    }

    /**
     * Waits for Javascript and jQuery to finish loading.
     *
     * @return a result true or false
     */
    public boolean waitForJSandJQueryToLoad() {

        ExpectedCondition<Boolean> jQueryLoad = (driver) -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };

        ExpectedCondition<Boolean> jsLoad = (driver) -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                .equals("complete");

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public boolean elementPresent(By by) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void closeNewlyOpenedWindowIfAny() {
        String winHandleBefore = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();

        for (String winHandle : windowHandles) {
            if (!winHandle.equals(winHandleBefore)) {
                driver.switchTo().window(winHandle).close();
            }
        }
        driver.switchTo().window(winHandleBefore);
    }

}
