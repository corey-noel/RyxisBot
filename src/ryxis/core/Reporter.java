package ryxis.core;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class Reporter extends ListenerAdapter<PircBotX> {
	
	private ReporterMode reportMode;
	private RyxisBot bot;
	
	public Reporter(RyxisBot b) {
		reportMode = ReporterMode.OFF;
		bot = b;
	}
	
	public String toggleReportMode() {
		if (reportMode == ReporterMode.ALL || reportMode == ReporterMode.INVISIBLE) {
			reportMode = ReporterMode.OFF;
			return "Report mode set to off.";
		} else {
			reportMode = ReporterMode.INVISIBLE;
			return "Report mode set to invisible messages.";
		}
	}
	
	public void setReportMode(ReporterMode mode) {
		reportMode = mode;
	}
	
	public void onMessage(MessageEvent<PircBotX> event) {
		if (reportMode == ReporterMode.ALL || (reportMode == ReporterMode.INVISIBLE && !event.getChannel().getUsers().contains(bot.master.getMaster())))
			bot.speech.sendMessageIgnoreSilence(bot.master.getMaster(), "Message from " + event.getUser().getNick() + " in " + event.getChannel().getName() + ": " + event.getMessage());
	}
	
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) {
		if (reportMode == ReporterMode.ALL || (reportMode == ReporterMode.INVISIBLE && !event.getUser().equals(bot.master.getMaster())))
			bot.speech.sendMessageIgnoreSilence(bot.master.getMaster(), "Private message from " + event.getUser().getNick() + ": " + event.getMessage());
	}
	
	public static enum ReporterMode {OFF, ALL, INVISIBLE}
}
