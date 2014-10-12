package Annotations;

/**
 * Created by Aspera on 08.10.2014.
 */
import java.lang.annotation.*;

@Target(value=ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)

public @interface NeedNull {
}
