package uk.q3c.krail.testapp.persist;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by David Sowerby on 29/12/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface Jpa2 {
}
