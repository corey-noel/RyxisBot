package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import felix.calculator.AngleMode;
import felix.calculator.Calculator;
import felix.calculator.SyntaxException;

public class CommandCalculator extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		System.out.println(event.getMessageSansCommand());
		try {
			if (event.getArgs()[0].equalsIgnoreCase("radian")) {
				if (event.getArgs().length > 1)
					return "" + Calculator.calculate(event.getMessageSansCommand().substring(event.getMessageSansCommand().indexOf(" ")), AngleMode.RADIAN); 
				else
					throw new SyntaxException("You need to provide an expression!");
			} else if (event.getArgs()[0].equalsIgnoreCase("degree")) {
				if (event.getArgs().length > 1)
					return "" + Calculator.calculate(event.getMessageSansCommand().substring(event.getMessageSansCommand().indexOf(" ")), AngleMode.DEGREE); 
				else 
					throw new SyntaxException("You need to provide an expression!");
			} else 
				return "" + Calculator.calculate(event.getMessageSansCommand(), AngleMode.DEGREE);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String getCommand() {
		return "calc";
	}

	@Override
	public String getHelp() {
		return "Returns the calculated value of the input. (Place 'radian' before an expression to use radian mode.)";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return getHelp();
	}
}
