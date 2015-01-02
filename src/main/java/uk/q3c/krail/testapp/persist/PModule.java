package uk.q3c.krail.testapp.persist;

import org.apache.onami.persist.PersistenceModule;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by David Sowerby on 30/12/14.
 */
public class PModule extends PersistenceModule {

    private EntityManagerFactory derbyEntityManagerFactory;

    private EntityManagerFactory hsqlEntityManagerFactory;

    /**
     * Configures the persistence units over the exposed methods.
     */
    @Override
    protected void configurePersistence() {
        prepFactory();
        //        this.bindApplicationManagedPersistenceUnit("derbyDb")
        //            .annotatedWith(Jpa1.class);
        //        this.bindApplicationManagedPersistenceUnit("hsqlDb")
        //            .annotatedWith(Jpa2.class);

        this.bindContainerManagedPersistenceUnit(derbyEntityManagerFactory)
            .annotatedWith(Jpa1.class);
        this.bindContainerManagedPersistenceUnit(hsqlEntityManagerFactory)
            .annotatedWith(Jpa2.class);
    }

    private void prepFactory() {
        JpaConfig config = new JpaConfig();

        config.transactionType(JpaConfig.TransactionType.RESOURCE_LOCAL)
              .db(JpaConfig.Db.DERBY_EMBEDDED)
              .url("jdbc:derby:/home/david/temp/derbyDb", true)
              .user("test")
              .password("test")
              .ddlGeneration(JpaConfig.Ddl.DROP_AND_CREATE);
        derbyEntityManagerFactory = Persistence.createEntityManagerFactory("derbyDb", config);


        config = new JpaConfig();
        config.db(JpaConfig.Db.HSQLDB)
              .url("jdbc:hsqldb:mem:test", true)
              .user("sa")
              .password("")
              .ddlGeneration(JpaConfig.Ddl.DROP_AND_CREATE);
        hsqlEntityManagerFactory = Persistence.createEntityManagerFactory("hsqlDb", config);
    }


    // Configure provider to be EclipseLink.
    //    String providerClass = "org.eclipse.persistence.jpa.PersistenceProvider";
    //    PersistenceProvider provider = null;
    //    try {
    //        provider = (PersistenceProvider)Class.forName(providerClass).newInstance();
    //    } catch (Exception error) {
    //        throw new TestProblemException("Failed to create persistence provider.", error);
    //    }
    //    Map properties = getPersistenceProperties();
    //    getExecutor().setEntityManagerFactory(provider.createEntityManagerFactory("performance", properties));

}
