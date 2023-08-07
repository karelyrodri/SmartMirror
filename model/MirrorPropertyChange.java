package model;

import java.beans.PropertyChangeListener;

public interface MirrorPropertyChange {
	// Property change strings
	 String PROPERTY_WEATHER_CURRENT = "CURRENT WEATHER";
	 String PROPERTY_WEATHER_FORCAST = "5 DAY WEATHER FORCAST";
	 String PROPERTY_QUOTE = "NEW QUOTE";
	 String PROPERTY_QUOTE_AUTHOR = "QUOTE'S AUTHOR";
	 void addPropertyChangeListener(String thePropertyName,
			                        PropertyChangeListener theListener);
}
