package uk.q3c.krail.testapp.view;

import uk.q3c.krail.core.form.FormConfiguration;

/**
 * Created by David Sowerby on 03 Aug 2018
 */
public class PersonFormConfiguration extends FormConfiguration {
    @Override
    public void config() {
        section("standard").entityClass(Person.class);
    }
}
