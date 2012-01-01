package ryxis.core;

import java.util.ArrayList;
import org.pircbotx.Channel;
import org.pircbotx.UserSnapshot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import ryxis.tools.StringTools;

public class RoomHandler extends ListenerAdapter<RyxisBot> {
	
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private final RyxisBot bot;
	private String[] channelsToJoin;
	
	public RoomHandler(RyxisBot b) {
		bot = b;
	}
	
	@Override
	public void onMessage(MessageEvent<RyxisBot> event) {
		getRoom(event.getChannel()).updateRoom(event.getUser(), event.getMessage(), event);
	}
	
	@Override 
	public void onJoin(JoinEvent<RyxisBot> event) {
		if(event.getUser().equals(event.getBot().getUserBot()))
			rooms.add(new Room(event.getChannel(), bot));
		getRoom(event.getChannel()).updateRoom(event.getUser(), null, event);
	}
	
	@Override
	public void onAction(ActionEvent<RyxisBot> event) {
		Channel chan = event.getChannel();
			if (chan != null)
				getRoom(chan).updateRoom(event.getUser(), event.getMessage(), event);
	}
	
	@Override
	public void onQuit(QuitEvent<RyxisBot> event) {
		UserSnapshot us = event.getUser();
		for (Channel chan : us.getChannels())
			getRoom(chan).updateRoom(event.getUser(), null, event);
	}
	
	@Override
	public void onPart(PartEvent<RyxisBot> event) {
		getRoom(event.getChannel()).updateRoom(event.getUser(), null, event);
	}
	
	@Override
	public void onKick(KickEvent<RyxisBot> event) {
		getRoom(event.getChannel()).updateRoom(event.getRecipient(), null, event);
	}
	
	@Override
	public void onConnect(ConnectEvent<RyxisBot> event) {
		if (channelsToJoin != null)
			for (String channel : channelsToJoin)
				bot.joinChannel(StringTools.parseChannel(channel));
	}
	
	@Override
	public void onDisconnect(DisconnectEvent<RyxisBot> event) {
		rooms.clear();
	}
	
	public Room getRoom(Channel chan) {
		for (Room room : rooms) 
			if (room.channel.equals(chan))
				return room;
		return null;
	}

	public void setChannelsToJoin(String[] channels) {
		channelsToJoin = channels;
	}
}
