package ryxis.command.core;

import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import ryxis.core.FilteredListener;
import ryxis.core.RyxisBot;

public class CommandHandler extends FilteredListener {
					
	public final static char COMMAND_CHAR = '!';
			
	@Override
	public void onFilteredMessage(MessageEvent<RyxisBot> event) {
		if (!event.getMessage().startsWith(COMMAND_CHAR + "")) 
			return;
		CommandEvent ce = new CommandEvent(event);
		ce.trimCommandChar(COMMAND_CHAR);
		onCall(ce);
	}
	
	@Override
	public void onFilteredPrivateMessage(PrivateMessageEvent<RyxisBot> event) {
		CommandEvent ce = new CommandEvent(event);
		ce.trimCommandChar(COMMAND_CHAR);
		onCall(ce);
	}
	
	public void onCall(CommandEvent event) {
		for (BaseCommand currentCommand : event.getBot().commands) {
			if(event.getMessage().toLowerCase().startsWith(currentCommand.getCommand())) {
				boolean success = false;
				String message = null;
				if (event.getUser().equals(event.getBot().master.getMaster()) || !currentCommand.isMasterOnly()) {
					if (event.getArgs().length >= currentCommand.getMinArgs()) {
						message = currentCommand.onCall(event);
						success = true;
					}
					else message = currentCommand.getUnderArgsMessage();
				} else message = event.getBot().master.getMasterReason();
				if (message != null) {
					if (success && currentCommand.respondWithAction()) event.respondAction(message);
					else event.respond(message);
				} 
				return;
			}
		}	
	}
}