package it.esinware.mapping.customize;

import it.esinware.mapping.BeanMapper;

public interface MapplyFieldBidiConverter<S, D> extends MapplyFieldConverter<S, D> {

	public S convertFrom(D source, BeanMapper mapper);
	
	public D convertTo(S source, BeanMapper mapper);
}
