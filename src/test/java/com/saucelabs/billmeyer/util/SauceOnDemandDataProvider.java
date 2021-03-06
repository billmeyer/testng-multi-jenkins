package com.saucelabs.billmeyer.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SauceOnDemandDataProvider
{
    public static final String SAUCE_ONDEMAND_BROWSERS = "SAUCE_ONDEMAND_BROWSERS";

    /**
     * Constructs a List of Object array instances which represent a series of browser combinations.
     * The method retrieves and parses the value of the SAUCE_ONDEMAND_BROWSERS environment variable/system
     * property which is assumed to be in JSON format.
     *
     * @param testMethod Test method consuming the data
     * @return ArrayList of data provider (List of strings)
     */
    @DataProvider(name = "sauceOnDemandDataProvider")
    public static Iterator<Object[]> sauceOnDemandDataProvider(Method testMethod)
    {
        List<Object[]> data = new ArrayList<Object[]>();

        //read browsers from JSON-formatted environment variable if specified
        String json = Util.readPropertyOrEnv(SAUCE_ONDEMAND_BROWSERS, "[{\"browser\": \"firefox\",\"browser-version\": \"59.0\",\"os\": " + "\"macOS 10.13\"},{\"browser\": \"chrome\",\"browser-version\": \"66\",\"os\": \"macOS 10.13\"}]");

        System.out.printf("Sauce OnDemand Data Provider, json='%s'", json);

        if (json == null || json.equals(""))
        {
            throw new IllegalArgumentException("Unable to find JSON");
        }

        JSONArray browsers = new JSONArray(json);
        for (Object object : browsers)
        {
            JSONObject jsonObject = (JSONObject) object;
            data.add(new Object[]{jsonObject.get("browser"), jsonObject.get("browser-version"), jsonObject.get("os")});
        }

        return data.iterator();
    }

}