package com.saucelabs.billmeyer.tests;

import com.saucelabs.billmeyer.pages.GuineaPigPage;
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
    public void verifyLinkTest(String browser, String version, String os, String screenResolution, Method method)
    throws MalformedURLException, InvalidElementStateException
    {
        // Create our web session...
        WebDriver driver = createDriver(browser, version, os, screenResolution, method.getName());
        Assert.assertNotNull(driver);

        annotateJob("Visiting GuineaPig page...");
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        annotateJob("Clicking on link...");
        page.followLink();

        annotateJob("Asserting that we are on a new page...");
        Assert.assertFalse(page.isOnPage());
    }
}