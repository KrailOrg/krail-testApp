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

package uk.q3c.krail.testbench.page.object;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.q3c.krail.core.view.DefaultLoginView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import java.util.Optional;


/**
 * Created by david on 03/10/14.
 */
@SuppressFBWarnings("CD_CIRCULAR_DEPENDENCY")
public class LoginFormPageObject extends PageObject {


    private Credentials credentials = new Credentials("ds", "password");

    /**
     * Initialises the PageObject with a reference to the parent test case, so that the PageObject can access a number
     * of variables from the parent, for example: drivers, baseUrl, application appContext.
     *
     * @param parentCase
     */
    public LoginFormPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the credentials you want to use to log in.  If not set, the default is used when you call {@link #login()}.
     * Alternatively you can set the username and password independently and then click the submit button.
     *
     * @param credentials
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setCredentials(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    /**
     * Log in using {@link #credentials}
     */
    public void login() {
        login(credentials.username, credentials.password, false);
    }

    public void loginWithEnterKey() {
        login(credentials.username, credentials.password, true);
    }

    public void login(String username, String password) {
        login(username, password, false);
    }

    public void login(String username, String password, boolean useEnterKey) {
        parentCase.waitForUrl("login");
        TextFieldElement usernameBox = usernameBox();
        PasswordFieldElement passwordBox = passwordBox();

        while (!usernameBox.getValue()
                .isEmpty()) {
            usernameBox.clear();
            System.out.println("cleared username box");
        }

        while (!usernameBox().getValue()
                .equals(username)) {
            usernameBox.sendKeys(username);
            System.out.println("user name set to " + username);
        }


        passwordBox.clear();
        pause(100);
        passwordBox.sendKeys(password);
        while (passwordBox.getValue()
                .isEmpty()) {
            System.out.println("waiting for password box");
        }

        if (useEnterKey) {
            passwordBox.sendKeys("\n");
        } else {
            submitButton().click();
        }
        pause(100);

    }

    public TextFieldElement usernameBox() {
        return element(TextFieldElement.class, Optional.of("username"), DefaultLoginView.class, TextField.class);
    }

    public PasswordFieldElement passwordBox() {
        return element(PasswordFieldElement.class, Optional.of("password"), DefaultLoginView.class,
                PasswordField.class);
    }

    public ButtonElement submitButton() {
        return element(ButtonElement.class, Optional.empty(), DefaultLoginView.class, Button.class);
    }

    public String message() {
        return messageLabel().getText();
    }

    public LabelElement messageLabel() {
        return element(LabelElement.class, Optional.of("status"), DefaultLoginView.class, Label.class);
    }


    public static class Credentials {
        private String password;
        private String username;

        public Credentials(String username, String password) {
            this.password = password;
            this.username = username;
        }
    }


}
