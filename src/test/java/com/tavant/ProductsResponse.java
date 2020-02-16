package com.tavant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductsResponse {

	@JsonProperty("value")
	 Value[] value;

	public Value[] getValueList() {
		return value;
	}

	public void setValueList(Value[] value) {
		this.value = value;
	}

}
