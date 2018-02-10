package uk.q3c.krail.testapp

import org.junit.BeforeClass
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode

/**
 * Created by David Sowerby on 10 Feb 2018
 */
abstract class BrowserTestCase {
    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {

            executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
            createBrowser()

        }
    }
}