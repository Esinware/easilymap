package it.esinware.mapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.burningwave.core.assembler.StaticComponentContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.esinware.mapping.annotation.FieldBinding;
import it.esinware.mapping.config.BeanWrapper;
import it.esinware.mapping.config.MappingResolver;
import it.esinware.mapping.customize.ConverterPlaceholder;
import it.esinware.mapping.customize.FactoryPlaceholder;
import it.esinware.mapping.customize.MapperPlaceholder;
import it.esinware.mapping.customize.MapplyFieldBidiConverter;
import it.esinware.mapping.customize.MapplyFieldConverter;
import it.esinware.mapping.customize.MapplyTypeMapper;
import it.esinware.mapping.exception.UnrecognizedConverterException;
import it.esinware.mapping.orika.BidiFieldConverter;
import it.esinware.mapping.orika.InstanceFactory;
import it.esinware.mapping.orika.SimpleFieldConverter;
import it.esinware.mapping.orika.TypeMapper;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;
import ma.glasnost.orika.metadata.TypeFactory;

public class BeanMapper {

	private static Logger logger = LoggerFactory.getLogger(BeanMapper.class);
	private MapperFactory factory;
	private MappingResolver resolver;
	
	public BeanMapper() {
		StaticComponentContainer.Modules.exportAllToAll();
		factory = new DefaultMapperFactory.Builder().build();
		factory.registerConcreteType(SortedSet.class, TreeSet.class);
		factory.registerConcreteType(SortedMap.class, TreeMap.class);
		resolver = new MappingResolver();
		configure(resolver.getConverters());
		config(resolver.getBeans());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void configure(Map<Class<? extends MapplyFieldConverter<?, ?>>, String> converters) {
		converters.keySet().forEach(converter -> {
			try {
				Converter converterImpl = null;
				if(MapplyFieldBidiConverter.class.isAssignableFrom(converter))
					converterImpl = new BidiFieldConverter((MapplyFieldBidiConverter)converter.getConstructor().newInstance(), this);
				else if(MapplyFieldConverter.class.isAssignableFrom(converter))
					converterImpl = new SimpleFieldConverter(converter.getConstructor().newInstance(), this);
				if(converterImpl == null)
					throw new UnrecognizedConverterException();
				factory.getConverterFactory().registerConverter(converters.get(converter), new SimpleFieldConverter(converter.getConstructor().newInstance(), this) /*(Converter<?, ?>)converter.newInstance()*/);
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void config(List<BeanWrapper> beans) {
		beans.forEach(bean -> {
			ClassMapBuilder<?, ?> builder = factory.classMap(bean.getSourceClass(), bean.getTargetClass());
			if(!bean.getCustomizer().equals(MapperPlaceholder.class)) {
				try {
					Class<? extends MapplyTypeMapper<?, ?>> map = bean.getCustomizer();
//					builder.customize((Mapper)bean.getCustomizer().newInstance());
					builder.customize(new TypeMapper(map.getDeclaredConstructor().newInstance(), this));
				} catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
			if(!bean.getFactory().equals(FactoryPlaceholder.class)) {
				try {
					ObjectFactory instanceFactory = new InstanceFactory<>(bean.getFactory().getConstructor().newInstance());
					factory.registerObjectFactory(instanceFactory, TypeFactory.valueOf(bean.getSourceClass()));
				} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
				if(!binding.converter().equals(ConverterPlaceholder.class))
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
	
	public <S, D> Collection<D> map(Collection<S> sources, Class<D> destination) {
		if(sources instanceof List)
			return factory.getMapperFacade().mapAsList(sources, destination);
		else if(sources instanceof Set) {
			return factory.getMapperFacade().mapAsSet(sources, destination);
		} else {
			return factory.getMapperFacade().mapAsList(sources, destination);
		}
//		List<D> result = new ArrayList<>();
//		for(S s : sources)
//			result.add(this.map(s, destination));
//		return result;
	}
}