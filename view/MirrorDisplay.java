package view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import controller.MirrorControl;

import java.awt.Color;

public class MirrorDisplay extends JFrame {
	private static final long serialVersionUID = 1L;
	private DateTimePowerQuote myInfo;
	private WeatherForcast myWeather;
	private Music mySpotifyMusic;
	private MirrorControl myControl;
	
	public MirrorDisplay() {
		super();
		myInfo = new DateTimePowerQuote();
		myWeather = new WeatherForcast();
		myInfo.add(myWeather);
		mySpotifyMusic = new Music();
		mySpotifyMusic.add(myInfo.getPower());
		myInfo.getPower().setBounds(322, 1060, 140, 140);
		myWeather.getForcast().addKeyListener(myControl);
		myControl = new MirrorControl(mySpotifyMusic, myWeather, myInfo);
		initialize();
	}
	
	private void initialize() {
		addListener(myInfo.getItems());
		addListener(mySpotifyMusic.getItems());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setPreferredSize(new Dimension(772, 1380));
	    setBackground(Color.BLACK);
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setResizable(false);
	    pack();
	    add(myInfo);
	    add(mySpotifyMusic);
	    
	}
	
	
	private void addListener(List<JComponent> theComponents) {
		for (JComponent c : theComponents) {
			c.addKeyListener(myControl);
		}
	}
}
