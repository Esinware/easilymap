package it.esinware.test.model.target;

import it.esinware.mapping.annotation.FieldBinding;
import it.esinware.mapping.annotation.TypeBinding;
import it.esinware.test.mapping.converter.SimpleToString;
import it.esinware.test.model.source.AdvancedSourceModel;
import ma.glasnost.orika.metadata.MappingDirection;

@TypeBinding(binding = AdvancedSourceModel.class)
public class AdvancedTargetModelImpl extends AbstractTargetModel {

	@FieldBinding(binding = "source", converter = SimpleToString.class, direction = MappingDirection.B_TO_A)
	private String aFromSimple;

	public String getaFromSimple() {
		return aFromSimple;
	}

	public void setaFromSimple(String aFromSimple) {
		this.aFromSimple = aFromSimple;
	}

	@Override
	public String toString() {
		return "AdvancedTargetModelImpl [aFromSimple=" + aFromSimple + ", toString()=" + super.toString() + "]";
	}
}