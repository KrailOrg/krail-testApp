/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.co.q3c.v7.testapp.testbench;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.q3c.util.ID;
import uk.co.q3c.v7.base.view.DefaultLoginView;
import uk.co.q3c.v7.base.view.component.DefaultBreadcrumb;
import uk.co.q3c.v7.base.view.component.DefaultLoginStatusPanel;
import uk.co.q3c.v7.base.view.component.DefaultSubpagePanel;
import uk.co.q3c.v7.base.view.component.NavigationButton;

import com.vaadin.testbench.By;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.commands.TestBenchElementCommands;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class V7TestBenchTestCase extends TestBenchTestCase {
	private static Logger log = LoggerFactory.getLogger(V7TestBenchTestCase.class);
	protected String baseUrl = "http://localhost:8080/";
	protected String context = "testapp";
	protected final StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void defaultSetup() {
		setDriver(TestBench.createDriver(new FirefoxDriver()));
		getDriver().manage().window().setPosition(new Point(0, 0));
		getDriver().manage().window().setSize(new Dimension(1024, 768));
	}

	protected String url(String fragment) {
		return rootUrl() + "/#" + fragment;
	}

	protected String rootUrl() {
		String rootUrl = buildUrl(baseUrl, context);
		return rootUrl;
	}

	/**
	 * @deprecated Use buildUrl() instead
	 * 
	 * @see com.vaadin.testbench.TestBenchTestCase#concatUrl(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	protected String concatUrl(String baseUrl, String uri) {
		return super.concatUrl(baseUrl, uri);
	}

	protected String buildUrl(String... segments) {
		StringBuilder buf = new StringBuilder();
		boolean firstSegment = true;
		for (String segment : segments) {
			if (!firstSegment) {
				buf.append("/");
			} else {
				firstSegment = false;
			}
			buf.append(segment.replace("/", ""));
		}
		String result = buf.toString();
		// slashes will have been removed
		result = result.replace("http:", "http://");
		result = result.replace("https:", "https://");
		return result;
	}

	protected void verifyUrl(String fragment) {
		String expected = rootUrl() + "/#" + fragment;
		String actual = driver.getCurrentUrl();
		assertThat(actual).isEqualTo(expected);
	}

	protected void verifyNotUrl(String fragment) {
		String expected = rootUrl() + fragment;
		String actual = driver.getCurrentUrl();
		assertThat(actual).isNotEqualTo(expected);
	}

	protected UITree navTree() {
		return treeLocator().id("DefaultUserNavigationTree");
	}

	/**
	 * Returns the ElementLocator for the breadcrumb button at index
	 * 
	 * @param index
	 * @return
	 */
	protected ElementLocator breadcrumb(int index) {
		String idIndex = ID.getIdcIndex(index, DefaultBreadcrumb.class, NavigationButton.class);
		return locator().id(idIndex);
	}

	protected ElementLocator subpagepanel(int index) {
		String idIndex = ID.getIdcIndex(index, DefaultSubpagePanel.class, NavigationButton.class);
		return locator().id(idIndex);
	}

	// protected ElementLocator breadcrumbStep(int index) {
	// return new ElementLocator(driver).get(new ElementPath("breadcrumb/Slot[0]/VButton[0]/domChild[0]/domChild[0]"));
	// }

	protected String navTreeSelection() {
		try {
			String selectedNodeText = getDriver().findElement(
					By.xpath("id('DefaultUserNavigationTree')//div[contains(@class, 'v-tree-node-selected')]"))
					.getText();
			return selectedNodeText;
		} catch (Exception e) {
			return null;
		}
	}

	protected UITree treeLocator() {
		return new UITree(driver, context);
	}

	protected ElementLocator locator() {
		return new ElementLocator(driver, context);
	}

	protected void navigateTo(String fragment) {
		String url = url(fragment);
		driver.get(url);
		pause(500);
	}

	protected WebElement element(String qualifier, Class<?>... classes) {
		if (classes == null || classes.length == 0) {
			throw new RuntimeException("Id will fail with only a qualifier supplied.  Always use classes to define Id");
		}
		String s = id(qualifier, classes);
		WebElement findElement = driver.findElement(By.vaadin(s));
		return findElement;
	}

	protected WebElement element(Class<?>... classes) {
		return element(null, classes);
	}

	protected String id(Class<?>... components) {
		ElementPath elementPath = new ElementPath(context);
		ElementPath id = elementPath.id(ID.getIdc(components));
		return id.get();
	}

	protected String id(String qualifier, Class<?>... components) {
		ElementPath elementPath = new ElementPath(context);
		ElementPath id = elementPath.id(ID.getIdc(qualifier, components));
		return id.get();
	}

	protected void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			log.error("Sleep was interrupted");
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected void navigateForward() {
		driver.navigate().forward();
		pause(500);
	}

	protected void navigateBack() {
		driver.navigate().back();
		pause(500);
	}

	protected WebElement loginButton() {
		return element(DefaultLoginStatusPanel.class, Button.class);
	}

	protected WebElement notification() {
		WebElement notification = getDriver().findElement(By.className("v-Notification"));
		return notification;
	}

	// Does not work
	// protected WebElement waitForNotification(int limit) {
	// int attempts = 0;
	// boolean found = false;
	// while ((!found) && attempts < limit) {
	// try {
	// WebElement notification = getDriver().findElement(By.className("v-Notification"));
	// return notification;
	// } catch (NoSuchElementException nse) {
	// attempts++;
	// pause(1000);
	//
	// }
	//
	// }
	// throw new RuntimeException("Timed out - notification not found");
	//
	// }

	protected void closeNotification() {
		((TestBenchElementCommands) notification()).closeNotification();
	}

	public void fillLoginForm() {
		fillLoginForm("ds", "password");
	}

	public void fillLoginForm(String username, String password) {
		usernameBox().clear();
		usernameBox().sendKeys(username);
		passwordBox().clear();
		passwordBox().sendKeys(password);
		submitButton().click();
	}

	public void login() {
		loginButton().click();
		pause(100);
		fillLoginForm();
	}

	protected WebElement loginLabel() {
		return element(DefaultLoginStatusPanel.class, Label.class);
	}

	protected WebElement usernameBox() {
		return element("username", DefaultLoginView.class, TextField.class);
	}

	protected WebElement passwordBox() {
		return element("password", DefaultLoginView.class, PasswordField.class);
	}

	protected WebElement submitButton() {
		return element(DefaultLoginView.class, Button.class);
	}

	/**
	 * This does assume that you are already logged in!
	 */
	protected void logout() {
		loginButton().click();
	}

	protected String currentSelectionNavTree() {
		String sysaccount = "system-account";
		WebElement tree = navTree().getLocator().get();
		// WebElement e1 = tree.findElement(By.vaadin(sysaccount));
		// WebElement e2 = tree.findElement(By.id(sysaccount));
		// WebElement e3 = tree.findElement(By.linkText(sysaccount));
		// WebElement e4 = tree.findElement(By.name(sysaccount));
		WebElement e5 = tree.findElement(By.partialLinkText(sysaccount));
		return e5.getText();
	}
}
