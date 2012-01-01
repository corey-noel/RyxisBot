package ryxis.core;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class FilteredListener extends ListenerAdapter<RyxisBot> {
	
	@Override
	public void onMessage(MessageEvent<RyxisBot> event) {
		if (!event.getBot().blockList.contains(event.getUser()))
			onFilteredMessage(event);
	}
	
	@Override
	public void onPrivateMessage(PrivateMessageEvent<RyxisBot> event) {
		if (!event.getBot().blockList.contains(event.getUser()))
			onFilteredPrivateMessage(event);
	}
	
	@Override
	public void onAction(ActionEvent<RyxisBot> event) {
		if (!event.getBot().blockList.contains(event.getUser()))
			onFilteredAction(event);
	}
	
	@Override
	public void onInvite(InviteEvent<RyxisBot> event) {
		if (!event.getBot().blockList.contains(event.getBot().getUser(event.getUser())))
			onFilteredInvite(event);
	}
	
	public void onFilteredMessage(MessageEvent<RyxisBot> event) {}
	
	public void onFilteredPrivateMessage(PrivateMessageEvent<RyxisBot> event) {}	
	
	public void onFilteredAction(ActionEvent<RyxisBot> event) {}
	
	public void onFilteredInvite(InviteEvent<RyxisBot> event) {}
}