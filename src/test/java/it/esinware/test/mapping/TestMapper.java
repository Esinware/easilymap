package it.esinware.test.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.net.SimpleSocketServer;
import it.esinware.mapping.BeanMapper;
import it.esinware.test.model.source.AdvancedSourceModel;
import it.esinware.test.model.source.ExternalSourceModel;
import it.esinware.test.model.source.SimpleSource;
import it.esinware.test.model.source.SourceModel;
import it.esinware.test.model.target.AdvancedTargetModelImpl;
import it.esinware.test.model.target.TargetModelImpl;

public class TestMapper {

	@Test
	public void simpleMapping() {
		BeanMapper mapper = new BeanMapper();
		AdvancedSourceModel origin = new AdvancedSourceModel();
		origin.setFirstField("first field");
		origin.setSecondField("second field");
		origin.setExtraField("Extra field");
		SimpleSource simple = new SimpleSource();
		simple.setA("Valore di A");
		simple.setB("Valore di B");
		origin.setSource(simple);
		AdvancedTargetModelImpl mapped = mapper.map(origin, AdvancedTargetModelImpl.class);
		System.out.println(mapped.toString());
		assertNotNull(mapped.getaFromSimple());
		ExternalSourceModel extModel = new ExternalSourceModel();
		extModel.setFieldA("FieldA");
		extModel.setFieldB("FieldB");
		extModel.setImplField("ImplField");
		mapped = mapper.map(extModel, AdvancedTargetModelImpl.class);
		System.out.println(mapped.toString());
		assertNotNull(mapped.getExtraField());
	}
}