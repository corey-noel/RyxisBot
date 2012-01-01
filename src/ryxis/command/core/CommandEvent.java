package ryxis.command.core;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import ryxis.core.RyxisBot;
import ryxis.tools.StringTools;

public class CommandEvent extends Event<RyxisBot> {
	
	private boolean isPrivate;
	private String message;
	private User user;
	private Channel chan;
	private String[] args;
	
	public CommandEvent(MessageEvent<RyxisBot> event) {
		super(event.getBot());
		isPrivate = false;
		message = event.getMessage();
		user = event.getUser();
		chan = event.getChannel();
		args = StringTools.parseArgs(event.getMessage());
	}
	
	public CommandEvent(PrivateMessageEvent<RyxisBot> event) {
		super(event.getBot());
		isPrivate = true;
		message = event.getMessage();
		user = event.getUser();
		chan = null;
		args = StringTools.parseArgs(event.getMessage());
	}
		
	public boolean isPrivate() {return isPrivate;}
	public String getMessage() {return message;}
	public Channel getChannel() {return chan;}
	public User getUser() {return user;}
	public String[] getArgs() {return args;}
	
	public String getMessageSansCommand() {
		if (message.contains(" "))
			return message.substring(message.indexOf(" ") + 1);
		return null;
	}

	public void trimCommandChar(char commandChar) {
		if (message.startsWith(commandChar + "")) {
			message = message.substring(1);
		}
	}	
	
	public void respond(String response) {
		if (isPrivate) getBot().sendMessage(user, response);
		else getBot().sendMessage(chan, response);
	}	
		
	public void respondAction(String response) {
		if (isPrivate) getBot().sendAction(user, response);
		else getBot().sendAction(chan, response);
	}
}