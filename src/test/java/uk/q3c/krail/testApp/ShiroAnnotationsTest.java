/*
 * Copyright (c) 2015. David Sowerby
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package uk.q3c.krail.testApp;

import org.junit.Before;
import org.junit.Test;
import uk.q3c.krail.testapp.view.AccountsViewPageObject;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ShiroAnnotationsTest extends KrailTestBenchTestCase {


    AccountsViewPageObject accountsView = new AccountsViewPageObject(this);


    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
        startDriver();
    }

    @Test
    public void responses() {
        //given
        login();
        navigateTo("private/finance/accounts");
        accountsView.permissionsFailButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You do not have permission for that action");

        //        given
        notification().close();

        //when
        accountsView.permissionsPassButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("yes");

        //given
        closeNotification();

        //when
        accountsView.authenticationButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("Authenticated");

        //given
        closeNotification();

        //when
        accountsView.guestButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You will need to log out to do that");

        //given
        closeNotification();

        //when
        accountsView.rolePassButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("yes");

        //        given
        closeNotification();

        //when
        accountsView.roleFailButton()
                    .click();
        //then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You do not have permission for that action");
    }
}