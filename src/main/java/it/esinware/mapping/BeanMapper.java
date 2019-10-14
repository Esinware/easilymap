package it.esinware.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.esinware.mapping.annotation.FieldBinding;
import it.esinware.mapping.config.BeanWrapper;
import it.esinware.mapping.config.MappingResolver;
import it.esinware.mapping.orika.PlaceholderConverter;
import it.esinware.mapping.orika.PlaceholderMapper;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;

public class BeanMapper {

	private static Logger logger = LoggerFactory.getLogger(BeanMapper.class);
	private MapperFactory factory;
	private MappingResolver resolver;
	
	public BeanMapper() {
		factory = new DefaultMapperFactory.Builder().build();
		factory.registerConcreteType(SortedSet.class, TreeSet.class);
		factory.registerConcreteType(SortedMap.class, TreeMap.class);
		resolver = new MappingResolver();
		configure(resolver.getConverters());
		config(resolver.getBeans());
	}
	
	private void configure(Map<Class<?>, String> converters) {
		converters.keySet().forEach(converter -> {
			try {
				factory.getConverterFactory().registerConverter(converters.get(converter), (Converter<?, ?>)converter.newInstance());
			} catch(InstantiationException | IllegalAccessException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		});
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void config(List<BeanWrapper> beans) {
		beans.forEach(bean -> {
			ClassMapBuilder<?, ?> builder = factory.classMap(bean.getSourceClass(), bean.getTargetClass());
			if(!bean.getCustomizer().equals(PlaceholderMapper.class)) {
				try {
					builder.customize((Mapper)bean.getCustomizer().newInstance());
				} catch(InstantiationException | IllegalAccessException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
			Map<String, FieldBinding> bindings = bean.getFieldBindings();
			bindings.keySet().forEach(bindingName -> {
				FieldBinding binding = bindings.get(bindingName);
				FieldMapBuilder<?, ?> fieldBuilder = builder.fieldMap(bindingName, binding.binding());
				switch(binding.direction()) {
					case A_TO_B:
						fieldBuilder.aToB();
						break;
					case B_TO_A:
						fieldBuilder.bToA();
						break;
					case BIDIRECTIONAL:
						//Nothing to do
				}
				if(!binding.converter().equals(PlaceholderConverter.class))
					fieldBuilder.converter(resolver.getConverters().get(binding.converter()));
				fieldBuilder.add();
			});
			bean.getFieldsExcluded().forEach(builder::exclude);
			builder.byDefault().register();
		});
	}
	
	public <S, D> void map(S source, D destination) {
		factory.getMapperFacade().map(source, destination);
	}

	public <S, D> D map(S source, Class<D> destination) {
		return factory.getMapperFacade().map(source, destination);
	}
	
	public <S, D> List<D> map(List<S> sources, Class<S> source, Class<D> destination) {
		List<D> result = new ArrayList<>();
		for(S s : sources)
			result.add(this.map(s, destination));
		return result;
	}
}