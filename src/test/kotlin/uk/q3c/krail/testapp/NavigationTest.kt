package uk.q3c.krail.testapp

import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.view.component.UserNavigationMenu
import uk.q3c.krail.core.view.component.UserNavigationTree

/**
 *
 * From code tests (non-UI tests) we know that [UserNavigationTree] and [UserNavigationMenu] send the right message to the [Navigator].
 * We also know that when the url changes, the  [UserNavigationTree] will change its selection to match, and will expand as necessary
 *
 * We want avoid using Selenide with complex UI objects, and we know Vaadin works, so:
 *
 * These are the features to test:
 *
 * 1. One test each of navigating from Tree and Menu - both can have references included in a view and 'clicked' programmatically, and check URL
 * 1. Navigate to a page which has a reference to Tree, ensure the Tree has the correct selected value.  Everything else should be code tested
 * 1. Browser back - forward
 *
 * Created by David Sowerby on 19 Jan 2018
 */
class NavigationTest
