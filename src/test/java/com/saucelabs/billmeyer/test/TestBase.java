package com.saucelabs.billmeyer.test;

import com.saucelabs.billmeyer.util.SauceOnDemandDataProvider;
import com.saucelabs.billmeyer.util.Util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider in order to supply multiple browser combinations.
 *
 * @author Neil Manvar
 */
public class TestBase extends SauceOnDemandDataProvider
{

//    public String buildTag = System.getenv("BUILD_TAG");

    public String userName = System.getenv("SAUCE_USERNAME");

    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    /**
     * ThreadLocal variable which contains the  {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private ThreadLocal<RemoteWebDriver> webDriverThreadLocal = new ThreadLocal<RemoteWebDriver>();

    /**
     * ThreadLocal variable which contains the Sauce Job Id.
     */
    private ThreadLocal<String> sessionIdThreadLocal = new ThreadLocal<String>();

    /**
     * @return the Sauce Job id for the current thread
     */
    public String getSessionId()
    {
        return sessionIdThreadLocal.get();
    }

    protected void annotateJob(String text)
    {
        /**
         * Example of using the JavascriptExecutor to annotate the job execution as it runs
         *
         * @see https://wiki.saucelabs.com/display/DOCS/Annotating+Tests+with+Selenium%27s+JavaScript+Executor
         */

        if (webDriverThreadLocal.get() != null)
        {
            webDriverThreadLocal.get().executeScript("sauce:context=" + text);
        }
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the browser,
     * browserVersion and os parameters, and which is configured to run against ondemand.saucelabs.com, using
     * the userName and access key populated by the instance.
     *
     * @param browser    Represents the browser to be used as part of the test run.
     * @param browserVersion    Represents the browserVersion of the browser to be used as part of the test run.
     * @param os         Represents the operating system to be used as part of the test run.
     * @param methodName Represents the name of the test case that will be used to identify the test on Sauce.
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    protected RemoteWebDriver createDriver(String browser, String browserVersion, String os, String methodName) throws
            MalformedURLException
    {
        DesiredCapabilities caps = new DesiredCapabilities();

        // set desired capabilities to launch appropriate browser on Sauce
        caps.setCapability(CapabilityType.BROWSER_NAME, browser);
        caps.setCapability(CapabilityType.VERSION, browserVersion);
        caps.setCapability(CapabilityType.PLATFORM, os);
//        caps.setCapability("name", String.format("%s - %s %s on %s [%s]", methodName, browser, browserVersion, os, new Date()));
//        caps.setCapability("name", String.format("%s - %s %s on %s", methodName, browser, browserVersion, os));
        caps.setCapability("name", String.format("%s", methodName));
        caps.setCapability("seleniumVersion", "3.7.1");
        caps.setCapability("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));

        URL url = new URL("https://" + userName + ":" + accesskey + "@ondemand.saucelabs.com:443/wd/hub");
        RemoteWebDriver driver = null;

        try
        {
            // Launch the remote browser and set it as the current thread
            driver = new RemoteWebDriver(url, caps);
            webDriverThreadLocal.set(driver);
        }
        catch (org.openqa.selenium.WebDriverException e)
        {
            System.err.printf("WebDriver Error: %s\n", e.getMessage());
            return null;
        }

        // Save the session ID
        String sessionId = driver.getSessionId().toString();
        sessionIdThreadLocal.set(sessionId);

        String jobName = System.getenv("JOB_NAME");

        /**
         * The following environment variables will be populated when running in a Jenkins pipeline using the Sauce plugins. We output
         * these so that the saucePublisher will properly pick up the required output from the test results and publish them to the Sauce
         * Dashboard.
         * @see https://wiki.saucelabs.com/display/DOCS/Setting+Up+Reporting+between+Sauce+Labs+and+Jenkins
         * @see https://wiki.saucelabs.com/display/DOCS/Using+the+Sauce+Labs+Jenkins+Plugin+with+Jenkins+Pipeline
         */

        if (jobName != null)
        {
            System.out.printf("JOB_NAME: %s\n", jobName);
            System.out.printf("BUILD_NUMBER: %s\n", System.getenv("BUILD_NUMBER"));
            System.out.printf("JENKINS_BUILD_NUMBER: %s\n", System.getenv("JENKINS_BUILD_NUMBER"));
            System.out.printf("SauceOnDemandSessionID=%1$s job-name=%2$s\n", sessionId, jobName);
        }

        /**
         * There are two methods of annotating Sauce Jobs:
         *
         * 1. Using the JavascriptExecutor which only requires a RemoteWebDriver instance.
         *      @see {@link #annotateJob(String text)} for an example
         *
         * 2. Using the Sauce Labs REST API which requires the credentials used to authenticate to Sauce.
         *      @see {@link #tearDown(ITestResult)} for an example
         */
//        SauceREST sauceREST = new SauceREST(userName, accesskey);
//        sauceRestThreadLocal.set(sauceREST);

        return webDriverThreadLocal.get();
    }

    /**
     * Method that gets invoked after test.
     * Sets the job status (PASS or FAIL) and closes the browser.
     */
    @AfterMethod
    public void tearDown(ITestResult result) throws Exception
    {
        /**
         * Example of using the Sauce Labs REST API to set the Job Status.
         *
         *  @see https://wiki.saucelabs.com/display/DOCS/Annotating+Tests+with+the+Sauce+Labs+REST+API
         */

        WebDriver driver = webDriverThreadLocal.get();
        String sessionId = sessionIdThreadLocal.get();

        if (driver != null && sessionId != null)
        {
            if (result != null && result.isSuccess())
            {
                Util.reportSauceLabsResult(driver, true);
            }
            else
            {
                Util.reportSauceLabsResult(driver, false);
            }
            webDriverThreadLocal.get().quit();
        }
    }
}
