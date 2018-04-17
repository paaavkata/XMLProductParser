package com.premiummobile.First.magento;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = AttributeDeserializer.class)
public interface Attribute {

	public String getAttributeCode();
	public void setAttributeCode(String code);
	public Object getValue();
	public void setValue(Object value);
}
