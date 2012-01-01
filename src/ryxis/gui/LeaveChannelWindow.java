package ryxis.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.pircbotx.Channel;
import ryxis.core.RyxisBot;
import ryxis.core.Main;

@SuppressWarnings("serial")
public class LeaveChannelWindow extends JDialog implements ActionListener {
	
	private JComboBox channelList;
	private JButton buttonLeave;
	private JButton buttonCancel;
	
	public LeaveChannelWindow(JFrame parent) {
		super(parent, "Leave Channel", true);
		
		setBounds(500, 200, 360, 110);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		channelList = new JComboBox();
		channelList.setBounds(0, 0, 354, 50);
		contentPanel.add(channelList);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		buttonLeave = new JButton("Leave");
		buttonPane.add(buttonLeave);
		buttonLeave.addActionListener(this);
		getRootPane().setDefaultButton(buttonLeave);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		buttonPane.add(buttonCancel);
		
		validate();
		repaint();
		
		if (Main.getBot() != null)
			for (Channel chan : Main.getBot().getChannels())
				channelList.addItem(chan.getName());
		else 
			dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonLeave)) {
			RyxisBot bot = Main.getBot();
			String chan = (String) channelList.getSelectedItem();
			
			if (bot == null)
				System.out.println("Lost reference to the bot...");
			else if (chan == null)
				System.out.println("Lost reference to channel...");
			else
				bot.partChannel(bot.getChannel(chan));
		}
		dispose();
	}
}
