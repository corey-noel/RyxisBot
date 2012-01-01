package ryxis.core;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;

public class Master extends ListenerAdapter<PircBotX>{
	
	private User masterUser;
	private String master;
	private String dcReason = "My master told me to!";
	private String masterReason = "You must be the bot's master to issue that command!";
	private RyxisBot bot;
	
	public Master(RyxisBot b) {
		bot = b;
	}
	
	public String getMasterReason() { return masterReason; }
	public void setMasterReason(String reason) { masterReason = reason; }
	
	public String getDisconnectReason() { return dcReason; }
	public void setDisconnectReason(String reason) { dcReason = reason; }
	
	public void setMaster(String masterNick) { 
		master = masterNick; 
	}
	
	public User getMaster() { 
		if (masterUser != null) 
			return masterUser;
		else {
			masterUser = bot.getUser(master);
			return masterUser;
		}
	}
	
	@Override
	public void onConnect(ConnectEvent<PircBotX> event) {
		setMaster(master);
	}
}
