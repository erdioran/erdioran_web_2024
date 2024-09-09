package base;

import com.kariyer.base.DriverManager;
import com.kariyer.utils.ConfigManager;
import com.kariyer.utils.ExtentTestManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentTest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import static base.AutomationMethods.takeScreenShot;

public abstract class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void startBrowser(Method method, ITestResult result, ITestContext context) throws MalformedURLException {
        ThreadContext.put("testName", method.getName());
        LOGGER.info("Executing test method : [{}] in class [{}]", result.getMethod().getMethodName(),
                result.getTestClass().getName());
        String nodeName =
                StringUtils.isNotBlank(result.getMethod().getDescription()) ? result.getMethod().getDescription() : method.getName();
//        ExtentTest node = ExtentTestManager.getTest().createNode(nodeName);
 //       ExtentTestManager.setNode(node);
   //     ExtentTestManager.info("Test Started");
        DriverManager.launchBrowser(ConfigManager.getBrowser());
    }


    @AfterMethod(alwaysRun = true)
    public void closeBrowser(ITestResult result, ITestContext context) {
        if (!result.isSuccess()) {
            takeScreenShot("Failure screenshot");
            context.setAttribute("previousTestStatus", "failed");
        } else {
            context.setAttribute("previousTestStatus", "passed");
        }
        boolean isNewBrowserPerTest = Boolean.parseBoolean(ConfigManager.getConfigProperty("new.browser.per.test"));
        boolean isCleanUpTest = context.getName().contains("Clean");
        if (!result.isSuccess() || isNewBrowserPerTest && !isCleanUpTest) {
            DriverManager.quitDriver();
        }
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        DriverManager.quitDriver();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        DriverManager.quitDriver();
    }

}
