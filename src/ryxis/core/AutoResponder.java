package ryxis.core;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import ryxis.command.core.CommandEvent;
import ryxis.tools.FileTools;

public class AutoResponder extends FilteredListener {
	
	@Override
	public void onFilteredAction(ActionEvent<RyxisBot> event) {
		if (event.getMessage().contains("yawn")) {
			event.getBot().speech.replyAction(event, FileTools.randomLine("words/yawns").replace("<user>", event.getUser().getNick()));
		}
	}
	
	@Override
	public void onFilteredMessage(MessageEvent<RyxisBot> event) {
		CommandEvent ce = new CommandEvent(event);
		ce.respond(event.getBot().linkAnalyzer.analyzeMessage(ce));
	}

	@Override
	public void onFilteredPrivateMessage(PrivateMessageEvent<RyxisBot> event) {
		CommandEvent ce = new CommandEvent(event);
		ce.respond(event.getBot().linkAnalyzer.analyzeMessage(ce));
	}
	
	@Override
	public void onFilteredInvite(InviteEvent<RyxisBot> event) {
		event.getBot().joinChannel(event.getChannel());
	}
}
