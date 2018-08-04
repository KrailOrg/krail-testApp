package uk.q3c.krail.testapp.view

import uk.q3c.krail.core.form.Entity
import uk.q3c.krail.core.form.MultiSelectPropertyDelegate
import uk.q3c.krail.core.form.SingleSelectPropertyDelegate
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.Max

/**
 * Created by David Sowerby on 25 May 2018
 */
class Person(
        override var id: String = testUuid1,
        var title: String = "Mr",
        var name: String,
        @field:Max(12)
        var age: Int,
        var joinDate: LocalDate = LocalDate.parse("2010-12-31"),
        var dob: LocalDate = LocalDate.parse("1999-12-31")) : Serializable, Entity {

    var pricePlan: Int by SingleSelectPropertyDelegate<Person, Int>(setOf(1, 3))
    var roles: Set<String> by MultiSelectPropertyDelegate<Person, String>(setOf("admin", "manager"))

    init {
        pricePlan = 3
    }
}

const val testUuid1 = "123e4567-e89b-12d3-a456-556642440000"