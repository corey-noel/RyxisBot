package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.core.LinkAnalyzer;
import ryxis.tools.StringTools;


public class CommandSummary extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		String parsedURL = null;
		if (event.getArgs().length > 0)
			parsedURL = StringTools.parseURL(event.getArgs()[0]);
		else if (!event.isPrivate()) {
			parsedURL = StringTools.parseURL(event.getBot().roomHandler.getRoom(event.getChannel()).getLink());
		}
		if (parsedURL != null) {
			try {
				String URLTitle = LinkAnalyzer.getTitle(parsedURL, false);
				if (URLTitle == null)
					return "Looks like that page doesn't have a title! (" + parsedURL + ")";
				else
					return "Summary of " + parsedURL + ": " + URLTitle;
			}
			catch (Exception e) {
				e.printStackTrace();
				return "Couldn't get that URL... (" + parsedURL + ")";
			}
		}
		return null;
	}

	@Override
	public String getCommand() {
		return "summary";
	}

	@Override
	public String getHelp() {
		return "Gets the title of the given URL. (Even redirects! Uses a recent URL if none is given.)";
	}
}