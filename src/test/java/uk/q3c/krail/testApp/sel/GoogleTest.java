package uk.q3c.krail.testApp.sel;

import org.junit.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by David Sowerby on 03 Jan 2018
 */
public class GoogleTest {


    String menuCaption = "#DefaultUserNavigationMenu > span:nth-child(2) > span";
    String treeItem = "#gwt-uid-118";

    @Test
    public void testIt() throws InterruptedException {
        System.setProperty("selenide.browser", "chrome");
//        open("https://www.google.co.uk/");

        open("http://localhost:8080/krail-testapp/#notifications");
        open("http://localhost:8080/krail-testapp/#notifications");
        Thread.sleep(20000);
        $("#NotificationsView-Button-error").click();
        $(byText("You cannot use service Fake Service until it has been started")).shouldBe(visible);
        $(byClassName("v-Notification")).shouldBe(visible);
//        sleep(8000);

//    GooglePageObject searchPage = open("https://www.google.co.uk/", GooglePageObject.class);
//    GoogleResultsPage resultsPage = searchPage.search("selenide");
//    resultsPage.results().shouldHave(size(10));
//    resultsPage.results().get(0).shouldHave(text("Selenide: Concise UI Tests in Java"));
//        <h1 class="v-Notification-caption">You cannot use service Fake Service until it has been started</h1>
    }
}
