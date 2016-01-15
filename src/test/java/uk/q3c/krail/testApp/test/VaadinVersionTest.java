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

package uk.q3c.krail.testApp.test;

import com.vaadin.testbench.util.VersionUtil;
import org.junit.Test;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simply checks that deployment has the correct version
 *
 * Created by david on 04/10/14.
 */
public class VaadinVersionTest extends KrailTestBenchTestCase {

    @Test
    public void confirmVersion() {
        //given
        appContext = "krail-testapp";
        startDriver();
        //when

        //then
        assertThat(VersionUtil.getVaadinMajorVersion(getDriver())).isEqualTo(7);
        assertThat(VersionUtil.getVaadinMinorVersion(getDriver())).isEqualTo(6);
        assertThat(VersionUtil.getVaadinRevision(getDriver())).isEqualTo(1);
    }
}
