package io.billmeyer.saucelabs.parallel.Tests;

import com.saucelabs.saucerest.SauceREST;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider in order to supply multiple browser combinations.
 *
 * @author Neil Manvar
 */
public class TestBase
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

    private ThreadLocal<SauceREST> sauceRestThreadLocal = new ThreadLocal<SauceREST>();

    /**
     * DataProvider that explicitly sets the browser combinations to be used.
     *
     * @param testMethod
     * @return Two dimensional array of objects with browser, version, and platform information
     */
    @DataProvider(name = "hardCodedBrowsers", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod)
    {
        /**
         * Create an array of test OS/Browser/Screen Resolution combinations we want to test on.
         * @see https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-SpecifyingtheScreenResolution
         */

        // @formatter:off
        return new Object[][]{
                new Object[]{"MicrosoftEdge", "14.14393", "Windows 10", "1920x1080"},
                new Object[]{"firefox", "49.0", "Windows 10", "1440x900"},
                new Object[]{"internet explorer", "11.0", "Windows 7", "1280x1024"},
                new Object[]{"safari", "11.0", "OS X 10.13", "1600x1200"},   // High Sierra
                new Object[]{"firefox", "57.0", "OS X 10.13", "1600x1200"},  // High Sierra
                new Object[]{"safari", "11.0", "OS X 10.12", "1600x1200"},   // Sierra
//                new Object[]{"safari", "10.0", "OS X 10.11", "1280x960"},   // El Capitan
//                new Object[]{"safari", "9.0", "OS X 10.11", "1280x960"},    // El Capitan
                new Object[]{"chrome", "54.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "55.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "56.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "57.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "58.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "59.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "60.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "61.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "62.0", "OS X 10.10", "1440x900"},
                new Object[]{"chrome", "63.0", "OS X 10.10", "1440x900"},
                new Object[]{"firefox", "latest-1", "Windows 7", "1440x900"},
                new Object[]{"firefox", "latest-1", "Windows 8", "1440x900"},
                new Object[]{"firefox", "latest-1", "Windows 8.1", "1440x900"},
                new Object[]{"firefox", "latest-1", "Windows 10", "1440x900"},
                new Object[]{"chrome", "48.0", "Linux", "1024x768"},
                new Object[]{"firefox", "45.0", "Linux", "1024x768"}
        };
        // @formatter:on
    }

    /**
     * @return the {@link WebDriver} for the current thread
     */
    public RemoteWebDriver getWebDriver()
    {
        return webDriverThreadLocal.get();
    }

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

        webDriverThreadLocal.get().executeScript("sauce:context=" + text);
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the browser,
     * version and os parameters, and which is configured to run against ondemand.saucelabs.com, using
     * the userName and access key populated by the {@link #authentication} instance.
     *
     * @param browser    Represents the browser to be used as part of the test run.
     * @param version    Represents the version of the browser to be used as part of the test run.
     * @param os         Represents the operating system to be used as part of the test run.
     * @param methodName Represents the name of the test case that will be used to identify the test on Sauce.
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    protected RemoteWebDriver createDriver(String browser, String version, String os, String screenResolution, String methodName) throws
            MalformedURLException
    {
        DesiredCapabilities caps = new DesiredCapabilities();

        // set desired capabilities to launch appropriate browser on Sauce
        caps.setCapability(CapabilityType.BROWSER_NAME, browser);
        caps.setCapability(CapabilityType.VERSION, version);
        caps.setCapability(CapabilityType.PLATFORM, os);
        caps.setCapability("name", String.format("%s - %s %s on %s [%s]", methodName, browser, version, os, new Date()));
        caps.setCapability("screenResolution", screenResolution);
        caps.setCapability("seleniumVersion", "3.7.1");
        caps.setCapability("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));


//        if (buildTag != null)
//        {
//            caps.setCapability("build", buildTag);
//        }

        URL url = new URL("https://" + userName + ":" + accesskey + "@ondemand.saucelabs.com:443/wd/hub");

        // Launch the remote browser and set it as the current thread
        RemoteWebDriver driver = new RemoteWebDriver(url, caps);
        webDriverThreadLocal.set(driver);

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
        System.out.printf("JOB_NAME: %s\n", jobName);
        System.out.printf("BUILD_NUMBER: %s\n", System.getenv("BUILD_NUMBER"));
        System.out.printf("JENKINS_BUILD_NUMBER: %s\n", System.getenv("JENKINS_BUILD_NUMBER"));
        System.out.printf("SauceOnDemandSessionID=%1$s job-name=%2$s", sessionId, jobName);
        
        /**
         * There are two methods of annotating Sauce Jobs:
         *
         * 1. Using the JavascriptExecutor which only requires a RemoteWebDriver instance.
         *      @see {@link #annotateJob(String text)} for an example
         *
         * 2. Using the Sauce Labs REST API which requires the credentials used to authenticate to Sauce.
         *      @see {@link #tearDown(ITestResult)} for an example
         */
        SauceREST sauceREST = new SauceREST(userName, accesskey);
        sauceRestThreadLocal.set(sauceREST);

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

        SauceREST sauceRest = sauceRestThreadLocal.get();
        String sessionId = sessionIdThreadLocal.get();

        if (result != null && result.isSuccess()) sauceRest.jobPassed(sessionId);
        else sauceRest.jobFailed(sessionId);

        webDriverThreadLocal.get().quit();
    }
}
