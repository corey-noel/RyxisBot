package ryxis.command.core;

public abstract class BaseCommand {
	
	public abstract String onCall(CommandEvent event);	
	public abstract String getCommand();	
	public abstract String getHelp();
	
	public boolean isMasterOnly() {return false;}	
	public boolean isHidden() {return false;}

	public boolean respondWithAction() {return false;}
	
	public int getMinArgs() {return 0;}
	public String getUnderArgsMessage() {return "You did not enter enuogh arguments to use that command.";}
	
	@Override
	public final String toString() {return this.getCommand();}
}