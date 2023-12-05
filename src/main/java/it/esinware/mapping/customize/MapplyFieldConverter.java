package it.esinware.mapping.customize;

import it.esinware.mapping.BeanMapper;

public interface MapplyFieldConverter<S, D> {

	public D convert(S source, BeanMapper mapper);
}
