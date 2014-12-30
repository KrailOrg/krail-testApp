package uk.q3c.krail.testapp.persist;

import org.apache.onami.persist.PersistenceModule;

/**
 * Created by David Sowerby on 30/12/14.
 */
public class PModule extends PersistenceModule {

    //    private EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("todos");

    /**
     * Configures the persistence units over the exposed methods.
     */
    @Override
    protected void configurePersistence() {
        this.bindApplicationManagedPersistenceUnit("todos")
            .annotatedWith(Jpa1.class);
        this.bindApplicationManagedPersistenceUnit("widgets")
            .annotatedWith(Jpa2.class);
    }


}
