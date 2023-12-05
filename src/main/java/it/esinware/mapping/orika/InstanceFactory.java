package it.esinware.mapping.orika;

import it.esinware.mapping.customize.MapplyInstanceFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;


public class InstanceFactory<T> implements ObjectFactory<T> {

	private MapplyInstanceFactory<T> realFactory;
	
	public InstanceFactory(MapplyInstanceFactory<T> realFactory) {
		this.realFactory = realFactory;
	}

	@Override
	public T create(Object source, MappingContext mappingContext) {
		return realFactory.create(source);
	}

}
