### Release Notes for krail-testApp 0.13.0.0-v7compat+1

Tests for Krail 0.13.0.0-v7compat+1  These are using Vaadin 8 in compatibility mode, and all the components used are actually still at Vaadin 7

A couple of tests have been @Ignored, as they work manually - it is not considered worth fixing them, as this branch will not be developed any further

In order to get the widgetset to work successfully:
- all the Vaadin compatibility packages have been included in **compile** scope, though that may not be strictly necessary
- /home/david/git/krail-testapp/src/main/resources/uk/q3c/krail/testapp/widgetset/testAppWidgetset.gwt.xml inherits from "com.vaadin.v7.Vaadin7WidgetSet"
- The Gradle-Vaadin plugin has 'manage widgetset' set to false:
```
vaadinCompile{
    manageWidgetset false
}

```