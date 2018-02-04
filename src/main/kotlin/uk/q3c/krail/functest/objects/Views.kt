package uk.q3c.krail.functest.objects

import uk.q3c.krail.functest.Button
import uk.q3c.krail.functest.TextField
import uk.q3c.krail.functest.ViewObject

/**
 * Created by David Sowerby on 30 Jan 2018
 */
class LoginViewObject : ViewObject {

    val password by TextField()
    val username by TextField()
    val submit by Button()
}

















