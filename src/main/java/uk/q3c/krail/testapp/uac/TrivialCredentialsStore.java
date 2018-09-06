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

package uk.q3c.krail.testapp.uac;

import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public class TrivialCredentialsStore {
    private Map<String, TrivialUserAccount> store = new HashMap<>();

    @Inject
    protected TrivialCredentialsStore() {
        addAccount("eq", "eq", "hero", "page:view:p:finance:accounts:*", "option:edit:SimpleUserHierarchy:eq:0:*:*", "pay:request-increase");
        addAccount("fb", "fb", "villain", "page:view:p:finance:*", "option:edit:SimpleUserHierarchy:fb:0:*:*");
        addAccount("ds", "password", "hero", "page:view:p:finance:*", "option:edit:SimpleUserHierarchy:ds:0:*:*");
        addAccount("admin", "password", "superhero", "page:view:*", "option:edit:*");
    }

    public final TrivialCredentialsStore addAccount(String userId, String password, String role, String... permissions) {
        store.put(userId, new TrivialUserAccount(userId, password, role, permissions));
        return this;
    }


    public TrivialUserAccount getAccount(String principal) {
        return store.get(principal);
    }
}
