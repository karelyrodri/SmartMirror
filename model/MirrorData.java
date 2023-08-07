package model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class MirrorData implements MirrorPropertyChange{
	
	private final PropertyChangeSupport myPCS;
	private Spotify mySpotify;
	private String[] myQuotes;
	private Weather myWeather;
	private int myIndex;
	
	public MirrorData() {
		myPCS = new PropertyChangeSupport(this);
		mySpotify = new Spotify();
		//Bonney Lake [47.18, -122.19]
		myWeather = new Weather(47.18, -122.19);
		myIndex = 0;
		myQuotes = collectQuotes();
		
	}
		
	public void addPropertyChangeListener(String thePropertyName,
			                              PropertyChangeListener theListener) {
		myPCS.addPropertyChangeListener(thePropertyName, theListener);
	}
	
	
	public void sendWeather() {
		myPCS.firePropertyChange(PROPERTY_WEATHER_CURRENT, null,
				                   myWeather.getCurrentWeather());
		myPCS.firePropertyChange(PROPERTY_WEATHER_FORCAST, null,
                                   myWeather.getDailyWeather());
	}
	
	public void sendNewQuote() {
		if (myIndex >= 14) {
			myQuotes = collectQuotes();
			myIndex = 0;	
		}
		myPCS.firePropertyChange(PROPERTY_QUOTE, "", myQuotes[myIndex]);
		myPCS.firePropertyChange(PROPERTY_QUOTE_AUTHOR, "", myQuotes[myIndex + 1]);
		myIndex += 2;
	}
	
	public String[] collectQuotes() {
		String[] quotes = new String[14];
		for (int i = 1; i < 14; i+= 2   ) {
			Quotes q = new Quotes();
			quotes[i - 1] = q.getQuote();
			quotes[i] = q.getAuthor();
		}
		return quotes;
	}
	
	public Spotify getSpotify() {
		return mySpotify;
	}
	
	
}
