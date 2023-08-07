package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.MirrorPropertyChange;

public class DateTimePowerQuote extends Style implements PropertyChangeListener {
	private static final long serialVersionUID = 831680775641882176L;
	private JTextArea myQuote;
	private JLabel myTime;
	private JLabel myPower;
	private JLabel myDate;
	private StringBuilder myBuildingQuote;
	
	public DateTimePowerQuote() {
		 super();
		 this.requestFocusInWindow();
		 setLayout(null);
		 setBounds(0, 0, 768, 1010);
		 setup();
		 
		 add(myTime);
		 myTime.setBounds(20, 0, 200, 50);
		 add(myDate);
		 myDate.setBounds(475, 0, 300, 50);
		 JPanel qPanel = new JPanel(new BorderLayout());
		 qPanel.add(myQuote, BorderLayout.CENTER);
		 qPanel.setBackground(Color.BLACK);
		 add(qPanel);
		 qPanel.setBounds(140, 50, 440, 200);
		 
		 
	}
	
	private void setup() { 
		myPower = new JLabel();
		myPower.setIcon(makeIcons("/Images/PowerOn.jpg", 140));
		myPower.setVisible(false);
		myTime = makeLabel(formatIt("h:mm a"), 30);
		myDate = makeLabel(addSpaces(), 28);
		myQuote = new JTextArea("\n\n\tHello(:");
		setUpTextArea();
		myBuildingQuote = new StringBuilder();
		
	}
	
	private void setUpTextArea() {
		myQuote.setForeground(Color.WHITE);
		myQuote.setBackground(Color.BLACK);
		myQuote.setFont(new Font("Bernard MT Condensed", Font.BOLD, 23));
        myQuote.setFocusable(true);
        myQuote.setLineWrap(true);
        myQuote.setWrapStyleWord(true);
        myQuote.setEditable(false);
	}
	
	
	private String addSpaces() {
		String date = formatIt("MMMMMMMMM d, y");
		int spaceAmt = 15 - date.length(); 
		StringBuilder spaces = new StringBuilder();
		while (spaceAmt > 0) {
			spaces.append(" ");
			spaceAmt--;
		}
		spaces.append(date);
		return spaces.toString();
				
	}
	
	private String formatIt(String theFormat) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat(theFormat);
		return format.format(date);
	}

	public void setTime(String theFormat) {
		myTime.setText(formatIt(theFormat));
	}
	
	public void setDate(String theFormat) {
		if (theFormat.equals("MMMMMMMMM d, y")) {
			myDate.setText(addSpaces());
			myDate.setBounds(475, 0, 275, 50);
		} else if (theFormat.equals("MM/dd/yyyy")) {
			myDate.setText(" " + formatIt(theFormat));
			myDate.setBounds(550, 0, 200, 50);
		}
	}
	
	public List<JComponent> getItems() {
		List<JComponent> list = new ArrayList<JComponent>();
		list.add(myPower);
		list.add(myDate);
		list.add(myQuote);
		list.add(myTime);
		return list;
	}
	
	public JLabel getPower() {
		return myPower;
	}
	
	public JTextArea getQuote() {
		return myQuote;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent theEvent) {
		if (MirrorPropertyChange.PROPERTY_QUOTE.equals(theEvent.getPropertyName())) {
			myBuildingQuote.setLength(0);
			String newQuote = (String) theEvent.getNewValue(); 
			myBuildingQuote.append("\n" + newQuote);
		} else if (MirrorPropertyChange.PROPERTY_QUOTE_AUTHOR.equals(theEvent
				                                        .getPropertyName())) {
			String author =  (String) theEvent.getNewValue(); 
			myBuildingQuote.append("\n-" + author);
			myQuote.setText(myBuildingQuote.toString());
		
		}
		
		
		
	}	
}
