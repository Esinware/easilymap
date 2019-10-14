package it.esinware.mapping.orika;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class PlaceholderConverter extends CustomConverter<Void, Void> {

	@Override
	public Void convert(Void source, Type<? extends Void> destinationType, MappingContext mappingContext) {
		return null;
	}
}