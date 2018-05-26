package uk.q3c.krail.testapp.view;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import uk.q3c.krail.core.form.KrailBeanValidationBinder;
import uk.q3c.krail.core.form.KrailBeanValidationBinderFactory;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.AfterViewChangeBusMessage;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.testapp.i18n.Caption;
import uk.q3c.krail.testapp.i18n.DescriptionKey;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;


/**
 * Created by David Sowerby on 11 Feb 2018
 */
public class AutoForm extends ViewBase {

    private transient KrailBeanValidationBinderFactory binderFactory;

    @Caption(caption = LabelKey.title, description = DescriptionKey.title)
    private TextField titleField = new TextField();
    @Caption(caption = LabelKey.name, description = DescriptionKey.name)
    private TextField nameField = new TextField();
    @Caption(caption = LabelKey.age, description = DescriptionKey.age)
    private TextField ageField = new TextField();

    private FormLayout layout = new FormLayout();
    private Label validationMsg = new Label();
    private KrailBeanValidationBinder<Person> binder;

    @Inject
    protected AutoForm(Translate translate, SerializationSupport serialisationSupport, KrailBeanValidationBinderFactory binderFactory) {
        super(translate, serialisationSupport);
        this.binderFactory = binderFactory;
        serializationSupport.setExcludedFieldNames(ImmutableList.of("binder"));
    }

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        setRootComponent(layout);
        layout.addComponent(titleField);
        layout.addComponent(nameField);
        layout.addComponent(ageField);
        layout.addComponent(validationMsg);
    }

    /**
     * Binder may not be initialised - it certainly won't be after deserialisation
     */
    private void checkBinder() {
        if (binder == null) {
            binder = binderFactory.create(Person.class);
            bindFields();
        }

    }

    private void bindFields() {

        binder.bind(titleField, "title");
        binder.bind(nameField, "name");
        binder.forField(ageField).withConverter(new StringToIntegerConverter("Rubbish error message"))
                .bind("age");
    }

    @Override
    public void loadData(AfterViewChangeBusMessage busMessage) {
        checkBinder();
        binder.setBean(new Person("Dr", "Who", 12));
    }


}




















