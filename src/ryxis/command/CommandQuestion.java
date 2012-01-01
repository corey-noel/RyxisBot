package ryxis.command;

import java.io.IOException;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.core.LinkAnalyzer;
import ryxis.tools.StringTools;

// sample http://api.wolframalpha.com/v2/query?input=pi&appid=JW477J-WKXXQ4XPEH
// APP ID: JW477J-WKXXQ4XPEH

public class CommandQuestion extends BaseCommand {
	
	private final String START_TEXT = "http://api.wolframalpha.com/v2/query?input=";
	private final String END_TEXT = "&appid=JW477J-WKXXQ4XPEH";
	
	@Override
	public String onCall(CommandEvent event) {
		try {
			return LinkAnalyzer.wolframAlphaAnalyzer(START_TEXT + StringTools.turnIntoURL(event.getMessageSansCommand()) + END_TEXT);
		} catch (IOException e) {
			e.printStackTrace();
			return "I had issues asking that question!";
		} //TODO this dunn' fucks up a lot
	}

	@Override
	public String getCommand() {
		return "question";
	}

	@Override
	public String getHelp() {
		return "Search WolframAlpha! Please read before using this command: http://pastebin.com/6wmjX8w9";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return "You need a question to ask a question. Obviously.";
	}
}
