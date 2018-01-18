/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */
package uk.q3c.krail.testbench;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.testbench.page.object.LoginFormPageObject;
import uk.q3c.krail.testbench.page.object.LoginStatusPageObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.phantomjs.PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX;
import static uk.q3c.krail.testbench.KrailTestBenchTestCase.DRIVER_TYPE.HEADLESS;

public class KrailTestBenchTestCase extends TestBenchTestCase {
    private static Logger log = LoggerFactory.getLogger(KrailTestBenchTestCase.class);
    protected final StringBuffer verificationErrors = new StringBuffer();
    protected Locale browserLocale = Locale.UK;
    protected String baseUrl = "http://localhost:8080/";
    protected LoginStatusPageObject loginStatus = new LoginStatusPageObject(this);
    protected LoginFormPageObject loginForm = new LoginFormPageObject(this);
    protected String appContext = "testapp";
    protected DRIVER_TYPE defaultDriverType = HEADLESS;
    private int currentDriverIndex = 0;
    private List<WebDriver> drivers = new ArrayList<>();

    @Before
    public void baseSetup() throws Exception {
        System.out.println("setting up base test bench case");

        addDefaultDriver();
        getDriver().manage()
                .window()
                .setPosition(new Point(0, 0));
        getDriver().manage()
                .window()
                .setSize(new Dimension(1024, 768));
        System.out.println("default driver added");
        System.out.println("current driver index set to " + currentDriverIndex);
    }

    public DRIVER_TYPE getDefaultDriverType() {
        return defaultDriverType;
    }

    protected WebDriver addDefaultDriver() {
        switch (defaultDriverType) {
            case HEADLESS:
                return addDriver(createPhantomJSDriver());
            case FIREFOX:
                return addDriver(createFirefoxDriver());
            case CHROME:
                throw new UnsupportedOperationException("Support for Chrome not yet added");
            default:
                throw new UnsupportedOperationException("Unknown dirver option");
        }
    }

    protected WebDriver createPhantomJSDriver() {
        DesiredCapabilities caps = DesiredCapabilities.phantomjs();
        caps.setCapability(PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", browserLocale.toLanguageTag());
        return new PhantomJSDriver();
    }

    /**
     * Adds a driver, and if it is the first to be added, also sets it as default driver (the default driver is the 'driver' property of the  {@link
     * TestBenchTestCase}
     *
     * @param driver
     */
    protected WebDriver addDriver(WebDriver driver) {
        System.out.println("adding driver " + drivers.size());
        WebDriver realDriver;
        if (!(driver instanceof TestBenchDriverProxy)) {
            realDriver = TestBench.createDriver(driver);
        } else {
            realDriver = driver;
        }
        drivers.add(realDriver);

        if (drivers.size() == 1) {
            this.driver = realDriver;
        }
        return realDriver;
    }

    protected WebDriver createFirefoxDriver() {
        System.out.println("Creating Firefox driver");
        FirefoxProfile profile = createFirefoxProfile(browserLocale);
        return TestBench.createDriver(new FirefoxDriver(profile));
    }

    protected FirefoxProfile createFirefoxProfile(Locale locale) {
        FirefoxProfile profile = new FirefoxProfile();
        String s1 = locale.toLanguageTag()
                .toLowerCase()
                .replace('_', '-');
        profile.setPreference("intl.accept_languages", s1);
        return profile;
    }

    protected WebDriver createChromeDriver() {
        System.out.println("Creating Chrome driver");
        return TestBench.createDriver(new ChromeDriver());
    }

    /**
     * The same as {{@link #getDriver(int)} with index of {@link #currentDriverIndex}
     *
     * @return the WebDriver instance at index currentDriverIndex
     */
    @Override
    public WebDriver getDriver() {
        //needed because @Rule assumes that null will be returned when there is no driver
        if (drivers.isEmpty()) {
            return null;
        }
        return drivers.get(currentDriverIndex);
    }

    @SuppressFBWarnings("PCAIL_POSSIBLE_CONSTANT_ALLOCATION_IN_LOOP")
    public boolean waitForUrl(String fragment) {
        int timeout = 5000;
        long startTime = new Date().getTime();
        long elapsedTime = 0;
        String expected = rootUrl() + '#' + fragment;
        String actual = getDriver().getCurrentUrl();
        while (!actual.equals(expected) && (elapsedTime < timeout)) {
            actual = getDriver().getCurrentUrl();
            elapsedTime = new Date().getTime() - startTime;
            System.out.println("waiting for url: " + fragment + ' ' + elapsedTime + "ms");
        }
        return elapsedTime < timeout;
    }

    /**
     * "starts" the current driver by navigating the current driver to the {@link #rootUrl}.
     */
    protected void startDriver() {
        getDriver().get(rootUrl());
        waitForUrl("home");
    }

    protected String rootUrl() {
        String rootUrl = buildUrl(baseUrl, appContext);
        //Tomcat has issues when there is no trailing slash, so make sure it is there
        if (!rootUrl.endsWith("/")) {
            rootUrl += '/';
        }
        return rootUrl;
    }

    protected String buildUrl(String... segments) {
        StringBuilder buf = new StringBuilder();
        boolean firstSegment = true;
        for (String segment : segments) {
            if (!firstSegment) {
                buf.append('/');
            } else {
                firstSegment = false;
            }
            buf.append(segment.replace("/", ""));
        }
        String result = buf.toString();
        // slashes will have been removed
        result = result.replace("http:", "http://");
        return result.replace("https:", "https://");
    }

    /**
     * Browsers need to close the drivers here, but PhantomJS complains about that
     */
    @After
    public void baseTearDown() {

        System.out.println("closing all drivers, excluding HEADLESS");
        for (WebDriver webDriver : drivers) {
            if (!(webDriver instanceof PhantomJSDriver)) {
                System.out.println("closing web driver: " + webDriver.getTitle());
                webDriver.close();
            }
        }
        //        if (!drivers.contains(driver)) {
        //            driver.close();//in case it was set directly and not through addDriver
        //        }

        System.out.println("clearing drivers list");
        drivers.clear();
        pause(1000);
    }

    /**
     * Pause is ignored when running HEADLESS
     *
     * @param milliseconds
     */
    public void pause(int milliseconds) {
        if (defaultDriverType != DRIVER_TYPE.HEADLESS) {

            try {
                Thread.sleep(milliseconds);
            } catch (Exception e) {
                log.error("Sleep was interrupted");
            }
        }
    }

    /**
     * Navigates the current driver to {@code fragment}
     *
     * @param fragment
     */
    protected void navigateTo(String fragment) {
        String url = url(fragment);
        getDriver().get(url);
        waitForUrl(fragment);
    }

    public String getAppContext() {
        return appContext;
    }

    protected void verifyUrl(String fragment) {
        String expected = rootUrl() + '#' + fragment;
        String actual = getDriver().getCurrentUrl();
        assertThat(actual).isEqualTo(expected);
    }

    protected void verifyNotUrl(String fragment) {
        String expected = rootUrl() + fragment;
        String actual = getDriver().getCurrentUrl();
        assertThat(actual).isNotEqualTo(expected);
    }

    protected void navigateForward() {
        getDriver().navigate()
                .forward();
        pause(500);
    }

    protected void navigateWithRedirectExpected(String requestedUrl, String expectedUrl) {
        String url = url(requestedUrl);
        getDriver().get(url);
        waitForUrl(expectedUrl);
    }

    protected String url(String fragment) {
        return rootUrl() + '#' + fragment;
    }

    public WebDriver getDriver(int index) {
        //needed because @Rule assumes that null will be returned when there is no driver
        if (drivers.isEmpty()) {
            return null;
        }
        return drivers.get(index);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected void navigateBack() {
        getDriver().navigate()
                .back();
        pause(500);
    }

    /**
     * shorthand method to click the login button, and fill in the login form using credentials in {@link #loginForm}
     */
    protected void login() {
        loginStatus.loginButton()
                .click();
        System.out.println("login status button clicked");
        loginForm.login();
    }

    protected void closeNotification() {
        notification().close();
    }

    protected NotificationElement notification() {
        return $(NotificationElement.class).get(0);
    }

    /**
     * shorthand method to click the login button, and fill in the login form using credentials in {@link #loginForm} and using enter key to submit
     */
    protected void loginWithEnterKey() {
        loginStatus.loginButton()
                .click();
        System.out.println("login status button clicked");
        loginForm.loginWithEnterKey();
    }

    protected <E extends AbstractElement> E element(Class<E> elementClass, Optional<?> qualifier, Class<?>... componentClasses) {

        return element(elementClass, ID.getIdc(qualifier, componentClasses));
    }

    public <E extends AbstractElement> E element(Class<E> elementClass, String id) {
        return $(elementClass).id(id);
    }

    /**
     * Indexed from 0 (that is, the default driver is at index 0)
     *
     * @param index
     * @return
     */
    @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS")
    public WebDriver selectDriver(int index) {
        try {
            WebDriver wd = drivers.get(index);
            currentDriverIndex = index;
            setDriver(wd);
            System.out.println("Driver index " + index + " selected");
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Driver index of " + index + " is invalid", e);
        }
    }

    protected WebDriver driver(int index) {
        return drivers.get(index);
    }

    public enum DRIVER_TYPE {
        HEADLESS, FIREFOX, CHROME
    }


}
