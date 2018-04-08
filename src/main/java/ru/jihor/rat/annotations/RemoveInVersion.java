package ru.jihor.rat.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation denoting that this element should be removed in the specified version
 *
 * @author jihor (jihor@ya.ru)
 *         Created on 2018-04-06
 */
@Retention(RetentionPolicy.SOURCE)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR})
public @interface RemoveInVersion {

    /**
     * Version in X.Y.Z format
     */
    String version();

    /**
     * Reason to remove the element
     */
    String reason() default "";

}
