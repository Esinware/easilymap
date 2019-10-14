package it.esinware.mapping.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import it.esinware.mapping.orika.PlaceholderConverter;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.metadata.MappingDirection;

@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(Fields.class)
public @interface FieldBinding {
	
	String typeId() default "";

	String binding();

	Class<? extends Converter<?, ?>> converter() default PlaceholderConverter.class;
	
	MappingDirection direction() default MappingDirection.BIDIRECTIONAL;
}