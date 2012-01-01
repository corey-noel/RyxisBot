package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.core.Reporter;

public class CommandReport extends BaseCommand{
	
	private static final String ALL_MESSAGE = "Report mode set to all messages.", 
			INVIS_MESSAGE = "Report mode set to invisible messages.", 
			OFF_MESSAGE = "Report mode set to off.", ERR = "You dunn goofed.";
	
	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs().length == 0)
			return event.getBot().reporter.toggleReportMode();
		else {
			try {
				int input = Integer.parseInt(event.getArgs()[0]);
				switch (input) {
					case 0:
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.OFF);
						return OFF_MESSAGE;
					case 1:
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.ALL);
						return ALL_MESSAGE;
					case 2:
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.INVISIBLE);
						return INVIS_MESSAGE;
					default:
						return ERR;
				}
			} catch (NumberFormatException e) {
				switch (event.getArgs()[0].toLowerCase().charAt(0)) {
					case 'o':
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.OFF);
						return OFF_MESSAGE;
					case 'a':
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.ALL);
						return ALL_MESSAGE;
					case 'i':
						event.getBot().reporter.setReportMode(Reporter.ReporterMode.INVISIBLE);
						return INVIS_MESSAGE;
					default:
						return ERR;
				}
			}
		}
	}
	
	@Override
	public String getCommand() {
		return "report";
	}
	
	@Override
	public String getHelp() {
		return null;
	}
	
	@Override
	public boolean isMasterOnly() {
		return true;
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
}