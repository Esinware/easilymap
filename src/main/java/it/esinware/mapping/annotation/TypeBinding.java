package it.esinware.mapping.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import it.esinware.mapping.orika.PlaceholderMapper;
import ma.glasnost.orika.CustomMapper;

@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(Mapping.class)
public @interface TypeBinding {
	
	String typeId() default "";

	Class<?> binding();
	
	Class<? extends CustomMapper<?, ?>> customizer() default PlaceholderMapper.class;
	
	FieldOverride[] overrides() default {};
	
	String[] excludes() default {};
}