package view;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Music extends Style {
	private static final long serialVersionUID = -4117992604807927082L;
	private JButton myPausePlay;
	private JButton mySkip;
	private JButton myPrev;
	private JButton myShuffle;
	private JButton myRepeat;
	private JLabel myVolume;
	private JLabel mySpotifyLogo;
	private JLabel myTimeElapsed;
	private JLabel myTrackLength;
	private JLabel myTrackNameArtist;
	private JLabel myTrackImage;
	private JProgressBar myTimeBar;
	private JProgressBar myVolumeBar;
	
	public Music() {
		super();
		this.requestFocusInWindow();
		setVisible(true);
		setLayout(null);
		setUp();
		setBounds(0, 1010, 768, 220);
		add();
		
	}
	
	private void add() {
		JPanel buttons = buildButtons();
		add(buttons);
		buttons.setBounds(251, 1165, 360, 40);
		add(mySpotifyLogo);
		mySpotifyLogo.setBounds(30, 1045, 200, 60);
		add(myTrackImage);
		myTrackImage.setBounds(10, 1115, 150, 150);
		add(myTrackNameArtist);
		myTrackNameArtist.setBounds(175, 1115, 550, 40);
		add(myTimeElapsed);
		myTimeElapsed.setBounds(209, 1230, 150, 30);
		add(myTrackLength);
		myTrackLength.setBounds(611, 1230, 150, 30);
		add(myTimeBar);
		myTimeBar.setBounds(270, 1240, 330, 15);
		add(myVolume);
		myVolume.setBounds(635, 1175, 30, 30);
		add(myVolumeBar);
		myVolumeBar.setBounds(670, 1187, 65, 10);
		
	}
	
	private void setUp() {
		myTrackImage = new JLabel(makeIcons("/Images/SpotifyIcon.png", 100));
		mySpotifyLogo = new JLabel(makeIcons("/Images/SpotifyLogo.png", 200));
		myTrackNameArtist = makeLabel("", 25);
		myShuffle = makeButton("/Images/shuffleOff.jpg", 50);              
		myPrev = makeButton("/Images/prev.png", 45);
		myPausePlay = makeButton("/Images/play.png", 75); 
		mySkip = makeButton("/Images/next.png", 45);
		myRepeat = makeButton("/Images/off.jpg", 50);
		myTimeElapsed = makeLabel("0:00", 20);
		myTrackLength = makeLabel("0:00", 20);
		myVolume = new JLabel(makeIcons("/Images/volHalf.jpg", 40));
		myTimeBar = makeSlider();
		myVolumeBar = makeSlider();
		myVolumeBar.setMaximum(100);
		myVolumeBar.setValue(60);
	}
	
	public void setVolume(int theVolume) {
		myVolumeBar.setValue(theVolume);
		if (theVolume == 0) {
			myVolume.setIcon(makeIcons("/Images/volOff.jpg", 40));
		} else if (theVolume == 100) {
			myVolume.setIcon(makeIcons("/Images/volFull.jpg", 40));
		} else if (theVolume < 50 && theVolume >= 10) {
			myVolume.setIcon(makeIcons("/Images/volLow.jpg", 40));
		} else if (theVolume >= 50 && theVolume < 100) {
			myVolume.setIcon(makeIcons("/Images/volHalf.jpg", 40));
		}
	}
	
	public int getCurrentVol() {
		return myVolumeBar.getValue();
	}
		
	private JPanel buildButtons() {
		JPanel buttons = new JPanel(new GridLayout(1,5)); 
		buttons.setVisible(true);
		buttons.setBackground(Color.BLACK);
    	buttons.add(myShuffle);
    	buttons.add(myPrev);
		buttons.add(myPausePlay);
        buttons.add(mySkip);
    	buttons.add(myRepeat);
    	return buttons;
   
	}
	
	public void setTrackBarValue(int theValue) {
		myTimeBar.setValue(theValue);
	}
	
	public void setTimeElapsed(String theTime) {
		myTimeElapsed.setText(theTime);
	}
	
	public String getTimeElapsed() {
		return myTimeElapsed.getText();
	}
	
	public String getTrackLen() {
		return myTrackLength.getText();
	}
	
	public void updateTrackInfo(String theName, ImageIcon theImage, String[] theArtists,
			                                     String theSongLen, int theMaxVal) {
		StringBuilder songInfo = new StringBuilder(theName + " - ");
		int numOfArtists = theArtists.length;
		for (int i = 0; i < numOfArtists; i++) {
			String addArtist = i != numOfArtists - 1 
					         ? theArtists[i] + ", " : theArtists[i];
			songInfo.append(addArtist);
		}
		myTrackNameArtist.setText(songInfo.toString());
		myTrackLength.setText(theSongLen);
		myTrackImage.setIcon(theImage);
		myTimeBar.setMaximum(theMaxVal);
		
	}
	
	public void setRepeatIcon(String thePath) {
		myRepeat.setIcon(makeIcons("/Images/" + thePath + ".jpg", 50));
	} 
	
	public void setShuffleIcon(boolean theShuffling) {
		String path = theShuffling  ? "/Images/shuffleOn.jpg" 
				                    : "/Images/shuffleOff.jpg";
		myShuffle.setIcon(makeIcons(path, 50));
	}
	
	public void setPlaying(boolean theIsPlaying) {
		String path = theIsPlaying ? "/Images/pause.png"
				                   : "/Images/play.png";
		myPausePlay.setIcon(makeIcons(path, 75));		                	   
				
	}
	public JLabel getTrackName() {
		return myTrackNameArtist;
	}
	
	public List<JComponent> getItems() {
		List<JComponent> list = new ArrayList<JComponent>();
		list.add(myPausePlay);
		list.add(mySkip);
		list.add(myPrev);
		list.add(myShuffle);
		list.add(myRepeat);
		list.add(myVolume);
		list.add(myTimeElapsed);
		list.add(myTrackLength);
		list.add(myTrackNameArtist);
		list.add(myTrackImage);
		list.add(myTimeBar);
		list.add(myVolumeBar);
		return list;
	}
	
	public void setInvisible(boolean theVisibility) {
		for (JComponent i : getItems()) {
			i.setVisible(theVisibility);
		}
		mySpotifyLogo.setVisible(theVisibility);
	}
}
