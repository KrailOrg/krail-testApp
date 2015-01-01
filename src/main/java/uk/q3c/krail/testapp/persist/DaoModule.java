package uk.q3c.krail.testapp.persist;

import com.google.inject.AbstractModule;

/**
 * Created by David Sowerby on 01/01/15.
 */
public class DaoModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GenericJpaDao.class).to(DefaultGenericJpaDao.class);
    }
}
