package uk.q3c.krail.testApp.sel;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by David Sowerby on 03 Jan 2018
 */
public class GoogleResultsPage {
    public ElementsCollection results() {
        return $$("#rhscol");
    }
}
