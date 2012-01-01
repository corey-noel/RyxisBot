package ryxis.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import org.pircbotx.Colors;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandRoll extends BaseCommand{
	
	private final static String genError = Colors.RED + "Bad formatting or something.";
	
	@Override
	public String onCall(CommandEvent event) {
		return Colors.BOLD + event.getUser().getNick() + Colors.NORMAL + ": " + roll(event.getMessageSansCommand());
	}
	
	@Override
	public String getCommand() {
		return "roll";
	}
	
	@Override
	public String getHelp() {
		return "Roll the dice! [ http://en.wikipedia.org/wiki/Dice_notation ]";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return getHelp();
	}
	
	private static String roll(String message) {
		message = message.replaceAll("\\s", "").toLowerCase();
		
		if (message.equals(""))
			return genError;
		
		if (!message.matches("([\\ddk\\+\\-lh%]*)")) 
			return Colors.RED + "Invalid character";
		
		String[] tokens = message.split("(?<!^)(?=[dk+-])");
		System.out.println(Arrays.toString(tokens));
		
		int numDie = 0, numSides = 0, modifier = 0, keep = 0;
		KeeperType keeperType = KeeperType.KEEP_ALL;
		boolean percentile = false;
		
		try {
			for (String token : tokens) {				
				if (Character.isDigit(token.charAt(0))) {
					if (numDie != 0)
						return Colors.RED + "Duplicate dice value!";
					else
						numDie = Integer.parseInt(token);
					if (numDie > 100)
						return Colors.RED + "Cannot have more than 100 die!";
					else if (numDie == 0)
						return Colors.RED + "Cannot have zero dice!";
				}
				
				else {
					if (token.length() < 2)
						return Colors.RED + "Expected value after " + token.charAt(0) + "!";
					
					switch (token.charAt(0)) {
						case 'd':
							if (numSides != 0)
								return Colors.RED + "Duplicate sides value!";
							else if (token.charAt(1) == '%') 
								percentile = true;
							else 
								numSides = Integer.parseInt(token.substring(1));
							if (numSides == 0)
								return Colors.RED + "Cannot have zero sides!";
							else if (numSides > 100)
								return Colors.RED + "Cannot have more than 100 sides!";
							break;
							
						case 'k' :
							if (!keeperType.equals(KeeperType.KEEP_ALL) || keep != 0)
								return Colors.RED + "Duplicate keeper value!";
							else {
								keeperType = KeeperType.DROP_BOTH;
								keep = Integer.parseInt(token.substring(1));
							}
							if (keep == 0)
								return Colors.RED + "Cannot keep zero dice!";
							break;
							
						case '+' :
							if (modifier != 0)
								return Colors.RED + "Duplicate modifier value!";
							else
								modifier = Integer.parseInt(token.substring(1));
							if (modifier == 0)
								return Colors.RED + "Cannot have a modifier of 0!";
							else if (modifier > 100)
								return Colors.RED + "Cannot have a  modifier of more than 100!";
							break;
							
						case '-' :
							if (Character.isDigit(token.charAt(1))) {
								if (modifier != 0)
									return Colors.RED + "Duplicate modifier value!";
								else
									modifier = 0 - Integer.parseInt(token.substring(1));
								if (modifier == 0)
									return Colors.RED + "Cannot have a modifier of 0!";
							}
							else
								if (token.length() > 2)
									return genError;
								else if (!keeperType.equals(KeeperType.KEEP_ALL) || keep != 0)
									return Colors.RED + "Duplicate keeper value!";
								else if (token.charAt(1) == 'h')
									keeperType = KeeperType.DROP_HIGH;
								else if (token.charAt(1) == 'l')
									keeperType = KeeperType.DROP_LOW;
							break;
						default:
							return genError;
					}
				}
			}
		} 
		
		catch (NumberFormatException e) { 
			e.printStackTrace();
			return genError; 
		}
		catch (IndexOutOfBoundsException e) { 
			e.printStackTrace();
			return genError; 
		}
		
		if (percentile) {
			if (numDie != 0)
				return Colors.RED + "You can't specify the number of dice in a percentile roll.";
			else if (numSides != 0)
				return Colors.RED + "You can't specify the number of sides in a percentile roll.";
			else if (keep != 0 || !keeperType.equals(KeeperType.KEEP_ALL))
				return Colors.RED + "You can't have keepers on a percentile roll.";
			return percentileRoll(modifier);
		}
		
		else if (numDie == 1 || numDie == 0) {
				if (keep != 0 || !keeperType.equals(KeeperType.KEEP_ALL))
					return Colors.RED + "You can't have keepers on a single dice roll.";
				else if (numSides == 0)
					return Colors.RED + "Side value required.";
				return rollSingle(numSides, modifier);
		}
		
		else {
			if (keep > numDie)
				return Colors.RED + "You cannot keep more die then you have.";
			else if (numSides == 0)
				return Colors.RED + "Side value required.";
			return rollDice(numDie, numSides, modifier, keep, keeperType);		
		}
	}
	
	private static String percentileRoll(int modifier) {
		Random r = new Random();		
		String result = "Percentile roll: ";
		int roll = r.nextInt(100) + 1;
		if (modifier == 0)
			result += Colors.BOLD + roll + Colors.NORMAL + "!";
		else
			result += Colors.BOLD + (roll + modifier) + Colors.NORMAL + 
			" [" + roll + (modifier > 0 ? "+" : "") + modifier + "]!";
		return result;
	}
	
	private static String rollSingle(int numSides, int modifier) {
		Random r = new Random();
		int roll = r.nextInt(numSides) + 1;
		
		String result = "Rolled a d" + numSides + " for ";
		if (modifier == 0)
			result += Colors.BOLD + roll + Colors.NORMAL + "! ";
		else
			result += Colors.BOLD + (roll + modifier) + Colors.NORMAL +
					" [" + roll + (modifier > 0 ? "+" : "") + modifier + "]! ";
		
		if (numSides == 20 && roll == 20)
			result += Colors.BOLD + Colors.BLUE + "Critical success!" + Colors.NORMAL;
		else if (numSides == 20 && roll == 1)
			result += Colors.BOLD + Colors.RED + "Critical failure!" + Colors.NORMAL;
		
		return result;
	}
	
	private static String rollDice(int numDie, int numSides, int modifier, int keep, KeeperType keeperType) {
		Random r = new Random();
		
		ArrayList<Integer> rolls = new ArrayList<Integer>();
		for (int i = 0; i < numDie; i++)
			rolls.add(r.nextInt(numSides) + 1);
		Collections.sort(rolls);
		
		int sum = 0;
		String result = "";
		
		switch (keeperType) {
			case KEEP_ALL:
				sum = 0;
				for (Integer roll : rolls)
					sum += roll;
				
				result = "Rolled " + numDie + "d" + numSides + " for " + Colors.BOLD + (sum + modifier) + Colors.NORMAL + "! ";
				
				if (modifier != 0)
					result += "[" + sum + (modifier > 0 ? "+" : "") + modifier + "] ";
				
				result += "[";
				for (Integer roll : rolls)
					result += roll + ", ";
				result = result.substring(0, result.length() - 2) + "]";
				
				break;
				
			case DROP_BOTH:
				result = "Rolled " + numDie + "d" + numSides + " [Keeping " + keep + "] ";
				
				int lowSum = 0;
				for (int i = rolls.size() - 1; i > rolls.size() - keep - 1; i--)
					lowSum += rolls.get(i);
				
				int highSum = 0;
				for (int i = 0; i < keep; i++) 
					highSum += rolls.get(i);
				
				result += "Dropping low: ";
				
				result += Colors.BOLD + (lowSum + modifier) + Colors.NORMAL + " " + 
						(modifier != 0 ? ("[" + lowSum + (modifier > 0 ? "+" : "") + modifier + "]") : "") + " ";
				
				result += "[";
				for (int i = 0; i < rolls.size(); i++)
					result += (i > rolls.size() - keep - 1 ? Colors.GREEN : Colors.RED) + rolls.get(i) + Colors.NORMAL + ", ";
				result = result.substring(0, result.length() - 2) + "] ";
				
				result += "Dropping high: ";
				
				result += Colors.BOLD + (highSum + modifier) + Colors.NORMAL + " " + 
						(modifier != 0 ? ("[" + highSum + (modifier > 0 ? "+" : "") + modifier + "]") : "") + " ";
				
				result += "[";
				for (int i = 0; i < rolls.size(); i++)
					result += (i < keep ? Colors.GREEN : Colors.RED) + rolls.get(i) + Colors.NORMAL + ", ";
				result = result.substring(0, result.length() - 2) + "]";
				
				break;
				
			case DROP_LOW:
				for (int i = 1; i < rolls.size(); i++) 
					sum += rolls.get(i);
				result = "Rolled " + numDie + "d" + numSides + " for " + Colors.BOLD + (sum + modifier) + Colors.NORMAL + "! " + 
					(modifier != 0 ? ("[" + sum + (modifier > 0 ? "+" : "") + modifier + "] ") : " ") + "[Dropping the lowest.] ";
				
				result += "[" + Colors.RED + rolls.get(0) + Colors.NORMAL + ", ";
				for (int i = 1; i < rolls.size() - 1; i++)
					result += Colors.GREEN + rolls.get(i) + Colors.NORMAL + ", ";
				result += Colors.GREEN + rolls.get(rolls.size() - 1) + Colors.NORMAL + "]";
				
				break;
				
			case DROP_HIGH:
				for (int i = 0; i < rolls.size() - 1; i++) 
					sum += rolls.get(i);
				result = "Rolled " + numDie + "d" + numSides + " for " + Colors.BOLD + (sum + modifier) + Colors.NORMAL + "! " + 
					(modifier != 0 ? ("[" + sum + (modifier > 0 ? "+" : "") + modifier + "] ") : " ") + "[Dropping the highest.] [";
				
				for (int i = 0; i < rolls.size() - 1; i++)
					result += Colors.GREEN + rolls.get(i) + Colors.NORMAL + ", ";
				result += Colors.RED + rolls.get(rolls.size() - 1) + Colors.NORMAL + "]";
				
				break;
		}
		return result;
	}
	
	private static enum KeeperType {
		KEEP_ALL, DROP_HIGH, DROP_LOW, DROP_BOTH;
	}
}