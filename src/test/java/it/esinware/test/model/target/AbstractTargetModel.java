package it.esinware.test.model.target;

public class AbstractTargetModel {

	private String commonField;

	private String extraField;

	public String getCommonField() {
		return commonField;
	}

	public void setCommonField(String commonField) {
		this.commonField = commonField;
	}

	public String getExtraField() {
		return extraField;
	}

	public void setExtraField(String extraField) {
		this.extraField = extraField;
	}

	@Override
	public String toString() {
		return "AbstractModel [commonField=" + commonField + ", extraField=" + extraField + "]";
	}
}