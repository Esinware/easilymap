package it.esinware.mapping;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import ma.glasnost.orika.metadata.MappingDirection;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FieldBinding {

	String binding() default "";

	String converter() default "";
	
	MappingDirection direction() default MappingDirection.BIDIRECTIONAL;
}