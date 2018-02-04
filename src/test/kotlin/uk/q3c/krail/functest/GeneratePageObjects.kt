package uk.q3c.krail.functest

import uk.q3c.krail.functest.coded.CodedBrowser

/**
 * Created by David Sowerby on 04 Feb 2018
 */
fun main(args: Array<String>) {
    val codedBrowser = CodedBrowser()
    codedBrowser.generatePageObjects()
}