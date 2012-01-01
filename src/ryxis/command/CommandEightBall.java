  package ryxis.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.FileTools;

public class CommandEightBall extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
			boolean containsYesTerm = false;
			boolean containsNoTerm = false;
			Scanner yes = null, no = null;
			try {
				yes = new Scanner(new File("words/8ball/yesTerms"));
				no = new Scanner(new File("words/8ball/noTerms"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			while (yes.hasNextLine() && !containsYesTerm) 
				containsYesTerm = event.getMessage().toLowerCase().contains(yes.nextLine().toLowerCase().trim()) ? true : containsYesTerm;
			while (no.hasNextLine() && !containsNoTerm) 
				containsNoTerm = event.getMessage().toLowerCase().contains(no.nextLine().toLowerCase().trim()) ? true : containsNoTerm;
			if (containsNoTerm)
				return FileTools.randomLine("words/8ball/noOnly");
			else if (containsYesTerm)
				return FileTools.randomLine("words/8ball/yesOnly");
			else
				return FileTools.randomLine("words/8ball/8ball");
	}

	@Override
	public String getHelp() {
		return "Seriously the least rigged 8ball in existence. Nothing fishy here.";
	}

	@Override
	public String getCommand() {
		return "8ball";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return "You need a question to address the unbiased magic 8-ball!";
	}
}
