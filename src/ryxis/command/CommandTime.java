package ryxis.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandTime extends BaseCommand {

	private final static String errorMessage = "Give your desired time zone in integer form. Go here for more info: http://tinyurl.com/4ycklae";

	@Override
	public String onCall(CommandEvent event) {
		try {
			if (event.getArgs().length == 0)
				return timeZone(0);
			else {
				int zone = Integer.parseInt(event.getArgs()[0]);
				if (zone > -13 && zone < 13)
					return timeZone(zone);
				else 
					return errorMessage;
			}
		}
		catch (NumberFormatException e) {
			return errorMessage;
		}	
	}

	@Override	
	public String getCommand() {
		return "time";
	}

	@Override
	public String getHelp() {
		return  "Get the time in a specific time zone. (GMT by default. Go here for more info: http://tinyurl.com/4ycklae)";
	}
	
	private String timeZone(int zone) {
		Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT" + (zone >= 0 ? "+" + zone : zone)));
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm '(TZ:'Z)", Locale.ENGLISH);
		dateFormat.setCalendar(cal);
		return dateFormat.format(cal.getTime());
	}
}