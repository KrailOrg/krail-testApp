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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrivialUserAccount {

    private String password;
    private List<String> permissions;
    private Set<String> roles;
    private String userId;

    public TrivialUserAccount(String userId, String password, String role, String... permissions) {
        this.userId = userId;
        this.password = password;
        this.permissions = Arrays.asList(permissions);
        this.roles = new HashSet<>();
        roles.add(role);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public TrivialUserAccount addRole(String role) {
        roles.add(role);
        return this;
    }
}
