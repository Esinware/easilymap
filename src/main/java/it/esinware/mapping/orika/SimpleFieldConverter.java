package it.esinware.mapping.orika;

import it.esinware.mapping.BeanMapper;
import it.esinware.mapping.customize.MapplyFieldConverter;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class SimpleFieldConverter<S, D> extends CustomConverter<S, D> {

	private MapplyFieldConverter<S, D> realConverter;
	private BeanMapper mapper;
	
	public SimpleFieldConverter(MapplyFieldConverter<S, D> converter, BeanMapper mapper) {
		super();
		realConverter = converter;
		this.mapper = mapper;
	}

	@Override
	public D convert(S source, Type<? extends D> destinationType, MappingContext mappingContext) {
		return realConverter.convert(source, mapper);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
