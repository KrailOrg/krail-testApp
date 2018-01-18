package uk.q3c.krail.testApp.sel;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

/**
 * Created by David Sowerby on 03 Jan 2018
 */
public class GooglePageObject {
    public GoogleResultsPage search(String query) {
        $(By.name("q")).setValue(query).pressEnter();
        return page(GoogleResultsPage.class);
    }
}


