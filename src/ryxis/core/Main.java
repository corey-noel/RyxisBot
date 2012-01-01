package ryxis.core;

import java.io.FileOutputStream;
import java.io.IOException;
import ryxis.gui.ConsoleWindow;
import ryxis.gui.LaunchWindow;

public class Main {
	
	private static RyxisBot bot;
	private static AdvancedProperties properties;
	private static LaunchWindow launchWindow;
	private static ConsoleWindow consoleWindow;
	
	private static final String configLocation = "data/profile";
	public static RyxisBot getBot() { return bot; }
	
	public static void main(String[] args) {	
		
		if (System.getProperty("os.name").equals("Mac OS X")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "RyxisBot");
		}
		
		properties = new AdvancedProperties(new DefaultProperties());
		/*
		try {
			properties.load(Start.class.getResourceAsStream(configLocation));
		} catch (IOException e) {
			System.err.println("Could not find the config file/there was an error creating it.");
			e.printStackTrace();
		}
		*/

		launchWindow = new LaunchWindow(
				properties.getProperty("botNick"), 
				properties.getProperty("botPass"), 
				properties.getProperty("serverName"), 
				properties.getProperty("masterNick"));
		launchWindow.setVisible(true);
	}
	
	public static void start() {
		properties.setProperty("botNick", launchWindow.getBotNickname());
		properties.setProperty("botPass", launchWindow.getBotPassword());
		properties.setProperty("serverName", launchWindow.getServerName());
		properties.setProperty("masterNick", launchWindow.getMasterNickname());
		
		try {
			properties.store(new FileOutputStream(configLocation), null);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not find the config file/there was an error creating it.");
		}
		
		bot = new RyxisBot(properties.getProperty("botNick"), properties.getProperty("masterNick"), 
				properties.getProperty("serverName"));
		
		
		consoleWindow = new ConsoleWindow();
		consoleWindow.setVisible(true);
		
		new Thread( new Runnable() { public void run() { 
			bot.connect(properties.getProperty("serverName"), false, 
					properties.getProperty("botPass"), properties.getStringArray("autoRooms")); 
		} } ).start();
	}
	
	public static void quitAll() {
		launchWindow.dispose();
		consoleWindow.dispose();
	}
}