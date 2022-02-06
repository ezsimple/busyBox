package io.mkeasy.webapp.utils;

public class RSMeta {

	String name;
	String type;
	int precision;
	int scale;
	int columnDisplaySize;
	
	public RSMeta(String name, String type, int precision, int scale, int columnDisplaySize){
		this.name = name;
		this.type = type;
		this.precision = precision;
		this.scale = scale;
		this.columnDisplaySize = columnDisplaySize;
	}
	public int getPrecision() {
		return precision;
	}
	public int getScale() {
		return scale;
	}
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String toString() {
		return 
			"name=" + name +
			", type=" + type +
			", precision=" + precision +
			", scale=" + scale +
			", columnDisplaySize=" + columnDisplaySize ;
	}
}