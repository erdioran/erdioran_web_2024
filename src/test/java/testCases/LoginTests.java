package testCases;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.kariyer.base.DriverManager;
import com.kariyer.utils.DataManager;
import com.kariyer.utils.ExtentTestManager;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static base.AutomationMethods.*;
import static com.kariyer.utils.Helper.sleepInSeconds;
import static com.kariyer.utils.UrlManager.*;

public class LoginTests extends BaseTest {

    @BeforeMethod()
    public void beforLoginTest(Method method, ITestResult result) {
        ExtentTest test = ExtentTestManager.getNode();
        test.assignCategory("Login Scenarios");
    }

    @Test(description = "Success Login", priority = 1)
    public void succesLogin() throws Exception {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Success Login");
        ExtentTestManager.info("Success Login");

        pageLoad(getTestUrl()+ DataManager.getData("url.login"));
        enterText("LOGIN_USERNAME",DataManager.getData("userData.TEST_USER_1_MAIL"));
        enterText("LOGIN_PASSWORD",DataManager.getData("userData.TEST_USER_1_PASSWORD"));
        click("LOGIN_BUTTON");
        sleepInSeconds(5); // Capcha geçişi için. Test ortamında kullanmaya gerek yok
        Assert.assertTrue( elementVisibiltyWithSize("HOME_PROFILE_NAME"));
        ExtentTestManager.info("Success login with test user.");
    }

    @Test(description = "Wrong Password", priority = 2)
    public void wrongPassword() throws Exception {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Wrong Password");
        ExtentTestManager.info("Wrong Password");

        pageLoad(getTestUrl()+ DataManager.getData("url.login"));
        enterText("LOGIN_USERNAME",DataManager.getData("userData.TEST_USER_1_MAIL"));
        enterText("LOGIN_PASSWORD","TestTest123-");
        click("LOGIN_BUTTON");
        sleepInSeconds(5);
        Assert.assertTrue(elementVisibiltyWithSize("LOGIN_SERVER_MESSAGE"));
        Assert.assertEquals(getText("LOGIN_SERVER_MESSAGE"),"Kullanıcı adı veya şifre hatalı!");
        ExtentTestManager.info("A warning message was displayed after entering an incorrect password.");
    }

    @Test(description = "Password Validation", priority = 3)
    public void passwordValidation() throws Exception {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Password Validation");
        ExtentTestManager.info("Password Validation");

        pageLoad(getTestUrl()+ DataManager.getData("url.login"));
        enterText("LOGIN_USERNAME",DataManager.getData("userData.TEST_USER_1_MAIL"));
        enterText("LOGIN_PASSWORD","12");
        Assert.assertTrue(elementVisibiltyWithSize("LOGIN_VALIDATION_MESSAGE"));
        Assert.assertEquals(getText("LOGIN_VALIDATION_MESSAGE"),"En az 3 karakter girmelisin");
        ExtentTestManager.info("A warning message was displayed after entering an incomplete password.");
    }


}
