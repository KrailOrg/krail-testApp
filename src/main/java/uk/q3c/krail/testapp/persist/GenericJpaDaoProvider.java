package uk.q3c.krail.testapp.persist;

import java.lang.annotation.Annotation;

/**
 * Created by David Sowerby on 03/01/15.
 */
public interface GenericJpaDaoProvider {
    GenericJpaDao getDao(Class<? extends Annotation> annotatedBy);
}
