package it.esinware.mapping.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import it.esinware.mapping.customize.FactoryPlaceholder;
import it.esinware.mapping.customize.MapplyInstanceFactory;
import it.esinware.mapping.customize.MapplyTypeMapper;
import it.esinware.mapping.customize.MapperPlaceholder;

@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(Mapply.class)
public @interface TypeBinding {
	
	String typeId() default "";

	Class<?> binding();
	
	Class<? extends MapplyInstanceFactory<?>> factory() default FactoryPlaceholder.class; 
	
	Class<? extends MapplyTypeMapper<?, ?>> customizer() default MapperPlaceholder.class;
	
	FieldOverride[] overrides() default {};
	
	String[] excludes() default {};
}