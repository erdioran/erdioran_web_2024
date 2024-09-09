package com.kariyer.base;


import com.kariyer.listener.SeleniumListener;
import com.kariyer.utils.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.HashMap;

public class DriverManager {
    private static final Logger LOGGER = LogManager.getLogger(DriverManager.class);
    private static final InheritableThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return WEB_DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(WebDriver driver) {
        WEB_DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        try {
            if (WEB_DRIVER_THREAD_LOCAL.get() != null) {
                WEB_DRIVER_THREAD_LOCAL.get().quit();
            }
        } catch (Exception e) {
            LOGGER.warn(e);
        }
    }


    public static void launchBrowser(String browser) throws MalformedURLException {
        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            if (ConfigManager.isHeadless()) {    //headless mode, background tests
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
            } else {
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--remote-debugging-port=9222");
            }
            chromeOptions.addArguments("--remote-allow-origins=*");  // for after chrome v111
            chromeOptions.addArguments("--disable-gpu");  // for windows headless mode
          //  chromeOptions.addArguments("user-data-dir=/Users/erdioran/Library/Application Support/Google/Chrome");
            chromeOptions.addArguments("profile-directory=Profile 3");
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            driver = new ChromeDriver(chromeOptions);
            driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
        } else {
            throw new IllegalArgumentException(
                    String.format("%s is invalid value. Enter valid browser value in config.properties", browser));
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getIntValue("implicit.wait.time", 0)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getIntValue("page.load.time.out", 60)));
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
        eventFiringWebDriver.register(new SeleniumListener());
        DriverManager.setDriver(eventFiringWebDriver);
    }

}

