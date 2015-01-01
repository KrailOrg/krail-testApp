package uk.q3c.krail.testapp.persist;

import org.apache.onami.persist.EntityManagerProvider;
import org.apache.onami.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

/**
 * A collection of common persistence actions for JPA data sources.  Note that this class must be injected for
 * @Transactional to take effect (it is using Guice AOP) and you must call {@link #setEntityManagerProvider
 * (EntityManagerProvider)} with your chosen EntityManagerProvider before calling any other methods.
 *
 * This is necessary to allow Krail to support multiple JPA persistence units but still provide a generic JPA DAO
 *
 * <p>
 * Created by David Sowerby on 30/12/14.
 */
public class DefaultGenericJpaDao implements GenericJpaDao {

    private EntityManagerProvider entityManagerProvider;

    public DefaultGenericJpaDao() {
    }

    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }

    public void setEntityManagerProvider(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * Inserts a new record into the database and attaches the entity to the entityManager.
     *
     * @param entity
     */
    @Override
    @Transactional
    public <E extends JpaEntity> void persist(E entity) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(entity);
    }

    /**
     * Find an attached entity with the same id and update it. If one exists update and return the already attached
     * entity. If one doesn't exist, inserts a new record into the database & attaches the entity to the entityManager.
     *
     * @param entity
     */
    @Override
    @Transactional
    public <E extends JpaEntity> void merge(E entity) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.merge(entity);
    }

    @Override
    @Transactional
    public <E extends JpaEntity> List<E> loadAll(Class<E> entityClass) {
        EntityManager entityManager = entityManagerProvider.get();
        Query q = entityManager.createQuery("select t from " + tableName(entityClass) + " t");
        return q.getResultList();
    }

    protected <E extends JpaEntity> String tableName(Class<E> entityClass) {
        Metamodel meta = entityManagerProvider.get()
                                              .getMetamodel();
        EntityType<E> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        //If no Table annotation use the default (simple class name)
        String tableName = (t == null) ? entityType.getName() : t.name();
        return tableName;
    }

    @Override
    public <E extends JpaEntity> void delete(E entity) {

    }

    @Override
    public <E extends JpaEntity> E loadFromId(Class<E> entityClass, Object id) {
        return null;
    }
}
