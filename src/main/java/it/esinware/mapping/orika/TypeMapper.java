package it.esinware.mapping.orika;

import it.esinware.mapping.BeanMapper;
import it.esinware.mapping.customize.MapplyTypeMapper;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

public class TypeMapper<A, B> extends CustomMapper<A, B> {

	private MapplyTypeMapper<A, B> realMapper;
	private BeanMapper mapper;

	public TypeMapper(MapplyTypeMapper<A, B> realMapper, BeanMapper mapper) {
		super();
		this.realMapper = realMapper;
		this.mapper = mapper;
	}

	@Override
	public void mapAtoB(A a, B b, MappingContext context) {
		realMapper.mapAtoB(a, b, mapper);
	}

	@Override
	public void mapBtoA(B b, A a, MappingContext context) {
		realMapper.mapBtoA(b, a, mapper);
	}

}
