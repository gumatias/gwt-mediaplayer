package net.sf.video4j.gwt.plugin.shared.vast;

public enum Delivery {
	Streaming,
	Progressive;

	public static Delivery parse(String pValue) {
		if (Streaming.name().equalsIgnoreCase(pValue)) return Streaming;
		return Progressive;
	}
}
