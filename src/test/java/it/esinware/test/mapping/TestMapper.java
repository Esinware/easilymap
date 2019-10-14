package it.esinware.test.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import it.esinware.mapping.BeanMapper;
import it.esinware.test.model.source.AdvancedSourceModel;
import it.esinware.test.model.source.ExternalSourceModel;
import it.esinware.test.model.source.SourceModel;
import it.esinware.test.model.target.TargetModelImpl;

public class TestMapper {

	@Test
	public void simpleMapping() {
		BeanMapper mapper = new BeanMapper();
		AdvancedSourceModel origin = new AdvancedSourceModel();
		origin.setFirstField("first field");
		origin.setSecondField("second field");
		origin.setExtraField("Extra field");
		TargetModelImpl mapped = mapper.map(origin, TargetModelImpl.class);
		System.out.println(mapped.toString());
		assertNotNull(mapped.getCommonField());
		ExternalSourceModel extModel = new ExternalSourceModel();
		extModel.setFieldA("FieldA");
		extModel.setFieldB("FieldB");
		extModel.setImplField("ImplField");
		mapped = mapper.map(extModel, TargetModelImpl.class);
		System.out.println(mapped.toString());
		assertNotNull(mapped.getExtraField());
	}
}