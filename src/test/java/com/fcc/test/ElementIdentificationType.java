package com.fcc.test;

public enum ElementIdentificationType {

	ID("id"),XPATH("xpath"),CSS("css"),NAME("name"),LINKTEXT("linktext");
	
private String identificationType;
	
	private ElementIdentificationType (String _identificationType) {
		this.identificationType=_identificationType;
	}
	
	public String getIdentificationType() {
		return  identificationType;
	}
}
