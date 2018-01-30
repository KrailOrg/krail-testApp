package uk.q3c.krail.functest.objects

import uk.q3c.krail.functest.TextField
import uk.q3c.krail.functest.ViewElement

/**
 * Created by David Sowerby on 30 Jan 2018
 */
class LoginViewObject(override val id: String) : ViewElement {
    val password by TextField()
}













