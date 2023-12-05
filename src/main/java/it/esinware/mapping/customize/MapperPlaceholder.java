package it.esinware.mapping.customize;

import it.esinware.mapping.BeanMapper;

public class MapperPlaceholder implements MapplyTypeMapper<Void, Void> {

	@Override
	public void mapAtoB(Void a, Void b, BeanMapper mapper) {
		// Placeholder only for default parameter
	}

	@Override
	public void mapBtoA(Void b, Void a, BeanMapper mapper) {
		// Placeholder only for default parameter
		
	}

}