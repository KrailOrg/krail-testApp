package uk.q3c.krail.testapp.persist;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Sowerby on 01/01/15.
 */
public class JpaConfig {

    private Map<String, Object> map = new HashMap<>();

    public JpaConfig() {
    }

    public JpaConfig dbDerby() {
        map.put(PersistenceUnitProperties.JDBC_DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
        return this;
    }

    //    public JpaConfig excludeUnlistedClasses(boolean exclude) {
    //        <exclude-unlisted-classes>false</exclude-unlisted-classes>
    //    }
}
