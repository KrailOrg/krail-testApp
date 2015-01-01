package uk.q3c.krail.testapp.persist;

import org.apache.onami.persist.Transactional;

import java.util.List;

/**
 * Created by David Sowerby on 01/01/15.
 */
public interface GenericJpaDao {
    @Transactional
    <E extends JpaEntity> void persist(E entity);

    @Transactional
    <E extends JpaEntity> void merge(E entity);

    @Transactional
    <E extends JpaEntity> List<E> loadAll(Class<E> entityClass);

    <E extends JpaEntity> void delete(E entity);

    <E extends JpaEntity> E loadFromId(Class<E> entityClass, Object id);
}
