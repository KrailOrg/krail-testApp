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

package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.vaadin.ui.Button;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import uk.q3c.krail.core.user.notify.UserNotifier;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.ID;

import java.util.Optional;

/**
 * Used to check some Shiro annotations as well
 *
 * Created by David Sowerby on 22/05/15.
 */
@SuppressFBWarnings("LSC_LITERAL_STRING_COMPARISON")
public class AccountsView extends Grid3x3ViewBase {

    private UserNotifier userNotifier;

    @Inject
    protected AccountsView(UserNotifier userNotifier) {
        this.userNotifier = userNotifier;
    }

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);


        Button shiroPermissionsFailButton = new Button("shiro permissions fail");
        shiroPermissionsFailButton.addClickListener(event -> permissionCheckFail());
        shiroPermissionsFailButton.setId(ID.getId(Optional.of("permissions-fail"), this, shiroPermissionsFailButton));

        Button shiroPermissionsPassButton = new Button("shiro permissions pass");
        shiroPermissionsPassButton.addClickListener(event -> permissionCheckPass());
        shiroPermissionsPassButton.setId(ID.getId(Optional.of("permissions-pass"), this, shiroPermissionsPassButton));


        Button shiroRoleFailButton = new Button("shiro role fail");
        shiroRoleFailButton.addClickListener(event -> roleCheckFail());
        shiroRoleFailButton.setId(ID.getId(Optional.of("role-fail"), this, shiroRoleFailButton));

        Button shiroRolePassButton = new Button("shiro role pass");
        shiroRolePassButton.addClickListener(event -> roleCheckPass());
        shiroRolePassButton.setId(ID.getId(Optional.of("role-pass"), this, shiroRolePassButton));


        Button shiroAuthenticationFailButton = new Button("shiro authentication");
        shiroAuthenticationFailButton.addClickListener(event -> authenticationCheck());
        shiroAuthenticationFailButton.setId(ID.getId(Optional.of("authentication"), this, shiroAuthenticationFailButton));

        Button shiroGuestButton = new Button("shiro guest");
        shiroGuestButton.addClickListener(event -> guestCheck());
        shiroGuestButton.setId(ID.getId(Optional.of("guest"), this, shiroGuestButton));


        setMiddleCentre(shiroPermissionsFailButton);
        setTopCentre(shiroAuthenticationFailButton);
        setTopRight(shiroGuestButton);
        setTopLeft(shiroPermissionsPassButton);
        setBottomCentre(shiroRolePassButton);
        setBottomRight(shiroRoleFailButton);
    }

    @RequiresRoles("hero")
    protected void roleCheckPass() {
        userNotifier.notifyInformation(LabelKey.Yes);
    }

    @RequiresRoles("villain")
    protected void roleCheckFail() {
        userNotifier.notifyInformation(LabelKey.No);
    }

    @RequiresGuest
    protected void guestCheck() {
        userNotifier.notifyInformation(LabelKey.Guest);
    }

    @RequiresPermissions("counter:increment")
    protected void permissionCheckFail() {
        userNotifier.notifyWarning(LabelKey.Krail_Test_Application);
    }

    @RequiresAuthentication
    protected void authenticationCheck() {
        userNotifier.notifyInformation(LabelKey.Authenticated);
    }

    @RequiresPermissions("page:view:private")
    protected void permissionCheckPass() {
        userNotifier.notifyInformation(LabelKey.Yes);
    }


}
