package com.premiummobile.First.magento;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyListAttribute extends Attribute{
	
	@JsonProperty("attribute_code")
	private String attributeCode;
	@JsonProperty("value")
	private List<String> values;
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public List<String> getValues() {
		if(values == null){
			return new ArrayList<String>();
		}
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
}
