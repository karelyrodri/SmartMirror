package controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.MirrorData;
import model.MirrorPropertyChange;
import model.Spotify;
import view.DateTimePowerQuote;
import view.Music;
import view.WeatherForcast;

public class MirrorControl extends KeyAdapter {
	private MirrorData myModel;
	private DateTimePowerQuote myInfo;
	private WeatherForcast myWeather;
	private Music myMusicPlayer;
	private Spotify mySpotify;
	private Tasks myTasks;
	private boolean myFirstSongPlaying = true;
	private boolean myIsShuffling = false; 
	
	public MirrorControl(Music thePlayer, WeatherForcast theWeather,
			                              DateTimePowerQuote theInfo) {
		myModel = new MirrorData();
		mySpotify = myModel.getSpotify();
		myInfo = theInfo;
		myWeather = theWeather;
		myMusicPlayer = thePlayer;
		myTasks = new Tasks(myModel, myInfo, mySpotify, myMusicPlayer);
		initialize();
	}
	
	private void initialize() {
		myModel.addPropertyChangeListener(MirrorPropertyChange.PROPERTY_QUOTE, myInfo);
		myModel.addPropertyChangeListener(MirrorPropertyChange.PROPERTY_QUOTE_AUTHOR, myInfo);
		myModel.addPropertyChangeListener(MirrorPropertyChange.PROPERTY_WEATHER_CURRENT, myWeather);
		myModel.addPropertyChangeListener(MirrorPropertyChange.PROPERTY_WEATHER_FORCAST, myWeather);
		myModel.sendWeather();
		int secondsPastMin = Calendar.getInstance().get(Calendar.SECOND);
		int delay = 60000 - (secondsPastMin * 1000);
		new Timer().scheduleAtFixedRate(myTasks.minute(), delay, 60000);
		mySpotify.shuffle(myIsShuffling);
		mySpotify.repeat("off");
	}
	
	private void firstTime() {
		mySpotify.changeVolume(60);
		myTasks.setIsPlaying(true);
		myMusicPlayer.setPlaying(true);
		mySpotify.skipTrack();
		new Timer().scheduleAtFixedRate(myTasks.second(), 1000, 1000);
		myTasks.newSong();
		myFirstSongPlaying = false;
	}
	
	@Override
	public void keyPressed(KeyEvent theKE) {
		switch (theKE.getKeyCode()) {
			case KeyEvent.VK_NUMPAD0:
				myModel.sendNewQuote();
				break;
			case KeyEvent.VK_SUBTRACT:
				String tFormat = myTasks.getTimeFormat();
				String t1 = "h:mm a";
				String t2 = "k:mm";
				if (tFormat.equals(t1)) {
					myTasks.setTimeFormat(t2);
					myInfo.setTime(t2);
				} else if (tFormat.equals(t2)) {
					myTasks.setTimeFormat(t1);
					myInfo.setTime(t1);
				} break;
			case KeyEvent.VK_ADD:
				String dFormat = myTasks.getDateFormat();
				String d1 = "MMMMMMMMM d, y";
				String d2 = "MM/dd/yyyy";
				if (dFormat.equals(d1)) {
					myTasks.setDateFormat(d2);
					myInfo.setDate(d2);
				} else if (dFormat.equals(d2)) {
					myTasks.setDateFormat(d1);
					myInfo.setDate(d1);
				} break;
			case KeyEvent.VK_NUMPAD4:
				if (myTasks.isPlaying()) {
					myTasks.setIsPlaying(false);
					mySpotify.pause();
					myMusicPlayer.setPlaying(false);
				} break;
			case KeyEvent.VK_NUMPAD5:
				if (!myTasks.isPlaying()) {
					myTasks.setIsPlaying(true);
					if (myFirstSongPlaying) {
						firstTime();
					} else {
						mySpotify.play();
					}
					myMusicPlayer.setPlaying(true);
				} break;
			case KeyEvent.VK_NUMPAD1: 
				if (myFirstSongPlaying) {
					firstTime();
				} else {
					mySpotify.previousTrack();
					//mySpotify.repeat(myIsRepeating);
					myTasks.newSong();
				}
				break;
			case KeyEvent.VK_NUMPAD2: 
				if (myFirstSongPlaying) {
					firstTime();
				} else {
					mySpotify.skipTrack();
					try {
						Thread.sleep(1300);
					} catch (InterruptedException e) {
						System.out.print("Sleep 8/10 second for skipping song failed!");
						e.printStackTrace();
					}
					myTasks.setIsPlaying(true);
					myMusicPlayer.setPlaying(true);
					//mySpotify.repeat(myIsRepeating);
					myTasks.newSong();
				}
				break;
			case KeyEvent.VK_ENTER:
				JPanel forcast = myWeather.getForcast();
				boolean isVisible = forcast.isVisible();
				forcast.setVisible(!isVisible);
				break;
			case KeyEvent.VK_NUMPAD7:
				myIsShuffling = !myIsShuffling;
				myMusicPlayer.setShuffleIcon(myIsShuffling);
				mySpotify.shuffle(myIsShuffling);
				break;
			case KeyEvent.VK_NUMPAD8:
				myTasks.setIsRepeating(myTasks.isRepeating().equals("off") 
						                                  ? "track" : "off");
				String repeating = myTasks.isRepeating();
				myMusicPlayer.setRepeatIcon(repeating);
				mySpotify.repeat(repeating);
				break;
			case KeyEvent.VK_NUMPAD9: //up
				int vol = myMusicPlayer.getCurrentVol();
				if (vol < 100) {
					vol += 10;
					mySpotify.changeVolume(vol);
					myMusicPlayer.setVolume(vol);
				} break;
			case KeyEvent.VK_NUMPAD6: // down
				int volume = myMusicPlayer.getCurrentVol();
				if (volume >= 10) {
					volume -= 10;
					mySpotify.changeVolume(volume);
					myMusicPlayer.setVolume(volume);
				} break;
			case KeyEvent.VK_NUMPAD3: //mute
				mySpotify.changeVolume(0);
				myMusicPlayer.setVolume(0);
				break;
			case KeyEvent.VK_DECIMAL: 
				JLabel power = myInfo.getPower();
				boolean hasPower = power.isVisible();
				power.setVisible(!hasPower);
				myMusicPlayer.setInvisible(hasPower);
				myWeather.setVisible(hasPower);
				myInfo.getQuote().setVisible(hasPower);
				break;
		}
	}

}
