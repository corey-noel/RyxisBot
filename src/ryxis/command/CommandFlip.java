package ryxis.command;

import java.util.Random;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandFlip extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		Random r = new Random();
		if (event.getArgs().length == 0) 
			return r.nextInt(2) == 0 ? "Heads!" : "Tails!";
		else {
			try {
				return flipCoins(Integer.parseInt(event.getArgs()[0]), r);
			}
			catch (NumberFormatException e) {
				return "Give me a number of coins to flip.";
			}
		}
	}

	@Override
	public String getCommand() {
		return "flip";
	}

	@Override
	public String getHelp() {
		return "Flips one or more coins.";
	}
	
	private String flipCoins(int coins, Random r) {
		if (coins <= 0 || coins > 10000)
			return "No more than 10000 or less than 0.";
		if (coins == 1)
			return r.nextInt(2) == 0 ? "Heads!" : "Tails!";
		int heads = 0, tails = 0;
		for (int i = 0; i < coins; i++) {
			if (r.nextInt(2) == 0)
				heads++;
			else
				tails++;
		}
		return "Flipped " + coins + " coins for a total of " + heads + " heads and " + tails + " tails."; 
	}
}
