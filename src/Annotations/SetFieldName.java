package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Aspera on 11.10.2014.
 */

@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)

/**
 * Позволяет менять имя отображения поля
 * при выводе строки методом mkString() класса TaskAnnotation
 */
public @interface SetFieldName {

    public String name();

}
