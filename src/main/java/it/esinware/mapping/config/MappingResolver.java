package it.esinware.mapping.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import it.esinware.mapping.annotation.Mapply;
import it.esinware.mapping.annotation.MappingConverter;
import it.esinware.mapping.annotation.TypeBinding;
import it.esinware.mapping.customize.MapplyFieldConverter;

public class MappingResolver {

	private static Logger logger = LoggerFactory.getLogger(MappingResolver.class);
	private List<BeanWrapper> beans;
	private Map<Class<? extends MapplyFieldConverter<?, ?>>, String> converters;
	private ScanResult classes;
	
	public MappingResolver() {
		converters = new HashMap<>();
		beans = new ArrayList<>();
		classes = new ClassGraph().enableAllInfo().whitelistClasses().scan();
		registerConverters();
		loadMultiple();
		loadSingle();
	}
	
	public List<BeanWrapper> getBeans() {
		return this.beans;
	}
	
	public Map<Class<? extends MapplyFieldConverter<?, ?>>, String> getConverters() {
		return converters;
	}
	
	@SuppressWarnings("unchecked")
	private void registerConverters() {
		// Find for all custom converters annotated with MappingConverter
		ClassInfoList list = classes.getClassesWithAnnotation(MappingConverter.class.getName());
		for(ClassInfo classInfo : list) {
			Class<?> concreteClass = classInfo.loadClass();
			if(MapplyFieldConverter.class.isAssignableFrom(concreteClass)) {
				AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(MappingConverter.class.getName());
				String converterId = annotationInfo != null && !((String)annotationInfo.getParameterValues().get("value")).isEmpty() ? (String)annotationInfo.getParameterValues().get("value") : classInfo.getName();
				converters.put((Class<? extends MapplyFieldConverter<?, ?>>)concreteClass, converterId);
				logger.debug("Converter added {}", converterId);
			}
		}
	}
	
	private void loadMultiple() {
		ClassInfoList classInfoList = classes.getClassesWithAnnotation(Mapply.class.getName());
		for(ClassInfo classInfo : classInfoList) {
			Class<?> clazz = classInfo.loadClass();
			Mapply container = clazz.getAnnotation(Mapply.class);
			for(TypeBinding binding : container.value()) {
				BeanWrapper wrapper = new BeanWrapper(clazz, binding);
				this.beans.add(wrapper);
			}
		}
	}
	
	private void loadSingle() {
		ClassInfoList classInfoList = classes.getClassesWithAnnotation(TypeBinding.class.getName());
		for(ClassInfo classInfo : classInfoList) {
			Class<?> clazz = classInfo.loadClass();
			TypeBinding binding = clazz.getAnnotation(TypeBinding.class);
			BeanWrapper wrapper = new BeanWrapper(clazz, binding);
			this.beans.add(wrapper);
		}
	}
}