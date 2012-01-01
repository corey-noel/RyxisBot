package ryxis.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import ryxis.command.core.CommandEvent;

public class Speech {
	
	private final RyxisBot bot;
	private SpeechMode silent;
	private Set<Channel> silentChannels;
	public int maxMessageLength;
	public int maxMessageNum;
	
	public Speech(RyxisBot b) {
		bot = b;
		silent = SpeechMode.SPEAKING;
		silentChannels = new HashSet<Channel>();
		maxMessageLength = 512;
		maxMessageNum = 10;
	}
		
	public void setSilenceMode(SpeechMode silenceMode) { silent = silenceMode; }
	public SpeechMode getSilenceMode() { return silent; }
	public void toggleSilenceMode() { 
		if (silent == SpeechMode.SILENT || silent == SpeechMode.PM_ONLY)
			silent = SpeechMode.SPEAKING;
		else
			silent = SpeechMode.PM_ONLY;
	}
	
	public void addSilentChannel(Channel chan) { silentChannels.add(chan); }
	public void removeSilentChannel(Channel chan) { silentChannels.remove(chan); }
	public boolean getChannelSilence(Channel chan) { return silentChannels.contains(chan); }
	public void toggleChannelSilence(Channel chan) { 
		if(getChannelSilence(chan)) 
			removeSilentChannel(chan);
		else
			addSilentChannel(chan);
	}
			
	public void sendMessage(User user, String message) {
		if ((silent == SpeechMode.SPEAKING || silent == SpeechMode.PM_ONLY) && message != null)
			for (String s : splitMessage(message, user.getNick()))
				bot.sendRawMessage(user, s);
	}
	
	public void sendMessage(Channel chan, String message) {
		if (!silentChannels.contains(chan) && message != null && silent == SpeechMode.SPEAKING)
			for (String s : splitMessage(message, chan.getName()))
				bot.sendRawMessage(chan, s);
	}
	
	public void sendAction(User user, String message) {
		if ((silent == SpeechMode.SPEAKING || silent == SpeechMode.PM_ONLY) && message != null)
			for (String s : splitMessage(message, user.getNick()))
				bot.sendRawAction(user, s);
	}
	
	public void sendAction(Channel chan, String message) {
		if (!silentChannels.contains(chan) && message != null && silent == SpeechMode.SPEAKING)
			for (String s : splitMessage(message, chan.getName()))
				bot.sendRawAction(chan, s);
	}
	
	public void sendNotice(User user, String message) {
		if ((silent == SpeechMode.SPEAKING || silent == SpeechMode.PM_ONLY) && message != null)
			for (String s : splitMessage(message, user.getNick()))
				bot.sendNotice(user, s);
	}
	
	public void sendMessageIgnoreSilence(User user, String message) {
		if (message != null)
			for (String s : splitMessage(message, user.getNick()))
				bot.sendRawMessage(user, s);
	}
	
	public void reply(Event<RyxisBot> event, String message) {
		if (message != null) {
			if (event instanceof MessageEvent) {
				Channel chan = ((MessageEvent<RyxisBot>) event).getChannel();
				if (!silentChannels.contains(chan))
					sendMessage(chan, message);
			} else if (event instanceof PrivateMessageEvent)
				sendMessage(((PrivateMessageEvent<RyxisBot>) event).getUser(), message);
			else if (event instanceof CommandEvent)
				((CommandEvent)event).respond(message);
			else if (event instanceof ActionEvent) {
				if (((ActionEvent<RyxisBot>)event).getChannel() == null)
					sendMessage(((ActionEvent<RyxisBot>)event).getUser(), message);
				else
					sendMessage(((ActionEvent<RyxisBot>)event).getChannel(), message);
			}
		}
	}
	
	public void replyAction(Event<RyxisBot> event, String message) {
		if (message != null) {
			if (event instanceof MessageEvent) {
				Channel chan = ((MessageEvent<RyxisBot>) event).getChannel();
				if (!silentChannels.contains(chan))
					sendAction(chan, message);
			} else if (event instanceof PrivateMessageEvent)
				sendAction(((PrivateMessageEvent<RyxisBot>) event).getUser(), message);
			else if (event instanceof CommandEvent)
				((CommandEvent)event).respondAction(message);
			else if (event instanceof ActionEvent) {
				if (((ActionEvent<RyxisBot>)event).getChannel() == null)
					sendAction(((ActionEvent<RyxisBot>)event).getUser(), message);
				else
					sendAction(((ActionEvent<RyxisBot>)event).getChannel(), message);
			}
		}
	}
	
	private ArrayList<String> splitMessage(String message, String recipient) {
		int additionalLength = bot.getUserBot().getNick().length() +  recipient.length() + 30;
		ArrayList<String> messageBits = new ArrayList<String>();
		String tempBit = null;
		boolean trimmed = false;
		
		while ((message.length() + additionalLength > maxMessageLength || message.contains("\n")) && messageBits.size() < maxMessageNum - 1) {
			if (message.length() + additionalLength > maxMessageLength)
				tempBit = message.substring(0, maxMessageLength - additionalLength);
			else
				tempBit = message;
			trimmed = false;
		
			
			if (tempBit.contains("\n")) {
				tempBit = message.substring(0, message.indexOf('\n'));
				trimmed = true;
			} else if (tempBit.contains(" ")) {
				tempBit = tempBit.substring(0, tempBit.lastIndexOf(" "));
				trimmed = true;
			}

			message = message.substring(tempBit.length() + (trimmed ? 1 : 0));
			messageBits.add(tempBit);
		}
		
		messageBits.add(message);
		return messageBits;
	}
	
	public enum SpeechMode {
		SILENT, PM_ONLY, SPEAKING;
		
		public String toString() {
			switch(this) {
				case SILENT:
					return "silent";
				case PM_ONLY:
					return "private messages only";
				case SPEAKING:
					return "speaking";
			}
			return null;
		}
	}
}
