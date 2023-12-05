package it.esinware.mapping.customize;

import it.esinware.mapping.BeanMapper;

public class ConverterPlaceholder implements MapplyFieldConverter<Void, Void> {

	@Override
	public Void convert(Void source, BeanMapper mapper) {
		return null;
	}
}