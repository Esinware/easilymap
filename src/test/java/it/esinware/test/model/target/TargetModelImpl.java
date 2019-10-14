package it.esinware.test.model.target;

import it.esinware.mapping.annotation.FieldBinding;
import it.esinware.mapping.annotation.FieldOverride;
import it.esinware.mapping.annotation.TypeBinding;
import it.esinware.test.model.source.AdvancedSourceModel;
import it.esinware.test.model.source.ExternalSourceModel;

@TypeBinding(typeId = "main", binding = AdvancedSourceModel.class, overrides = {
	@FieldOverride(origin = @FieldBinding(binding = "firstField"), override = "commonField")
})
@TypeBinding(typeId = "secondary", binding = ExternalSourceModel.class, excludes = "extraField", overrides = {
		@FieldOverride(origin = @FieldBinding(binding = "fieldA"), override = "commonField")
})
public class TargetModelImpl extends AbstractTargetModel {

	@FieldBinding(typeId = "main", binding = "secondField")
	@FieldBinding(typeId = "secondary", binding = "fieldB")
	private String implField;

	public String getImplField() {
		return implField;
	}

	public void setImplField(String implField) {
		this.implField = implField;
	}

	@Override
	public String toString() {
		return "ModelImpl [implField=" + implField + ", toString()=" + super.toString() + "]";
	}
}