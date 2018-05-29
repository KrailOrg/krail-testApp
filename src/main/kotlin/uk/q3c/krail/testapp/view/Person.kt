package uk.q3c.krail.testapp.view

import java.io.Serializable
import javax.validation.constraints.Max

/**
 * Created by David Sowerby on 25 May 2018
 */
class Person(var title: String, var name: String, @field:Max(12) var age: Int) : Serializable
