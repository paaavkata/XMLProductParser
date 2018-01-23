package com.premiummobile.First.solytron.Model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Image {
	
	@Attribute
	private String type;
	
	private String text;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Text
	public String getText() {
		return text;
	}

	@Text
	public void setText(String text) {
		this.text = text;
	}
	
	
}
