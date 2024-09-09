package testCases;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.kariyer.utils.DataManager;
import com.kariyer.utils.ExtentTestManager;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static base.AutomationMethods.*;
import static com.kariyer.utils.Helper.sleepInSeconds;
import static com.kariyer.utils.UrlManager.getTestUrl;

public class ForgotPasswordTests extends BaseTest {

    @BeforeMethod()
    public void beforeForgotPasswordTest(Method method, ITestResult result) {
        ExtentTest test = ExtentTestManager.getNode();
        test.assignCategory("Forgot Password Scenarios");
    }

    @Test(description = "Password reset via email", priority = 1)
    public void passwordResetViaEmail() throws Exception {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Password reset via email");
        ExtentTestManager.info("Password reset via email");

        pageLoad(getTestUrl()+ DataManager.getData("url.login"));
        click("FORGOT_PASSWORD_BUTTON");
        enterText("FORGOT_PASSWORD_EMAIL",DataManager.getData("userData.TEST_USER_2_MAIL"));
        click("FORGOT_PASSWORD_GONDER_BUTTON");
        sleepInSeconds(5); // Capcha geçişi için. Test ortamında kullanmaya gerek yok
        Assert.assertTrue(elementVisibiltyWithSize("FORGOT_PASSWORD_MESSAGE"));
        Assert.assertEquals(getText("FORGOT_PASSWORD_MESSAGE"),"Şifre sıfırlama bağlantısını gönderdik");
        ExtentTestManager.info("Reset email send to test user mail.");
    }

    @Test(description = "Unregistered Mail Error Message", priority = 2)
    public void unregisteredMail() throws Exception {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Unregistered Mail Error Message");
        ExtentTestManager.info("Unregistered Mail Error Message");

        pageLoad(getTestUrl()+ DataManager.getData("url.login"));
        click("FORGOT_PASSWORD_BUTTON");
        enterText("FORGOT_PASSWORD_EMAIL","kariyertest111@gmail.com");
        click("FORGOT_PASSWORD_GONDER_BUTTON");
        sleepInSeconds(5);
        Assert.assertTrue(elementVisibiltyWithSize("FORGOT_PASSWORD_ERROR_MESSAGE"));
        Assert.assertEquals(getText("FORGOT_PASSWORD_ERROR_MESSAGE"),"Bir hata oluştu.");
        ExtentTestManager.info("An error was received for an unregistered email.");
    }



}
