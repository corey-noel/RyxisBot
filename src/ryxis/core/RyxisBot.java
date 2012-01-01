package ryxis.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLSocketFactory;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.managers.ListenerManager;
import ryxis.command.*;
import ryxis.command.core.*;
import ryxis.database.*;

public class RyxisBot extends PircBotX {
	
	public final Speech speech;
	public final BlockList blockList;
	public final RoomHandler roomHandler;
	public final Reporter reporter;
	public final Master master;
	public final CommandHandler commandHandler;
	public final LinkAnalyzer linkAnalyzer;
	public final AutoResponder autoResponder;
	
	public final Database database; 
	public final DatabaseUserHandler databaseUserHandler; 
	
	// TODO arrays
	public final Set<BaseCommand> commands;
	public final Set<DatabaseAccessor> accessors;
	
	public RyxisBot(String botNick, String masterNick, String serverName) {
		autoResponder = new AutoResponder();
		speech = new Speech(this);
		blockList = new BlockList(this);
		roomHandler = new RoomHandler(this);
		reporter = new Reporter(this);
		commandHandler = new CommandHandler();
		master = new Master(this);
		linkAnalyzer = new LinkAnalyzer();
		database = new Database(this);
		databaseUserHandler = new DatabaseUserHandler(serverName);
		
		setAutoNickChange(true);
		setVerbose(true);
		setNick(botNick);
		server = serverName;
		
		master.setMaster(masterNick);
				
		ListenerManager<? extends PircBotX> lm = getListenerManager();
		lm.addListener(commandHandler);
		lm.addListener(roomHandler);
		lm.addListener(autoResponder);
		lm.addListener(reporter);
		lm.addListener(master);

		
		BaseCommand[] commandArray = { new CommandCalculator(), new CommandCyoots(),
				new CommandDaysUntil(), new CommandDescription(), new CommandDispatch(),
				new CommandEightBall(), new CommandFlip(), new CommandFortune(), new CommandHelp(),
				new CommandHi(), new CommandIgnore(), new CommandJoin(), new CommandLeave(),
				new CommandQuestion(), new CommandQuit(), new CommandReport(), new CommandRoll(),
				new CommandSafeword(), new CommandSilence(), new CommandSomeone(),
				new CommandSummary(), new CommandTime(), new CommandLevel() };
		
		DatabaseAccessor[] accessorArray = { databaseUserHandler };
		
		commands = Collections.unmodifiableSet(new HashSet<BaseCommand>(Arrays.asList(commandArray)));
		accessors = Collections.unmodifiableSet(new HashSet<DatabaseAccessor>(Arrays.asList(accessorArray)));
	}
	
	@Override public void joinChannel(String channel) { super.joinChannel((channel.startsWith("#") ? channel : "#" + channel)); }
	
	@Override public void sendMessage(Channel chan, String message) { speech.sendMessage(chan, message); }
	@Override public void sendMessage(User user, String message) { speech.sendMessage(user, message); }
	@Override public void sendAction(Channel chan, String message) { speech.sendAction(chan, message); }
	@Override public void sendAction(User user, String message) { speech.sendAction(user, message); }
	
	public void sendRawMessage(Channel chan, String message) { super.sendMessage(chan, message); }
	public void sendRawMessage(User user, String message) { super.sendMessage(user, message); }
	public void sendRawAction(Channel chan, String message) { super.sendAction(chan, message); }
	public void sendRawAction(User user, String message) { super.sendAction(user, message); }
	
	public void connect(String serverName, boolean SSL, String botPass, String[] roomsToJoin) {
		output("Connecting to " + serverName + (SSL ? " with an SSL connection." : "."));
		roomHandler.setChannelsToJoin(roomsToJoin);
		
		try {
			if (SSL) 
				if (botPass == null)
					super.connect(serverName, 6697, SSLSocketFactory.getDefault());
				else
					super.connect(serverName, 6697, botPass, SSLSocketFactory.getDefault());
			else
				if (botPass == null)
					super.connect(serverName);
				else
					super.connect(serverName, 6667, botPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		disconnect(master.getDisconnectReason());
	}
	
	public void disconnect(String reason) {
		output("Disconnecting from the server at " + getServer() + ".");
		if (reason == null)
			super.quitServer();
		else
			super.quitServer(reason);
	}
	
	// TODO um why
	public static void output(String message) {
		System.out.println(System.currentTimeMillis() + " [RyxisBot] " + message);
	}
}