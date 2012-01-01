package ryxis.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import ryxis.core.Main;

@SuppressWarnings("serial")
public class ConsoleWindow extends JFrame implements ActionListener {
	
	private MessageConsole messageConsole;
	private JTextPane txtPaneConsole;
	private JButton buttonJoinChannel;
	private JButton buttonLeaveChannel;
	private JButton buttonQuit;
	
	public ConsoleWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 480);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 780, 400);
		contentPane.add(scrollPane);
		
		txtPaneConsole = new JTextPane();
		txtPaneConsole.setForeground(Color.BLACK);
		txtPaneConsole.setFont(new Font("Courier New", Font.PLAIN, 13));
		txtPaneConsole.setBackground(Color.WHITE);
		txtPaneConsole.setEditable(false);
		scrollPane.setViewportView(txtPaneConsole);
		
		buttonJoinChannel = new JButton("Join Channel");
		buttonJoinChannel.setBounds(10, 422, 117, 29);
		buttonJoinChannel.addActionListener(this);
		contentPane.add(buttonJoinChannel);
		
		buttonLeaveChannel = new JButton("Leave Channel");
		buttonLeaveChannel.setBounds(139, 422, 117, 29);
		buttonLeaveChannel.addActionListener(this);
		contentPane.add(buttonLeaveChannel);
		
		buttonQuit = new JButton("Quit Server");
		buttonQuit.setBounds(677, 422, 117, 29);
		buttonQuit.addActionListener(this);
		contentPane.add(buttonQuit);
		
		messageConsole = new MessageConsole(txtPaneConsole);
		messageConsole.setMessageLines(200);
		messageConsole.redirectOut();
		messageConsole.redirectErr(Color.RED, null);

		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonQuit)) {
			System.out.println("Shutting down from console window");
			dispose();
			Main.getBot().disconnect();
		} 
		
		else if (event.getSource().equals(buttonLeaveChannel)) {
			if (Main.getBot().getChannels().size() > 0)
				new LeaveChannelWindow(this).setVisible(true);
			else
				JOptionPane.showMessageDialog(this, "You aren't in any channels.", "Error Leaving Channel", JOptionPane.ERROR_MESSAGE);
		}
		
		else if (event.getSource().equals(buttonJoinChannel)) {
			new JoinChannelWindow(this).setVisible(true);
		}
	}
}
