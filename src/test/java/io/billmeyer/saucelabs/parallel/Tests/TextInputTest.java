package io.billmeyer.saucelabs.parallel.Tests;

import io.billmeyer.saucelabs.parallel.Pages.GuineaPigPage;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Date;

public class TextInputTest extends TestBase
{
    /**
     * Runs a simple test verifying if the comment input is functional.
     *
     * @throws InvalidElementStateException
     */
    @org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
    public void verifyCommentInputTest(String browser, String version, String os, String screenResolution, Method method) throws
            MalformedURLException, InvalidElementStateException
    {
        createDriver(browser, version, os, screenResolution, method.getName());

        WebDriver driver = getWebDriver();

        String commentInputText = new Date().toString();

        annotateJob("Visiting GuineaPig page...");
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        annotateJob(String.format("Submitting comment: [%s]", commentInputText));
        page.submitComment(commentInputText);

        annotateJob(String.format("Asserting submitted comment is: [%s]", commentInputText));
        Assert.assertTrue(page.getSubmittedCommentText().contains(commentInputText));
    }
}