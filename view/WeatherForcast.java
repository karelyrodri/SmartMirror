package view;
import java.awt.GridLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.MirrorPropertyChange;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

public class WeatherForcast extends Style implements PropertyChangeListener {
	private static final long serialVersionUID = 7895231908006061941L;
	private JPanel myToday;
	private JPanel myForcast;
	private int myWeekDay;
	private String[] myDays = new String[]{"Sunday", "Monday","Tuesday", "Wednesday",
			                                         "Thursday" , "Friday", "Saturday"};
	
	
	
	public WeatherForcast() {
		super();
		this.requestFocusInWindow();
		Calendar cal = Calendar.getInstance();
		myWeekDay = cal.get(Calendar.DAY_OF_WEEK); 
		setLayout(new FlowLayout(0, 28, -40));
		setBounds(485, 255, 260, 240);
		myToday = new JPanel(new BorderLayout(8, 0));
		myForcast = new JPanel(new GridLayout(2,5));
		
	}   
	
	private void todaysWeather(Map<String, String> theWeather) {
		myToday.setBackground(Color.BLACK);
		String image = theWeather.get("icon");
		String desc = theWeather.get("description");
		String temp = theWeather.get("temp").substring(0, 4) + '\u00B0';
		String humidity = theWeather.get("humidity") + "%";
		String wind = theWeather.get("speed") + "mph";
		
		StringBuilder info = new StringBuilder("<html>");
		info.append(myDays[myWeekDay - 1] + "<br/>");
		info.append(desc + "<br/>Humidity "); 
		info.append(humidity + "<br/>Wind ");
		info.append(wind + "</html>");
		JLabel weatherInfo = makeLabel(info.toString(), 15);
		JLabel tempAndIcon = makeLabel(temp, 20);
        tempAndIcon.setIcon(makeIcons("/Images/" + image + ".png", 68));
		myToday.add(weatherInfo, BorderLayout.WEST);
		myToday.add(tempAndIcon, BorderLayout.EAST);
		myToday.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEtchedBorder(), 
                          BorderFactory.createEmptyBorder(30, 20, 0, 0)));
	}
	
	//[description, temp, humidity, icon]
	//'\u00B0'
	private void buildForcast(Map<String, List<String>>  theFiveDayCast) {
		myForcast.setBackground(Color.BLACK);
		//myForcast.addKeyListener();
		int day = myWeekDay;
		for (int i = 1; i <= 5; i++) {
			if (day > 6) {
				day = 0;	
			}  
			String weekDay = "  " + myDays[day].substring(0, 3);
			day++;
			
			JLabel dayName = makeLabel(weekDay, 13);
			dayName.setVerticalAlignment(JLabel.BOTTOM);
			myForcast.add(dayName);
		}	
		
		for (int i = 1; i <= 5; i++) {
			List<String> dailyInfo = theFiveDayCast.get("Day" + i);
			StringBuilder info = new StringBuilder("<html> ");
			String degrees = dailyInfo.get(1).substring(0, 2);
			info.append(degrees);
			info.append('\u00B0' + "<br/>"); 
			info.append(dailyInfo.get(2) + "%</html>");
			JLabel infoLabel = makeLabel(info.toString(), 12);
			String iconImage = dailyInfo.get(3);
			ImageIcon icon = new ImageIcon(Music.class.getResource("/Images/" + iconImage + ".png"));                
	        Image smallImage = icon.getImage().getScaledInstance(40, -1, java.awt.Image.SCALE_SMOOTH);
	        infoLabel.setIcon(new ImageIcon(smallImage));
	        myForcast.add(infoLabel);
		}
	}
	
	public JPanel getForcast() {
		return myForcast;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent theEvent) {
		if (MirrorPropertyChange.PROPERTY_WEATHER_CURRENT
				                .equals(theEvent.getPropertyName())) {
			Map<String,String> currentWeather = (Map<String,String>) theEvent.getNewValue();
			myToday.setVisible(false);
			myToday.removeAll();
			todaysWeather(currentWeather);
			if (getComponentCount() == 0) {
				add(myToday);
			}
			myToday.setBounds(475, 260, 260, 45);
			myToday.setVisible(true);
		} else if (MirrorPropertyChange.PROPERTY_WEATHER_FORCAST
				                       .equals(theEvent.getPropertyName())) {
			Map<String, List<String>> forcast = (Map<String, List<String>>) theEvent.getNewValue();
			myToday.setVisible(false);
			myForcast.removeAll();
			buildForcast(forcast);
			if (getComponentCount() == 1) {
				add(myForcast);
			}
			myForcast.setBounds(505, 290, 215, 220);
			myToday.setVisible(true);
		}
		
	}
	 
		
	
}
