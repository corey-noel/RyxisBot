package ryxis.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import ryxis.core.Main;

@SuppressWarnings("serial")
public class JoinChannelWindow extends JDialog implements ActionListener {
	
	private JButton buttonOk;
	private JButton buttonCancel;
	private JTextField formattedTextField;
	
	public JoinChannelWindow(JFrame parent) {
		super(parent, "Join Channel", true);
		
		setBounds(500, 200, 450, 110);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		formattedTextField = new JTextField();
		formattedTextField.setColumns(30);
		formattedTextField.setDocument(new FormattedDocument());
		contentPanel.add(formattedTextField);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		buttonOk = new JButton("Join");
		buttonOk.addActionListener(this);
		buttonPane.add(buttonOk);
		getRootPane().setDefaultButton(buttonOk);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		buttonPane.add(buttonCancel);
		
		validate();
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonOk))
			if (Main.getBot() != null)
				Main.getBot().joinChannel(formattedTextField.getText());
			else
				System.out.println("There was an issue joining the room.");
		dispose();
	}
	
	private class FormattedDocument extends PlainDocument {
		@Override
		public void insertString(int offset, String s, AttributeSet attributes) throws BadLocationException {
		    super.insertString(offset, s.replaceAll("\\s", ""), attributes);
		}
		
		@Override
		public void replace(int offset, int length, String s, AttributeSet attributes) throws BadLocationException {
		    super.replace(offset, length, s.replaceAll("\\s", ""), attributes);
		}
	}
}