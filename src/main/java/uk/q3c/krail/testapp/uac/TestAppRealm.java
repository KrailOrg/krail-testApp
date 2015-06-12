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

package uk.q3c.krail.testapp.uac;

import com.google.inject.Inject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;

public class TestAppRealm extends AuthorizingRealm {

    private TrivialCredentialsStore credentialsStore;

    @Inject
    public TestAppRealm() {
        super();
        this.credentialsStore = new TrivialCredentialsStore();
        setCachingEnabled(false);
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        TrivialUserAccount userAccount = credentialsStore.getAccount((String) token.getPrincipal());
        if (userAccount == null) {
            return null;
        }
        String tokenCredentials = new String((char[]) token.getCredentials());
        if (userAccount.getPassword()
                       .equals(tokenCredentials)) {
            return new SimpleAuthenticationInfo(userAccount.getUserId(), token.getCredentials(), "TutorialRealm");
        } else {
            return null;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        TrivialUserAccount userAccount = credentialsStore.getAccount((String) principals.getPrimaryPrincipal());
        if (userAccount != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setStringPermissions(new HashSet(userAccount.getPermissions()));
            info.setRoles(userAccount.getRoles());
            return info;
        }
        return null;
    }

}