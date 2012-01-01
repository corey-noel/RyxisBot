package ryxis.command;

import java.util.Calendar;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandDaysUntil extends BaseCommand {

	@Override
	public String getCommand() {
		return "days";
	}

	@Override
	public String getHelp() {
		return "Get the days left until a holiday or date!";
	}

	@Override
	public String onCall(CommandEvent event) {
		Holiday holiday = Holiday.parseHoliday(event.getMessageSansCommand().trim());
		if (holiday != null)
			return "There are " + holiday.daysUntil() + " days until " + holiday.toString() + "!";
		else
			return "I don't know of a holiday called " + event.getMessageSansCommand() + "...";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return "Enter the name of a holiday. I'll tell you how many days are left until it!";
	}
		
	public static enum Holiday {
		NEW_YEAR (1, 1, 0, 0, 0, HolidayType.ON_DATE, new String[] { "New Year's Day", "new years day", "new years", "new year" }),
		ORTHODOX_CHRISTMAS (1, 7, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Orthodox Christmas", "orthodox xmas", "orthodox x mas", "orthodox x-mas"}),
		ORTHODOX_NEW_YEAR (1, 14, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Orthodox New Year", "orthodox new years"}),
		MARTIN_LUTHER_KING_DAY(1, 0, 3, Calendar.MONDAY, 0, HolidayType.ON_DAY_OF_WEEK, new String[] {"Marthin Luther King Day", "mlk day"}),
		GROUNDHOG_DAY(2, 2, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Groundhog Day"}),
		//CHINESE_NEW_YEAR() "The second new moon after the winter solstice." Are you shitting me?
		LINCOLNS_BIRTHDAY(2, 12, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Lincoln's Birthday", "Lincolns Birthday"}),
		//MARDI_GRAS() IS IT THAT HARD TO HAVE REGULAR DATES FOR HOLIDAYS  (Day before lent)
		//ASH_WEDNESDAY() SEEERIOUSLY (First day of lent)
		//When is Lent, you might ask?
		//40 days before Easter. 
		//When is Easter, you might ask?
		//The first Sunday after the first full moon after the March Equinox.
		//Why am I alive, you might ask?
		VALENTINES_DAY(2, 14, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Valentine's Day", "valentines day"}),
		PRESIDENTS_DAY(2, 0, 3, Calendar.MONDAY, 0, HolidayType.ON_DAY_OF_WEEK, new String[] {"President's Day", "presidents day"}),
		
		// month, day of month, week of month, day of week, year, type, aliases
		
		CHRISTMAS (12, 25, 0, 0, 0, HolidayType.ON_DATE, new String[] {"Christmas", "xmas", "x-mas", "x mas"});

		private final static int MILLIS_IN_DAY = 86400000;
		private final int MONTH;
		private final int DAY_OF_MONTH;
		private final int WEEK_OF_MONTH;
		private final int DAY_OF_WEEK;
		private final int YEAR; 
		private final HolidayType DATE_TYPE;
		private final String[] ALIASES;
		
		private Holiday(int a, int b, int c, int d, int e, HolidayType f, String[] g) {
			this.MONTH = a;
			this.DAY_OF_MONTH = b;
			this.WEEK_OF_MONTH = c;
			this.DAY_OF_WEEK = d;
			this.YEAR = e;
			this.DATE_TYPE = f;
			this.ALIASES = g;
		}	
		
		public long daysUntil() {
			Calendar current = Calendar.getInstance();
			Calendar holiday = Calendar.getInstance();
			switch (DATE_TYPE) {
			case ON_DATE:
				holiday.set(current.get(Calendar.YEAR), this.MONTH - 1, this.DAY_OF_MONTH);
				if (holiday.before(current)) 
					holiday.set(Calendar.YEAR, current.get(Calendar.YEAR) + 1);
				break;
			case ON_DAY_OF_WEEK:
				holiday.set(current.get(Calendar.YEAR), this.MONTH, -1);
				holiday.set(Calendar.DAY_OF_WEEK_IN_MONTH, this.WEEK_OF_MONTH);
				holiday.set(Calendar.DAY_OF_WEEK, this.DAY_OF_WEEK);
				if (holiday.before(current)) 
					holiday.set(Calendar.YEAR, current.get(Calendar.YEAR) + 1); //TODO I think this breaks it. Simply adding a year will fuck stuff up. 
				break;
			case ONE_TIME_EVENT:
				holiday.set(this.YEAR, this.MONTH, this.DAY_OF_MONTH);
				break;
			}
			return (holiday.getTimeInMillis() - current.getTimeInMillis()) / MILLIS_IN_DAY;
		}
		
		@Override
		public String toString() {
			return ALIASES[0];
		}
		
		public static Holiday parseHoliday(String input) {
			for (Holiday holiday : Holiday.values())
				for (String alias : holiday.ALIASES)
					if (alias.toLowerCase().equals(input.toLowerCase()))
							return holiday;
			return null;
		}

		private static enum HolidayType {
			ON_DATE,
			ON_DAY_OF_WEEK,
			ONE_TIME_EVENT;
		}
	}
}
