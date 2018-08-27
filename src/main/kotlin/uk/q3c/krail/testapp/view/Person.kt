package uk.q3c.krail.testapp.view

import org.dizitart.no2.objects.Id
import uk.q3c.krail.core.form.Entity
import uk.q3c.krail.i18n.I18NKey
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.Max

/**
 * Created by David Sowerby on 25 May 2018
 */
data class Person(
        @field:Id override var id: String = testUuid1,
        var title: String = "Mr",
        var name: String,
        @field:Max(12) var age: Int,
        var joinDate: LocalDate = LocalDate.parse("2010-12-31"),
        var dob: LocalDate = LocalDate.parse("1999-12-31"),
        var pricePlan: Int = 3,
        var roles: Set<String> = mutableSetOf()) : Serializable, Entity

const val testUuid1 = "123e4567-e89b-12d3-a456-556642440000"

enum class PersonKey : I18NKey {
    id, Title, Name, Age, Join_Date, Date_of_Birth, Price_Plan, Roles
}