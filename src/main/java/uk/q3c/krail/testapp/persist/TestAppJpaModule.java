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

package uk.q3c.krail.testapp.persist;

import org.junit.rules.TemporaryFolder;
import uk.q3c.krail.persist.jpa.DefaultJpaInstanceConfiguration;
import uk.q3c.krail.persist.jpa.JpaDb;
import uk.q3c.krail.persist.jpa.JpaModule;

import java.io.File;
import java.io.IOException;

/**
 * Created by David Sowerby on 03/01/15.
 */
public class TestAppJpaModule extends JpaModule {


    private final TemporaryFolder tempFolder;

    public TestAppJpaModule() {
        tempFolder = new TemporaryFolder();
        try {
            tempFolder.create();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp folder in TestAppJpaUnitModule", e);
        }
    }

    /**
     * Configures the persistence units over the exposed methods.
     */
    @Override
    protected void define() {
        addPersistenceUnit("derbyDb", Jpa1.class, derbyConfig());
        addPersistenceUnit("hsqlDb", Jpa2.class, hsqlConfig());
    }

    private DefaultJpaInstanceConfiguration derbyConfig() {
        DefaultJpaInstanceConfiguration config = new DefaultJpaInstanceConfiguration();
        File dbFolder = new File(tempFolder.getRoot(), "derbyDb");

        config.transactionType(DefaultJpaInstanceConfiguration.TransactionType.RESOURCE_LOCAL)
              .db(JpaDb.DERBY_EMBEDDED)
              .autoCreate(true)
              .url(dbFolder.getAbsolutePath())
              .user("test")
              .password("test")
              .ddlGeneration(DefaultJpaInstanceConfiguration.Ddl.DROP_AND_CREATE);
        return config;
    }

    private DefaultJpaInstanceConfiguration hsqlConfig() {
        DefaultJpaInstanceConfiguration config = new DefaultJpaInstanceConfiguration();
        config.db(JpaDb.HSQLDB)
              .autoCreate(true)
              .url("mem:test")
              .user("sa")
              .password("")
              .ddlGeneration(DefaultJpaInstanceConfiguration.Ddl.DROP_AND_CREATE);
        return config;
    }

}