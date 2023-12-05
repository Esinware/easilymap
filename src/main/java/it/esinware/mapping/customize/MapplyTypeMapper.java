package it.esinware.mapping.customize;

import it.esinware.mapping.BeanMapper;

public interface MapplyTypeMapper<A, B> {

	public void mapAtoB(A a, B b, BeanMapper mapper);
	
	public void mapBtoA(B b, A a, BeanMapper mapper);
}
