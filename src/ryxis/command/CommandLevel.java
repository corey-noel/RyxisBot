package ryxis.command;

import org.pircbotx.Colors;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;


public class CommandLevel extends BaseCommand {
	
	private final static String block = "#";

	@Override
	public String onCall(CommandEvent event) {
		String nick = null, meter = null;
		if (event.getArgs().length < 1)
			return getHelp();
		else if (event.getArgs().length == 1)
			nick = event.getUser().getNick();
		else
			nick = event.getArgs()[1];
		meter = event.getArgs()[0];
		
		int meterValue = Math.abs(nick.hashCode() + meter.hashCode()) % 101;
		
		String result = nick + "'s " + meter + " level: " + meterValue + "% ";
		
		result += "[";		
		
		if (meterValue < 20)
			result += Colors.BLUE;
		else if (meterValue < 40)
			result += Colors.GREEN;
		else if (meterValue < 60)
			result += Colors.YELLOW;
		else if (meterValue < 80)
			result += Colors.BROWN;
		else 
			result += Colors.RED;
		
		for (int i = 0; i < 10; i++)
			if (i * 10 < meterValue)
				result += block;
			else
				result += "_";
		
		result += Colors.NORMAL + "] ";
		
		if (meterValue <= 5)
			result += "(Not even slightly " + meter + ".)";
		else if (meterValue >= 95)
			result += "(Hella " + meter +".)";
		
		return result;
	}

	@Override
	public String getCommand() {
		return "level";
	}

	@Override
	public String getHelp() {
		return "Find the level of someone's whatever. !level <something> <person>";
	}
	
}
