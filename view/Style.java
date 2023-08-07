 package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public abstract class Style extends JPanel { 
	
	private static final long serialVersionUID = 3083633810436602617L;

	public Style() {
		super();
		setBackground(Color.BLACK);
		setVisible(true);
		
	}
	
	protected JLabel makeLabel(String theLabel, int theFontSize) {
		JLabel label = new JLabel(theLabel);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.BLACK);
		label.setFont(new Font("Bernard MT Condensed", Font.BOLD, theFontSize));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setFocusable(true);
		return label;
	}
	
	public ImageIcon makeIcons(String thePath, int theSize) {
		final ImageIcon icon = new ImageIcon(Music.class.getResource(thePath));                
        final Image smallImage = icon.getImage().getScaledInstance(theSize, -1, java.awt.Image.SCALE_SMOOTH); 
        return new ImageIcon(smallImage);
	}
	
	protected JButton makeButton(String thePath, int theSize) {
        JButton button = new JButton(makeIcons(thePath, theSize));
        button.setEnabled(true);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFocusable(true);
        button.setBorderPainted(false);
        return button;
	} 
	
	protected JProgressBar makeSlider() {
		JProgressBar bar = new JProgressBar();
		bar.setBackground(Color.GRAY);
		bar.setForeground(Color.WHITE);
		bar.setEnabled(false);
		bar.setFocusable(true);
		bar.setOpaque(true);
		return bar;
	}
	
	
}
