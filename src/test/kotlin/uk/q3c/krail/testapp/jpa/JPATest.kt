package uk.q3c.krail.testapp.jpa

import com.codeborne.selenide.Condition.visible
import org.junit.Test
import uk.q3c.krail.testapp.selenide.*


/**
 * Created by David Sowerby on 18 Jan 2018
 */

class JPAPage(val page: Page = Page("jpa", "Table 1")) : PageObj by page {


    fun common1Button(): ButtonElement {
        return page.buttonById("#JpaView-Button-1")
    }

    fun table1(): GridElement {
        return page.gridById("#JpaView-Grid-1")
    }
}

class JPATest : SelenideTestCase() {


    @Test
    fun cb() {

        // given
        val jpaPage = JPAPage()

        // when
        jpaPage.open()

        // then
        jpaPage.common1Button().shouldBe(visible)
        jpaPage.table1().shouldBe(visible)

    }

}

