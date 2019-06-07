package it.esinware.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationEnumValue;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.FieldInfoList;
import io.github.classgraph.FieldInfoList.FieldInfoFilter;
import io.github.classgraph.ScanResult;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;
import ma.glasnost.orika.metadata.MappingDirection;

public class BeanMapper {

	private static Logger logger = LoggerFactory.getLogger(BeanMapper.class);
	private MapperFactory factory;
	private ScanResult classes;

	public BeanMapper(Class<?> sourcePackages) {
		this(sourcePackages.getPackage().getName());
	}
	
	public BeanMapper(String basePackageName) {
		factory = new DefaultMapperFactory.Builder().build();
		factory.registerConcreteType(SortedSet.class, TreeSet.class);
//		factory.registerObjectFactory(new OrganizationFactory(), TypeFactory.valueOf(Organization.class), TypeFactory.valueOf(OrganizationModel.class));
//		factory.registerObjectFactory(new OrganizationModelFactory(), TypeFactory.valueOf(OrganizationModel.class), TypeFactory.valueOf(Organization.class));
		classes = new ClassGraph().enableAllInfo().whitelistClasses().scan();
		registerConverters();
		init();
	}
	
	private void registerConverters() {
//		// Find for all custom converters annotated with MappingConverter
		ClassInfoList list = classes.getClassesWithAnnotation(MappingConverter.class.getName());
		for(ClassInfo classInfo : list) {
			AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(MappingConverter.class.getName());
			String converterId = (String)annotationInfo.getParameterValues().get("value");
			try {
				factory.getConverterFactory().registerConverter(converterId, (Converter<?, ?>)classInfo.loadClass().newInstance());
			} catch(InstantiationException | IllegalAccessException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void init() {
		ClassInfoList routeClassInfoList = classes.getClassesWithAnnotation(TypeBinding.class.getName());
		for(ClassInfo routeClassInfo : routeClassInfoList) {
			// Get the Route annotation on the class
			AnnotationInfo annotationInfo = routeClassInfo.getAnnotationInfo(TypeBinding.class.getName());
			if(annotationInfo != null) {
				AnnotationParameterValueList annotationParamVals = annotationInfo.getParameterValues();
				AnnotationClassRef bindingClass = (AnnotationClassRef)annotationParamVals.get("binding");
				AnnotationClassRef customizerClass = (AnnotationClassRef)annotationParamVals.get("customizer");
				try {
					ClassMapBuilder<?, ?> builder = factory.classMap(Class.forName(routeClassInfo.getName()), Class.forName(bindingClass.getName()));
					FieldInfoList fields = routeClassInfo.getDeclaredFieldInfo();
					fields.filter(new FieldInfoFilter() {
						
						@Override
						public boolean accept(FieldInfo fieldInfo) {
							return fieldInfo.hasAnnotation(FieldBinding.class.getName());
						}
					}).forEach(new Consumer<FieldInfo>() {

						@Override
						public void accept(FieldInfo t) {
							AnnotationInfo info = t.getAnnotationInfo(FieldBinding.class.getName());
							String fieldBinding = (String)info.getParameterValues().get("binding");
							String converterId = (String)info.getParameterValues().get("converter");
							MappingDirection direction = (MappingDirection)((AnnotationEnumValue)info.getParameterValues().get("direction")).loadClassAndReturnEnumValue();
							logger.debug("Binding [" + t.getName() + "] versus [" + fieldBinding + "]");
							FieldMapBuilder<?, ?> fieldBuilder = builder.fieldMap(t.getName(), fieldBinding);
							switch(direction) {
								case A_TO_B:
									fieldBuilder.aToB();
									break;
								case B_TO_A:
									fieldBuilder.bToA();
									break;
								case BIDIRECTIONAL:
									//Nothing to do
							}
							if(StringUtils.isNotBlank(converterId))
								fieldBuilder.converter(converterId);
							fieldBuilder.add();
						}
						
					});
					if(!customizerClass.getName().equals(CustomMapper.class.getName()))
						builder.customize((Mapper)Class.forName(customizerClass.getName()).newInstance());
					builder.byDefault().register();
				} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}
	
	public <S, D> void map(S source, D destination) {
		factory.getMapperFacade().map(source, destination);
	}

	public <S, D> D map(S source, Class<D> destination) {
		return factory.getMapperFacade().map(source, destination);
	}
	
	public <S, D> List<D> map(List<S> sources, Class<S> source, Class<D> destination) {
		List<D> result = new ArrayList<D>();
		for(S s : sources)
			result.add(factory.getMapperFacade().map(s, destination));
		return result;
	}
}