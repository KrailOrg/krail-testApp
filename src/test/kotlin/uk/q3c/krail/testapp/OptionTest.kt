package uk.q3c.krail.testapp

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode

/**
 * Created by David Sowerby on 10 Feb 2018
 */
class OptionTest : Spek({
    given("Browser selection") {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED

        createBrowser()

        on("") {


            it("?") {


            }
        }
    }
})