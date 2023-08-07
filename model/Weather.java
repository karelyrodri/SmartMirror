package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;         
import javax.ws.rs.client.WebTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Weather {
	private double myLatitude;
	private double myLongitude;
	private String myAPIKey;
	private Map<String, String> myCurrentData;
	private Map<String, List<String>> myDailyData;
	
	public Weather(double theLatitude, double theLongitude) {
		myLatitude = theLatitude;
		myLongitude = theLongitude;
		myCurrentData = new HashMap<String, String>();
		myDailyData = new HashMap<String, List<String>>();
		myAPIKey = "";
		callWeather();
	}
	
	//api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}
	private void  callWeather() {
		StringBuilder sbCurrent = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?lat=");
		String coords = myLatitude + "&lon=" + myLongitude;
		String apiUnits = "&appid=" + myAPIKey + "&units=imperial";
		sbCurrent.append(coords);
		sbCurrent.append(apiUnits);
		String currentWeatherSite = sbCurrent.toString();
		StringBuilder sbDaily = new StringBuilder("https://api.openweathermap.org/data/2.5/forecast?lat=");
		sbDaily.append(coords);
		sbDaily.append(apiUnits);
		String dailyForcastSite = sbDaily.toString();
		
		Client client = ClientBuilder.newClient();
		try {
			WebTarget targetCurr = client.target(currentWeatherSite);
			String jsonCurrentW = targetCurr.request().get(String.class);
			currentData(jsonCurrentW);
			
			WebTarget targetDaily = client.target(dailyForcastSite);
			String jsonDailyW = targetDaily.request().get(String.class);	
			dailyData(jsonDailyW);
		} catch(Exception exc) {
			System.out.println("Failed to retrieve data from website!");
		}
			
	}
	
	private void currentData(String theJSON) throws JSONException {
		JSONObject objectBase = new JSONObject(theJSON);
		JSONObject weather = objectBase.getJSONArray("weather").getJSONObject(0);
		Object icon = weather.get("icon"); 
		Object description = weather.get("description"); 
		
		JSONObject details = objectBase.getJSONObject("main");
		Object temp = details.get("temp");
		Object humidity = details.get("humidity"); 
		
		JSONObject wind = objectBase.getJSONObject("wind");
		Object speed = wind.get("speed");
		
		myCurrentData.put("icon", (String) icon);
		myCurrentData.put("description", (String) description);
		myCurrentData.put("temp", temp.toString());
		myCurrentData.put("humidity", humidity.toString());
		myCurrentData.put("speed", speed.toString());
		
	}
	
	private void dailyData(String theJSON) throws JSONException {
		JSONArray objectBase = new JSONObject(theJSON).getJSONArray("list");
		
		//Day# : [description, temp, humidity, icon]
		myDailyData.put("Day1", collectForcast(2, objectBase));
		myDailyData.put("Day2", collectForcast(10, objectBase));
		myDailyData.put("Day3", collectForcast(18, objectBase));
		myDailyData.put("Day4", collectForcast(26, objectBase));
		myDailyData.put("Day5", collectForcast(34, objectBase));
	}
	
	private List<String> collectForcast(int theDayIndex, JSONArray theObjectBase) throws JSONException {	
		JSONObject day = theObjectBase.getJSONObject(theDayIndex);
		
		JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
		Object description = weather.get("description");
		Object icon = weather.get("icon");
		
		JSONObject details = day.getJSONObject("main");
		Object temp = details.get("temp");
		Object humidity = details.get("humidity");
		
		//[description, temp, humidity, icon]
		List<String> data = new ArrayList<>();
		data.add((String) description);
		data.add(temp.toString());
		data.add(humidity.toString());
		data.add((String) icon);
		
		return data;
	}
	
	
	public Map<String,String> getCurrentWeather() {
		return myCurrentData;
	}
	
	public Map<String, List<String>> getDailyWeather() {
		return myDailyData;
	}
}
