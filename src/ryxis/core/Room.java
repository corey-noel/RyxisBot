package ryxis.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import ryxis.command.core.CommandHandler;
import ryxis.tools.StringTools;


public class Room {

	public final Channel channel;
	private HashMap<User, Long> userActivity;
	private String lastMessage;
	private int messageRepeats;
	private String lastLink;
	private RyxisBot bot;
	
	public Room(Channel chan, RyxisBot b) {
		bot = b;
		channel = chan;
		userActivity = new HashMap<User, Long>();
		lastMessage = "";
		messageRepeats = -1;
		for (User user : channel.getUsers())
			userActivity.put(user, 0L);
		lastLink = null;
	}
	
	public void updateRoom(User user, String message, Event<RyxisBot> event) {
		if (event instanceof MessageEvent || event instanceof JoinEvent || event instanceof ActionEvent)
			userActivity.put(user, System.currentTimeMillis());
		else 
			userActivity.remove(user);
		
		if (message != null) {
			if (lastMessage.equals(message) && !message.startsWith(CommandHandler.COMMAND_CHAR + ""))
				messageRepeats++;
			else
				messageRepeats = 0;
			if (lastMessage.equals(message) && messageRepeats >= 2) {
				messageRepeats = -1;
				bot.speech.reply(event, message);
			}
			lastMessage = message;
			
			if (StringTools.containsURL(message)) {
				lastLink = StringTools.getURL(message);
			}
		}
	}
	
	public Set<User> getActiveUsers(long activeTime) {
		long currentTime = System.currentTimeMillis();
		Set<User> result = new HashSet<User>();
		for (Entry<User, Long> entry : userActivity.entrySet())
			if (currentTime - entry.getValue() < activeTime && !entry.getKey().isAway())
				result.add(entry.getKey());
		return result;
	}
		
	public String toString() {
		return this.channel.toString();
	}
		
	public String getLink() {
		return lastLink;
	}
}
