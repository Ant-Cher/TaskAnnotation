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
 * Позволяет контролировать количество
 * максимально выводимых объектов у полей
 * Iterable и Map
 */
public @interface MaxAmountOfDisplayedObjects {

    public int maxAmount();

}
