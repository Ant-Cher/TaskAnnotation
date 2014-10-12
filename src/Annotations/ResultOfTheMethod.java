package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Aspera on 11.10.2014.
 */
@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)

/**
 * Если метод не принимает параметров, то эта аннотация
 * позволяет добавить в строку, выводимую методом mkString() класса TaskAnnotation,
 * результат вывода этого метода в виде:
 * имаМетода = результатМетода
 */
public @interface ResultOfTheMethod {

}
