package it.esinware.mapping;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import ma.glasnost.orika.CustomMapper;

@Retention(RUNTIME)
@Target(TYPE)
public @interface TypeBinding {

	Class<?> binding();
	
	@SuppressWarnings("rawtypes")
	Class<? extends CustomMapper> customizer() default CustomMapper.class;
}