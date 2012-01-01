 package ryxis.command;

import java.util.Set;
import org.pircbotx.Channel;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.core.Speech;

public class CommandSilence extends BaseCommand{

	@Override
	public String getCommand() {
		return "silence";
	}

	@Override
	public String getHelp() {
		return "Can be used be channel ops to shut the bot up in a specific room.";
	}

	@Override
	public String onCall(CommandEvent event) {
		if (event.getUser().equals(event.getBot().master.getMaster())) {
			if (event.getArgs().length == 0)
				event.getBot().speech.toggleSilenceMode();
			else if (event.getArgs()[0].equalsIgnoreCase("silence"))
				event.getBot().speech.setSilenceMode(Speech.SpeechMode.SILENT);
			else if (event.getArgs()[0].equalsIgnoreCase("pm"))
				event.getBot().speech.setSilenceMode(Speech.SpeechMode.PM_ONLY);
			else if (event.getArgs()[0].equalsIgnoreCase("speaking"))
				event.getBot().speech.setSilenceMode(Speech.SpeechMode.SPEAKING);
			else if (event.getArgs()[0].startsWith("#")) {
				Set<Channel> chans = event.getBot().getChannels();
				for (Channel chan : chans)
					if (chan.getName().equals(event.getArgs()[0])) {
						event.getBot().speech.toggleChannelSilence(chan);
						event.getBot().sendMessage(event.getUser(), "Silence mode in " + chan.getName() + " set to " + event.getBot().speech.getChannelSilence(chan));
						return null;
					}
			} else {
				event.getBot().sendMessage(event.getUser(), getHelp());
				return null;
			}
			event.getBot().sendMessage(event.getUser(), "Silence mode set to " + event.getBot().speech.getSilenceMode().toString() + ".");
			return null;
		} else if (!event.isPrivate() && (event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser()))) {
			event.getBot().speech.toggleChannelSilence(event.getChannel());
			event.getBot().sendMessage(event.getUser(), event.getChannel().getName() + "'s silent mode set to " + (event.getBot().speech.getChannelSilence(event.getChannel()) ? "silent." : "speaking."));
			return null;
		} return event.getBot().master.getMasterReason();
	}
}