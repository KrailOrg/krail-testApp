package uk.co.q3c.v7.testapp;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.co.q3c.v7.base.guice.uiscope.UIKeyProvider;
import uk.co.q3c.v7.base.ui.ScopedUIProvider;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.ui.UI;

public class TestAppUIProvider extends ScopedUIProvider {

	@Inject
	protected TestAppUIProvider(Map<String, Provider<UI>> uiProMap, UIKeyProvider uiKeyProvider) {
		super(uiProMap, uiKeyProvider);
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return TestAppUI.class;
	}

}
