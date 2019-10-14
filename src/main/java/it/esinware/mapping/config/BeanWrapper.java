package it.esinware.mapping.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import it.esinware.mapping.annotation.FieldBinding;
import it.esinware.mapping.annotation.FieldOverride;
import it.esinware.mapping.annotation.Fields;
import it.esinware.mapping.annotation.TypeBinding;
import ma.glasnost.orika.CustomMapper;

public class BeanWrapper {

	private String typeId;
	private Class<? extends CustomMapper<?, ?>> customizer;
	private Map<String, FieldBinding> fieldOverrides;
	private List<String> fieldsExcluded;
	private Map<String, FieldBinding> fieldBindings;
	private Map<String, Field> fields;
	private Class<?> sourceClass;
	private Class<?> targetClass;

	public BeanWrapper(Class<?> sourceClass, TypeBinding params) {
		this.sourceClass = sourceClass;
		bind(params);
		fields = new HashMap<>();
		discoverFields();
	}

	private void bind(TypeBinding binding) {
		typeId = binding.typeId();
		targetClass = binding.binding();
		customizer = binding.customizer();
		fieldsExcluded = Arrays.asList(binding.excludes());
		FieldOverride[] overrides = binding.overrides();
		fieldOverrides = new HashMap<>();
		for(FieldOverride override : overrides)
			fieldOverrides.put(override.override(), override.origin());
	}

	private void discoverFields() {
		fieldBindings = new HashMap<>();
		recurseFields(sourceClass);
		fields.values().forEach(field -> {
			FieldBinding binding = findBinding(field);
			if(binding != null && !fieldOverrides.containsKey(field.getName()) && typeId.equals(binding.typeId())) {
				fieldBindings.put(field.getName(), binding);
			} else if(fieldOverrides.containsKey(field.getName())) {
				fieldBindings.put(field.getName(), fieldOverrides.get(field.getName()));
			}
		});
	}

	private void recurseFields(Class<?> currentClass) {
		if(currentClass.getSuperclass() != null)
			recurseFields(currentClass.getSuperclass());
		for(Field field : currentClass.getDeclaredFields()) {
			if(!fieldsExcluded.contains(field.getName())) {
				fields.put(field.getName(), field);
			}
		}
	}
	
	private FieldBinding findBinding(Field field) {
		FieldBinding binding = field.getAnnotation(FieldBinding.class);
		if(binding == null) {
			Fields fields = field.getAnnotation(Fields.class);
			if(fields != null) {
				for(FieldBinding bind : fields.value()) {
					if(typeId.equals(bind.typeId())) {
						binding = bind;
					}
				}
			}
		}
		return binding;
	}

	public Class<? extends CustomMapper<?, ?>> getCustomizer() {
		return customizer;
	}

	public List<String> getFieldsExcluded() {
		return fieldsExcluded;
	}

	public Map<String, FieldBinding> getFieldBindings() {
		return fieldBindings;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}
}