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
 * Created by David Sowerby on 30/12/14.
 */
public abstract class JpaBaseDao<E extends JpaEntity> implements BaseDao<E> {

    protected Class<E> entityClass;
    private EntityManagerProvider entityManagerProvider;

    public JpaBaseDao(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }


    /**
     * Inserts a new record into the database
     * Attach the entity to the entity manager.
     *
     * @param entity
     */
    @Transactional
    @Override
    public void persist(E entity) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(entity);
    }

    /**
     * Find an attached entity with the same id and update it. If one exists update and return the already attached
     * entity. If one doesn't exist, inserts a new record into the database & attaches the entity to the entity
     * manager.
     *
     * @param entity
     */
    @Transactional
    @Override
    public void merge(E entity) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.merge(entity);
    }

    @Transactional
    @Override
    public List<E> loadAll() {
        EntityManager entityManager = entityManagerProvider.get();
        Query q = entityManager.createQuery("select t from " + tableName() + " t");
        List<E> todoList = q.getResultList();
        return todoList;
    }

    protected String tableName() {
        Metamodel meta = entityManagerProvider.get()
                                              .getMetamodel();
        EntityType<E> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null) ? entityType.getName() : t.name();
        return tableName;
    }

    @Override
    public void delete(E entity) {

    }

    @Override
    public E loadFromId(Object id) {
        return null;
    }
}
