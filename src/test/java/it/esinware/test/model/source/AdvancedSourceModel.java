package it.esinware.test.model.source;

public class AdvancedSourceModel extends SourceModel {

	private String extraField;
	private SimpleSource source;

	public String getExtraField() {
		return extraField;
	}

	public void setExtraField(String extraField) {
		this.extraField = extraField;
	}

	public SimpleSource getSource() {
		return source;
	}

	public void setSource(SimpleSource source) {
		this.source = source;
	}
}
