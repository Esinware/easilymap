package it.esinware.mapping.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import it.esinware.mapping.customize.MapplyFieldConverter;
import it.esinware.mapping.customize.ConverterPlaceholder;
import ma.glasnost.orika.metadata.MappingDirection;

@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(Fields.class)
public @interface FieldBinding {
	
	String typeId() default "";

	String binding();

	Class<? extends MapplyFieldConverter<?, ?>> converter() default ConverterPlaceholder.class;
	
	MappingDirection direction() default MappingDirection.BIDIRECTIONAL;
}