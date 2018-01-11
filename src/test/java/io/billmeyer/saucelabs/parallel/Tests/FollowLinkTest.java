package io.billmeyer.saucelabs.parallel.Tests;

import io.billmeyer.saucelabs.parallel.Pages.GuineaPigPage;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class FollowLinkTest extends TestBase
{
    /**
     * Runs a simple test verifying link can be followed.
     *
     * @throws InvalidElementStateException
     */
    @Test(dataProvider = "hardCodedBrowsers")
    public void verifyLinkTest(String browser, String version, String os, String screenResolution, Method method) throws
            MalformedURLException, InvalidElementStateException
    {
        // Create our web session...
        createDriver(browser, version, os, screenResolution, method.getName());
        WebDriver driver = getWebDriver();

        annotateJob("Visiting GuineaPig page...");
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        annotateJob("Clicking on link...");
        page.followLink();

        annotateJob("Asserting that we are on a new page...");
        Assert.assertFalse(page.isOnPage());
    }
}