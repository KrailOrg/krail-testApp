package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.ui.InlineDateField
import uk.q3c.krail.core.form.FieldType
import uk.q3c.krail.core.form.FormConfiguration
import uk.q3c.krail.core.form.StyleBorderless
import uk.q3c.krail.core.form.StyleSize
import uk.q3c.krail.core.form.min
import uk.q3c.krail.i18n.Translate

/**
 * Created by David Sowerby on 03 Aug 2018
 */
class PersonFormConfiguration : FormConfiguration() {
//    override fun config() {
//        section("standard")
//                .entityClass(Person::class.java)
//                .columnOrder("title", "name", "age")
//                .sampleCaptionKey(PersonKey.Age)
//                .sampleDescriptionKey(PersonKey.Age)
//                .fieldOrder("title", "name", "age", "dob", "joinDate", "pricePlan", "roles")
//                .property("dob").caption(PersonKey.Date_of_Birth).description(PersonKey.Date_of_Birth).end()
//                .property("pricePlan").fieldType(FieldType.SINGLE_SELECT).selectDataProvider(PricePlanDataProvider::class.java).end()
//                .property("roles").fieldType(FieldType.MULTI_SELECT).selectDataProvider(RolesDataProvider::class.java)
//
//    }

    override fun config() {
        section("standard")
                .columnOrder("title", "name", "age")
                .excludedProperties("id")
                .entityClass(Person::class.java)
                .styleAttributes(borderless = StyleBorderless.yes)
                .sampleCaptionKey(PersonKey.Age)
                .sampleDescriptionKey(PersonKey.Age)
                .fieldOrder("title", "name", "age", "dob", "joinDate", "pricePlan", "roles")
                .property("title").styleAttributes(size = StyleSize.huge).end()
                .property("joinDate").componentClass(InlineDateField::class.java).caption(PersonKey.Join_Date).description(PersonKey.Join_Date).end()
                .property("age").min(3).end()
                .property("pricePlan").fieldType(FieldType.SINGLE_SELECT).selectDataProvider(PricePlanDataProvider::class.java).end()
                .property("roles").fieldType(FieldType.MULTI_SELECT).selectDataProvider(RolesDataProvider::class.java).end()
                .property("dob").caption(PersonKey.Date_of_Birth).description(PersonKey.Date_of_Birth).end()
    }
}


class PricePlanDataProvider @Inject constructor(val translate: Translate) : ListDataProvider<Int>(setOf(1, 3))
class RolesDataProvider @Inject constructor(val translate: Translate) : ListDataProvider<String>(setOf("admin", "manager"))

