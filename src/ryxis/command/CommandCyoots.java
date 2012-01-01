package ryxis.command;

import java.io.File;
import java.io.IOException;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.FileTools;

public class CommandCyoots extends BaseCommand{
	
	private static final String fileLink = "words/cyoots";
	
	@Override
	public String onCall(CommandEvent event) {
		return FileTools.randomLine(fileLink).trim();
	}

	@Override
	public String getCommand() {
		return "cyoots";
	}

	@Override
	public String getHelp() {
		int numImages = -1;
		try {
			numImages = FileTools.getNumLines(new File(fileLink));
		} catch (IOException e) {}
		return "\"When you're feeling sad~\" An animal gif dispenser for your daily cute! (Currently " + (numImages <= 0 ? "[error]" : numImages) + " images!)" ;
	}

}
