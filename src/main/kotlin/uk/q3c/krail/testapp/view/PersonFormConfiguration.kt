package uk.q3c.krail.testapp.view

import uk.q3c.krail.core.form.FormConfiguration

/**
 * Created by David Sowerby on 03 Aug 2018
 */
class PersonFormConfiguration : FormConfiguration() {
    override fun config() {
        section("standard")
                .entityClass(Person::class.java)
                .columnOrder("title", "name", "age")
                .sampleCaptionKey(PersonKey.Age)
                .sampleDescriptionKey(PersonKey.Age)
                .fieldOrder("title", "name", "age", "dob", "joinDate", "pricePlan", "roles")
                .property("dob").caption(PersonKey.Date_of_Birth).description(PersonKey.Date_of_Birth)
    }
}


