package uk.q3c.krail.testapp.persist;

import org.apache.onami.persist.PersistenceModule;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

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
        //        prepFactory();
        this.bindApplicationManagedPersistenceUnit("derbyDb")
            .annotatedWith(Jpa1.class);
        this.bindApplicationManagedPersistenceUnit("hsqlDb")
            .annotatedWith(Jpa2.class);

        //        this.bindContainerManagedPersistenceUnit(derbyEntityManagerFactory).annotatedWith(Jpa1.class);
        //        this.bindContainerManagedPersistenceUnit(hsqlEntityManagerFactory).annotatedWith(Jpa2.class);
    }

    private void prepFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.provider", PersistenceProvider.class);
        properties.put(PersistenceUnitProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
        //        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:derby:/home/david/temp/derbyDb;create=true");
        properties.put(PersistenceUnitProperties.JDBC_USER, "test");
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "test");
        properties.put(PersistenceUnitProperties.DDL_GENERATION, "drop-and-create-tables");



        derbyEntityManagerFactory = Persistence.createEntityManagerFactory("derbyDb", properties);
        properties = new HashMap<>();
        properties.put("javax.persistence.provider", PersistenceProvider.class);
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "org.hsqldb.jdbcDriver");
        hsqlEntityManagerFactory = Persistence.createEntityManagerFactory("hsqlDb", properties);
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
