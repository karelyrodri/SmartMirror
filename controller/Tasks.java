package controller;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import org.json.JSONException;

import model.MirrorData;
import model.Spotify;
import model.Track;
import view.DateTimePowerQuote;
import view.Music;

public class Tasks {
	private DateTimePowerQuote myInfo;
	private MirrorData myModel;
	private Music myMusicPlayer;
	private Spotify mySpotify;
	private String myDateFormat = "MMMMMMMMM d, y";
	private String myTimeFormat = "h:mm a";
	private String myIsRepeating = "off";
	private String myPreviousSong = null;
	private boolean myIsPlaying = false;
	private int mySongTimeElapsed;
	private int myDayNum; 
	
	
	public Tasks (MirrorData theModel, DateTimePowerQuote theInfo,
		            Spotify theSpotify, Music thePlayer ) {
		myModel = theModel;
		myInfo = theInfo;
		mySpotify = theSpotify;
		myMusicPlayer = thePlayer;
		myDayNum = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		createTimers();
		
	}
	
	public void createTimers() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() { myModel.sendNewQuote();}}, 20000);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() { myModel.sendWeather();}}, 3600000, 3600000);
	}
	
	public TimerTask songTask() {
		TimerTask checkSong = new TimerTask() {
			@Override
			public void run() {
				String supposedUpdated = myMusicPlayer.getTrackName().getText();
				if (myPreviousSong.equals(supposedUpdated)) {
					pullSongData();
					mySongTimeElapsed -= 2000;
				}}};
		return checkSong;
	}
	
	public TimerTask minute() {
		TimerTask everyMinute = new TimerTask() {
			@Override
			public void run() {
				myInfo.setTime(myTimeFormat);
				mySpotify.setExpirationSet(60);
				
				int checkDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				if (myDayNum != checkDate) {
					myDayNum = checkDate;
					myInfo.setDate(myDateFormat);
					myModel.sendWeather();
				}}};
		return everyMinute;
	}
	
	public TimerTask second() {
		TimerTask everySecond = new TimerTask() {
			@Override
			public void run() {
				if (myIsPlaying) {
					if (myMusicPlayer.getTrackLen().equals
					   (myMusicPlayer.getTimeElapsed())) {
						///////////// Raspotify does not support repeating track:(
						if (myIsRepeating.equals("track")) {
							myMusicPlayer.setTimeElapsed("0:00");
							mySpotify.previousTrack();
							mySongTimeElapsed = -1000;
					    /////////////
						} else {
							myPreviousSong = myMusicPlayer.getTrackName().getText();
							newSong();
							new Timer().schedule(songTask(), 3000);
						}
					}
					mySongTimeElapsed += 1000;
					myMusicPlayer.setTimeElapsed(calculateTime (mySongTimeElapsed));
					myMusicPlayer.setTrackBarValue(mySongTimeElapsed);
				}}}; 
		return everySecond;
	}
	
	public void newSong() {
		myMusicPlayer.setTimeElapsed("0:00");
		myMusicPlayer.setTrackBarValue(0);
		mySongTimeElapsed = 0;
		pullSongData(); 
		
	}

	private void pullSongData() {
		try {
			mySpotify.playingCurrenty();
		} catch (JSONException e) {
			System.out.println("Could not retrieve next song!");
		}
		Track track = mySpotify.getCurrentTrack();
		int duration = track.getDurationMS();
		String trackLen = calculateTime(duration);
		String name = track.getTrackName();
		String[] artists = track.getArtists();
		ImageIcon image = track.getImage();
		myMusicPlayer.updateTrackInfo(name, image, artists, trackLen, duration);
	}
	
	private String calculateTime(int theDurationMS) {
		int minute = (theDurationMS / 1000)  / 60;
		int second = (int)((theDurationMS / 1000) % 60);
		StringBuilder timeFormat = new StringBuilder(minute + ":");
		timeFormat.append(second > 9 ? String.valueOf(second) : "0" + second);
		return timeFormat.toString();
	}
	
	public String getDateFormat() {
		return myDateFormat;
	}

	public String getTimeFormat() {
		return myTimeFormat;
	}

	public String isRepeating() {
		return myIsRepeating;
	}

	public boolean isPlaying() {
		return myIsPlaying;
	}
	public void setDateFormat(String theFormat) {
		myDateFormat = theFormat;
	}

	public void setTimeFormat(String theFormat) {
		myTimeFormat = theFormat;
	}

	public void setIsRepeating(String theIsRepeating) {
		myIsRepeating = theIsRepeating;
	}

	public void setIsPlaying(boolean theIsPlaying) {
		myIsPlaying = theIsPlaying;
	}
	 
	
}
