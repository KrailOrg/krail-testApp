package uk.q3c.krail;

import com.google.inject.Inject;
import uk.q3c.krail.eventbus.MessageBus;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.service.AbstractService;
import uk.q3c.krail.service.RelatedServiceExecutor;
import uk.q3c.krail.service.ServiceKey;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by David Sowerby on 01 Apr 2018
 */
public class TestServiceSerial extends AbstractService {

    @Inject
    protected TestServiceSerial(Translate translate, MessageBus messageBus, RelatedServiceExecutor servicesExecutor, SerializationSupport serializationSupport) {
        super(translate, messageBus, servicesExecutor, serializationSupport);
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    public ServiceKey getServiceKey() {
        return new ServiceKey(LabelKey.age);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
