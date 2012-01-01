package ryxis.command;

import java.util.ArrayList;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.database.DatabaseUserHandler;

public class CommandDispatch extends BaseCommand {
	
	private final static String USAGE = "Usage: [grant|revoke|list|animalName] [nick] [animalName]";
	
	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs().length < 1)
			return USAGE;
		else if (event.getArgs()[0].equalsIgnoreCase("grant")) {
			if (event.getArgs().length < 3)
				return USAGE;
			else if (!event.getUser().equals(event.getBot().master.getMaster()))
				return event.getBot().master.getMasterReason();
			return grant(event.getArgs()[1], event.getArgs()[2], event.getBot().databaseUserHandler);	
		} else if (event.getArgs()[0].equalsIgnoreCase("revoke")) {
			if (event.getArgs().length < 3)
				return USAGE;
			else if (!event.getUser().equals(event.getBot().master.getMaster()))
				return event.getBot().master.getMasterReason();
			return revoke(event.getArgs()[1], event.getArgs()[2], event.getBot().databaseUserHandler);
		} else if (event.getArgs()[0].equalsIgnoreCase("list")) {
			if (event.getArgs().length < 2)
				return list(event.getUser().getNick(), event.getBot().databaseUserHandler);
			else 
				return list(event.getArgs()[1], event.getBot().databaseUserHandler);
		} else {
			return dispatch(event.getUser().getNick(), event.getArgs()[0], event.getBot().databaseUserHandler);
		}
	}

	@Override
	public String getCommand() {
		return "dispatch";
	}

	@Override
	public String getHelp() {
		return "Because you probably needed more ASCII animals in your life. " + USAGE;
	}
	
	private String grant(String nick, String animalName, DatabaseUserHandler db) {
		DispatchAnimal animal = DispatchAnimal.getAnimal(animalName);
		if (animal == null)
			return "I don't know of an animal called " + animalName + "!";
		boolean[] dispatchArray = db.getUserDispatch(nick);
		dispatchArray[animal.identifier] = true;
		db.setUserDispatch(nick, dispatchArray);
		return nick + " was granted " + animalName + "!";
	}
	
	private String revoke(String nick, String animalName, DatabaseUserHandler db) {
		DispatchAnimal animal = DispatchAnimal.getAnimal(animalName);
		if (animal == null)
			return "I don't know of an animal called " + animalName + "!";
		boolean[] dispatchArray = db.getUserDispatch(nick);
		dispatchArray[animal.identifier] = false;
		db.setUserDispatch(nick,dispatchArray);
		return nick + " no longer has " + animalName + ". :<";
	}
	
	private String list(String nick, DatabaseUserHandler db) {
		if (!db.userExists(nick))
			return nick + " doesn't have access to any animals! D:";
		boolean[] userDispatch = db.getUserDispatch(nick);
		ArrayList<DispatchAnimal> animals = new ArrayList<DispatchAnimal>();
		for (int i = 0; i < userDispatch.length; i++) {
			if (userDispatch[i]) {
				DispatchAnimal animal = DispatchAnimal.getAnimal(i);
				if (animal != null)
					animals.add(animal);
			}
		}
		if (animals.size() == 0)
			return nick + " doesn't have access to any animals! D:";
		else {
			String list = nick + " has: ";
			for (DispatchAnimal animal : animals) {
				list += animal.name + ", ";
			}
			list = list.substring(0, list.length() - 2) + "!";
			return list;
		}
	}
	
	private String dispatch(String nick, String animalName, DatabaseUserHandler db) {
		DispatchAnimal targetAnimal = DispatchAnimal.getAnimal(animalName);
		if (targetAnimal == null)
			return "I don't know of an animal called " + animalName + ". :<";
		boolean[] userDispatch = db.getUserDispatch(nick);
		for (int i = 0; i < userDispatch.length; i++) {
			if (userDispatch[i]) {
				DispatchAnimal animal = DispatchAnimal.getAnimal(i);
				if (animal != null && animal.equals(targetAnimal))
					return animal.print;
			}
		}
		return "You don't have access to " + animalName + ". :<";
	}
	
	public static enum DispatchAnimal {
		BUNNY("bunnies", 0, 
				"(\\___/)\n" +
				"(='.'=)\n" +
				"(\")_(\")"),
		DUCK("ducks", 1, 
				"   _          _          _\n" +
				" >(')____,  >(')____,  >(')____,\n" +
				"   (` =~~/    (` =~~/    (` =~~/ \n" +
				"~^~^`---'~^~^~^`---'~^~^~^`---'~^~^~"),
		MOUSE("mice", 2, "~~~(___C'>"),
		SHARK("sharks", 3, 
				"\\_____)\\_____\n" +
				"/--v____ __`<\n" +
				"        )/"),
		PUPPY("puppies", 4, 
				"  _.-.\n" +
				"'( ^{_}    (\n" +
				"  `~\\`-----'\\\n" +
				"     )_)---)_)"),
		OWL("owls", 5, 
				" /\\_/\\\n" +
				"((@v@))\n" +
				"():::()\n" +
				" VV-VV");

		  
		
		public final String name;
		public final String print;
		public final int identifier;
		
		DispatchAnimal(String n, int id, String p) {
			name = n;
			identifier = id;
			print = p;
		}
		
		public static DispatchAnimal getAnimal(String animalName) {
			for (DispatchAnimal animal : DispatchAnimal.values())
				if (animal.name.equals(animalName))
					return animal;
			return null;
		}
		
		public static DispatchAnimal getAnimal(int animalId) {
			for (DispatchAnimal animal : DispatchAnimal.values())
				if (animal.identifier == animalId)
					return animal;
			return null;
		}
	}
}