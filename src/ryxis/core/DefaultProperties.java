package ryxis.core;

public class DefaultProperties extends AdvancedProperties {

	private static final long serialVersionUID = -3954038261483406581L;

	public DefaultProperties() {
		setProperty("messageDelay", "500");
		setProperty("SSL", "true");
		setProperty("botNick", "RyxisBot");
		setProperty("serverName", "otter.anthrochat.net");
		setProperty("masterNick", "Ryxis");
	}
}