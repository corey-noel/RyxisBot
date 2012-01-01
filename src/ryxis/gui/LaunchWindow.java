package ryxis.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ryxis.core.Main;

@SuppressWarnings("serial")
public class LaunchWindow extends JFrame implements ActionListener {
	
	private JPanel contentPane;
	private JTextField txtBotNickname;
	private JTextField txtServerName;
	private JPasswordField txtNickPassword;
	private JButton buttonLaunch;
	private JButton buttonQuit;
	private JTextField txtOwnerNickname;
	
	public LaunchWindow(String botNick, String botPassword, String serverName, String masterNick) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel title = new JLabel("RyxisBot Launch Options");
		title.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		title.setBounds(25, 20, 256, 16);
		contentPane.add(title);
		
		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(25, 43, 98, 16);
		contentPane.add(lblServerName);
		
		JLabel lblBotNickname = new JLabel("Bot Nickname");
		lblBotNickname.setBounds(25, 93, 98, 16);
		contentPane.add(lblBotNickname);
		
		JLabel lblBotNickPassword = new JLabel("Bot Nick Password (Optional)");
		lblBotNickPassword.setBounds(25, 143, 192, 16);
		contentPane.add(lblBotNickPassword);
		
		JLabel lblOwnerNick = new JLabel("Owner Nick");
		lblOwnerNick.setBounds(25, 193, 86, 16);
		contentPane.add(lblOwnerNick);
		
		txtServerName = new JTextField();
		txtServerName.setColumns(10);
		txtServerName.setBounds(25, 59, 400, 28);
		contentPane.add(txtServerName);
		
		txtBotNickname = new JTextField();
		txtBotNickname.setBounds(25, 109, 400, 28);
		contentPane.add(txtBotNickname);
		txtBotNickname.setColumns(10);
		
		txtNickPassword = new JPasswordField();
		txtNickPassword.setBounds(25, 159, 400, 28);
		contentPane.add(txtNickPassword);
		
		txtOwnerNickname = new JTextField();
		txtOwnerNickname.setColumns(10);
		txtOwnerNickname.setBounds(25, 207, 400, 28);
		contentPane.add(txtOwnerNickname);
		
		buttonLaunch = new JButton("Launch");
		buttonLaunch.setBounds(25, 242, 117, 29);
		buttonLaunch.addActionListener(this);
		getRootPane().setDefaultButton(buttonLaunch);
		contentPane.add(buttonLaunch);
		
		buttonQuit = new JButton("Quit");
		buttonQuit.setBounds(308, 242, 117, 29);
		buttonQuit.addActionListener(this);
		contentPane.add(buttonQuit);
		
		if (botNick != null)
			txtBotNickname.setText(botNick);
		
		if (botPassword != null)
			txtNickPassword.setText(botPassword);
		
		if (serverName != null)
			txtServerName.setText(serverName);
		
		if (masterNick != null)
			txtOwnerNickname.setText(masterNick);
		
		validate();
		repaint();
	}
	
	public String getBotNickname() { return txtBotNickname.getText(); }
	public String getMasterNickname() { return txtOwnerNickname.getText(); }
	public String getServerName() { return txtServerName.getText(); }
	public String getBotPassword() { return new String(txtNickPassword.getPassword()); }

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonQuit)) {
			dispose();
			System.out.println("Quitting from launch screen.");
		}
		
		else if (event.getSource().equals(buttonLaunch)) {
			if (getBotNickname() == null || getBotNickname().equals("") || 
				getServerName() == null || getServerName().equals("") || 
				getMasterNickname() == null || getMasterNickname().equals(""))
					JOptionPane.showMessageDialog(this, "You left out necessarry fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
			else {
				dispose();
				Main.start();
			}
		}
	}
}