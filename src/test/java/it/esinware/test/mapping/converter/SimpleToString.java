package it.esinware.test.mapping.converter;

import it.esinware.mapping.BeanMapper;
import it.esinware.mapping.annotation.MappingConverter;
import it.esinware.mapping.customize.MapplyFieldConverter;
import it.esinware.test.model.source.SimpleSource;

@MappingConverter
public class SimpleToString implements MapplyFieldConverter<SimpleSource, String> {

	@Override
	public String convert(SimpleSource source, BeanMapper mapper) {
		return source.getA();
	}
}
