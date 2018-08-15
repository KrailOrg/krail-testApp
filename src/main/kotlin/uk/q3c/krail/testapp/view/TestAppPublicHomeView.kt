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
package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.ui.Button
import com.vaadin.ui.VerticalLayout
import uk.q3c.krail.core.view.PublicHomeView
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.i18n.Translate
import uk.q3c.util.guice.SerializationSupport

class TestAppPublicHomeView @Inject
constructor(translate: Translate, serializationSupport: SerializationSupport) : ViewBase(translate, serializationSupport), PublicHomeView {
    val button = Button("Generate an exception")


    public override fun doBuild(busMessage: ViewChangeBusMessage) {

        button.addClickListener { _ -> throw IllegalStateException("Fake exception - generated for testing") }
        rootComponent = VerticalLayout(button)
    }
}
