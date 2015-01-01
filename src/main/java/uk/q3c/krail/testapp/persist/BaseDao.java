package uk.q3c.krail.testapp.persist;

import java.util.List;

/**
 * More here https://code.google.com/p/generic-dao/source/browse/trunk/generic-dao/src/main/java/com/szczytowski
 * /genericdao/api/IDao.java
 * Created by David Sowerby on 30/12/14.
 */
public interface BaseDao<E extends Entity> {

    /**
     * Inserts a new record into the database
     * Attach the entity to the entity manager.
     */

    void persist(E entity);

    /**
     * Find an attached entity with the same id and update it. If one exists update and return the already attached
     * entity. If one doesn't exist, inserts a new record into the database & attaches the entity to the entity
     * manager.
     *
     * @param entity
     */

    void merge(E entity);

    List<E> loadAll();

    void delete(E entity);

    E loadFromId(Object id);
}
