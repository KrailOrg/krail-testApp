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
import uk.q3c.krail.core.shiro.SubjectProvider;
import uk.q3c.krail.core.user.notify.UserNotifier;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;

import java.util.Optional;

/**
 * Used to check some Shiro annotations as well
 * <p>
 * Created by David Sowerby on 22/05/15.
 */
@SuppressFBWarnings("LSC_LITERAL_STRING_COMPARISON")
public class AccountsView extends Grid3x3ViewBase {

    private final SubjectProvider subjectProvider;
    private UserNotifier userNotifier;

    @Inject
    protected AccountsView(UserNotifier userNotifier, Translate translate, SerializationSupport serializationSupport, SubjectProvider subjectProvider) {
        super(translate, serializationSupport);
        this.subjectProvider = subjectProvider;
        nameKey = LabelKey.Accounts;
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

    //    @RequiresRoles("hero")
    protected void roleCheckPass() {
        if (subjectProvider.get().hasRole("hero")) {
            userNotifier.notifyInformation(LabelKey.Yes);
        }
    }

    //    @RequiresRoles("villain")
    protected void roleCheckFail() {
        if (subjectProvider.get().hasRole("villain")) {
            userNotifier.notifyInformation(LabelKey.No);
        }
    }

    //    @RequiresGuest
    protected void guestCheck() {
        if (!(subjectProvider.get().isAuthenticated() || subjectProvider.get().isRemembered())) {
            userNotifier.notifyInformation(LabelKey.Guest);
        }
    }

    //    @RequiresPermissions("counter:increment")
    protected void permissionCheckFail() {
        if (subjectProvider.get().isPermitted("counter:increment")) {
            userNotifier.notifyWarning(LabelKey.Krail_Test);
        }
    }

    //    @RequiresAuthentication
    protected void authenticationCheck() {
        if (subjectProvider.get().isAuthenticated()) {
            userNotifier.notifyInformation(LabelKey.Authenticated);
        }
    }

    //    @RequiresPermissions("page:view:private")
    protected void permissionCheckPass() {
        if (subjectProvider.get().isPermitted("page:view:private")) {
            userNotifier.notifyInformation(LabelKey.Yes);
        }
    }


}
