package it.esinware.mapping.orika;

import it.esinware.mapping.BeanMapper;
import it.esinware.mapping.customize.MapplyFieldBidiConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class BidiFieldConverter<S, D> extends BidirectionalConverter<S, D> {

	private MapplyFieldBidiConverter<S, D> realConverter;
	private BeanMapper mapper;

	public BidiFieldConverter(MapplyFieldBidiConverter<S, D> realConverter, BeanMapper mapper) {
		super();
		this.realConverter = realConverter;
		this.mapper = mapper;
	}

	@Override
	public D convertTo(S source, Type<D> destinationType, MappingContext mappingContext) {
		return realConverter.convertTo(source, mapper);
	}

	@Override
	public S convertFrom(D source, Type<S> destinationType, MappingContext mappingContext) {
		return realConverter.convertFrom(source, mapper);
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
