package base;


import com.aventstack.extentreports.MediaEntityBuilder;
import com.kariyer.base.DriverManager;
import com.kariyer.utils.ConfigManager;
import com.kariyer.utils.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import static com.kariyer.utils.ElementManager.returnElement;
import static com.kariyer.utils.Helper.sleepInSeconds;


public class AutomationMethods {


    private static final Logger LOGGER = LogManager.getLogger(AutomationMethods.class);


    public static void pageLoad(String url) {
        DriverManager.getDriver().get(url);
    }

    public static void takeScreenShot(String failureScreenshot){
        if (ConfigManager.getBooleanValue("capture.all.screens", true)) {
            String base64Screenshot = ((TakesScreenshot) Objects.requireNonNull(DriverManager.getDriver()))
                    .getScreenshotAs(OutputType.BASE64);
            try {
                ExtentTestManager.getNode().info("", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
            catch (IOException exception){
                //
            }
        }
    }

    public static String getBase64Screenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }


    public static void enterText(String element, String textToEnter) throws Exception {
        WebElement webElement = findObject(returnElement(element));
        webElement.clear();
        webElement.sendKeys(textToEnter);
    }

    public static String getText(String element) throws Exception {
        return findObject(returnElement(element)).getText().trim();
    }

    public static WebElement findObject(By by) throws Exception {
        FluentWait<WebDriver> wait = getFluentWait();
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static List<WebElement> waitAllElement(By selector) {
        FluentWait<WebDriver> wait = getFluentWait();
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
    }


    public static void click(String element) throws Exception {
        waitForIntervalsAndClick(returnElement(element), 1, ConfigManager.getExplicitWaitTime());
    }
    public static void clickPath(By element) throws Exception {
        waitForIntervalsAndClick(element, 1, ConfigManager.getExplicitWaitTime());
    }


    public static void waitForIntervalsAndClick(By by, int interval, int maxWait) throws Exception {
        boolean elementExists = false;
        int counter = 0;
        while (counter <= maxWait) {
            try {
                DriverManager.getDriver().findElement(by).click();
                elementExists = true;
                counter = maxWait + 1;
            } catch (Exception e) {
                LOGGER.info("Web element [{}] | Click attempt : [{}]", by.toString(), counter);
                sleepInSeconds(interval);
                counter++;
                elementExists = false;
            }
        }
        if (!elementExists) {
            DriverManager.getDriver().findElement(by).click();
        }
    }


    public static FluentWait<WebDriver> getFluentWait(int intervalInSeconds, int maxWaitTimeInSeconds) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(intervalInSeconds))
                .pollingEvery(Duration.ofSeconds(maxWaitTimeInSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    public static FluentWait<WebDriver> getFluentWait() {
        return getFluentWait(1, ConfigManager.getExplicitWaitTime());
    }

    public static void switchToTab() {
        // açık olan diğer taba geçer.

        String main = DriverManager.getDriver().getWindowHandle();
        for (String windowHandle : DriverManager.getDriver().getWindowHandles()) {
            if (!main.contentEquals(windowHandle)) {
                DriverManager.getDriver().switchTo().window(windowHandle);
                LOGGER.info("Tab switch");
                break;
            }
        }
    }

    public static int getTabCount() {
        // açık tab sayısını döndürür

        return DriverManager.getDriver().getWindowHandles().size();
    }

    public static String getTabUrl() {
        // aktif tab'ın url'ini döndürür

        return DriverManager.getDriver().getCurrentUrl();
    }


    public static boolean elementVisibiltyWithSize(String element) throws Exception {
        // by ile gönderilen elementi, tüm sayfa kodu içinde arar. olup olmadığını döndürür
        By by = returnElement(element);
        return DriverManager.getDriver().findElements(by).size() > 0;
    }


    public static boolean emailPatternMatches(String emailAddress) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(emailAddress)
                .matches();
    }

    public static boolean checkStringContains(String text, String value) {
        // text stringini içinde, value değerinin olup olmadığını döndürür

        if (text.contains(value)) {
            return true;
        } else {
            return false;
        }
    }



    public static int getListSize(By by) {
        // by ile gönderilen elementin, size'ını (sayfada kaç adet olduğunu) döndürür)

        return DriverManager.getDriver().findElements(by).size();
    }

    public static int getRandomNumberBetween(int start, int end) {
        // start - end sayıları arasında random bir sayı döndürür.

        return (int) (Math.random() * end) + start;
    }

    public static void pageScroollDownToTargetElement(By by) {
        // by değeri ile gönderilen elemente kadar, sayfayı aşağı scroll eder.

        DriverManager.getDriver().findElement(by).sendKeys(Keys.ARROW_DOWN);
        LOGGER.info("Page scroll down");
    }

    public static void pageScroollUpToTargetElement(By by) {
        // by değeri ile gönderilen elemente kadar, sayfayı yukarı scroll eder.

        DriverManager.getDriver().findElement(by).sendKeys(Keys.ARROW_UP);
        LOGGER.info("Page scroll up");
    }

    public static void scrollToElementCenter(By by) {
        // Elementi sayfanın ortasında tutacak şekilde scroll eder
        WebElement element = DriverManager.getDriver().findElement(by);

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        int windowHeight = DriverManager.getDriver().manage().window().getSize().getHeight();
        int elementPositionY = element.getLocation().getY();
        int elementHeight = element.getSize().getHeight();

        int scrollToY = elementPositionY - (windowHeight / 2) + (elementHeight / 2);
        js.executeScript("window.scrollTo(0, " + scrollToY + ")");
    }


    public static String getElementBackgroundColor(By by) {
        String buttonColor = DriverManager.getDriver().findElement(by).getCssValue("background-color");
        return buttonColor;
    }

    public static String getElementFontColor(By by) {
        String buttonColor = DriverManager.getDriver().findElement(by).getCssValue("color");
        return buttonColor;
    }

    public static void backPage() {
        // tarayıcıda back işlevi yapar

        DriverManager.getDriver().navigate().back();
    }

    public static void refreshPage() {
        // sayfayı refreshler

        DriverManager.getDriver().navigate().refresh();
    }


    public static void keyboardEnter() {
        // KlavYeden enter tuşuna basar.

        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(Keys.ENTER).perform();
    }

    public static void scrollPageToTop() {
        // Sayfayı en üste scroll eder.

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(0, 0)");
    }

    public static void switchDriverIframe(By by) {
        DriverManager.getDriver().switchTo().frame(DriverManager.getDriver().findElement(by));
    }

    public static void switchDriverDefault() {
        DriverManager.getDriver().switchTo().defaultContent();
    }


    public static void closeTab() {
        // açık olan tabı kapatır
        DriverManager.getDriver().close();
    }


    public static void closeTabOtherThanTheMainTab() {
        // Bu yöntem, driver nesnesi üzerinden yönetilen tüm sekmelerin tanımlarını alır ve ana sekme dışındaki tüm sekmeleri dolaşır.
        // Dolaşırken, her bir sekme için ana sekmeden farklı bir ID kontrolü yapar.
        // Ana sekmeden farklı bir ID'ye sahip olan sekmeler kapatılır ve sonunda ana sekme tekrar seçil

        String mainTab = DriverManager.getDriver().getWindowHandle();
        Set<String> allTabs = DriverManager.getDriver().getWindowHandles();

        for (String tab : allTabs) {
            if (!tab.equals(mainTab)) {
                DriverManager.getDriver().switchTo().window(tab);
                DriverManager.getDriver().close();
            }
        }

        DriverManager.getDriver().switchTo().window(mainTab);

    }

    public static boolean waitForElement(String element, int timeoutInSeconds) {
        int attempts = 0;
        while (attempts < timeoutInSeconds) {
            try {
                if (elementVisibiltyWithSize(element)) {
                    return true;
                }
            } catch (NoSuchElementException e) {
                sleepInSeconds(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            attempts++;
        }
        return false;
    }
}
