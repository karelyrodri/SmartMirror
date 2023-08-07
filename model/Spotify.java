package model;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Spotify {
	private String myClientID;
	private String mySecretID;
	private String myRedirectURI;
	private String myCode;
	private String myAccessToken;
	private String myRefreshToken; 
	private String myDeviceID;
	private Track myCurrentTrack;
	private int myExpiresIn;
	private Client myClient; 
	private WebDriver myWebDriver;	
	
	public Spotify() {
		myClientID = "";
		mySecretID = "";
		myRedirectURI = "http://localhost:8081";
		myWebDriver = new FirefoxDriver();
		myClient = ClientBuilder.newClient();
		oAuthGet();   
	
		
	}
	
	private void oAuthGet() {
		StringBuilder requestSite = new StringBuilder("https://accounts.spotify.com/authorize?client_id=");
		requestSite.append(myClientID + "&response_type=code&redirect_uri=" + myRedirectURI );
		requestSite.append("&scope=user-read-currently-playing%20");
		requestSite.append("user-modify-playback-state%20user-read-playback-state");
		String authSite = requestSite.toString();
		
		try {
		WebTarget target = myClient.target(authSite);
		Response  redirectedSite = target.request().get();
		String codeQueryParam = loginAcceptAgreement(redirectedSite.getLocation());
		myCode = codeQueryParam.substring(codeQueryParam.indexOf('=') + 1);
		
		} catch (Exception e) {
			System.out.println("Failed to make authentication request to Spotify!");
		}
		String params = "grant_type=authorization_code&code=" + myCode
			          + "&redirect_uri=" + myRedirectURI;
		oAuthPost(params, true);	
	}
	
	private void oAuthPost(String theParams, boolean theContainsRefresh) {
		StringBuilder requestSite = new StringBuilder("https://accounts.spotify.com/api/token");
		String postSite = requestSite.toString();
		
		String clientIDs = myClientID + ":" + mySecretID;
		String encodedClientIDs = Base64.getEncoder().encodeToString(clientIDs.getBytes());
	
		try {
			Response tokens = myClient.target(postSite).request(MediaType.APPLICATION_JSON_TYPE)
 			                        .header("Authorization","Basic " +  encodedClientIDs)
					                .post(Entity.entity(theParams, MediaType.APPLICATION_FORM_URLENCODED));
			String jsonResponse = tokens.readEntity(String.class);
			disectTokens(jsonResponse, theContainsRefresh);
			raspotifyDevice();
		} catch (JSONException e) {
			System.out.println("Failed to make POST authentication request!");
		}

	}
	
	private void checkRefreshUpdate() {
		if (myExpiresIn <= 0) {
			String params = "grant_type=refresh_token&refresh_token=" + myRefreshToken;
			oAuthPost(params, false);
		}
		
	}
	
	private void disectTokens(String theJson, boolean theRefresh) throws JSONException {
  		JSONObject jsonObj = new JSONObject(theJson);
		if (theRefresh) {
			myRefreshToken = (String) jsonObj.get("refresh_token");
		}
		myAccessToken = (String) jsonObj.get("access_token");
		myExpiresIn = (int) jsonObj.get("expires_in");
	}
	
	private String loginAcceptAgreement(URI theURI) {
		myWebDriver.get(theURI.toString());
		myWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		if (existsElement("login-username")) {
			myWebDriver.findElement(By.id("login-username")).sendKeys("");
			myWebDriver.findElement(By.id("login-password")).sendKeys("");
			myWebDriver.findElement(By.id("login-button")).click();
		}
		myWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		if (existsElement("auth-accept")) {
			myWebDriver.findElement(By.id("auth-accept")).click();
			
		}
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			System.out.println("Sleep Fail");
			e.printStackTrace();
		}
		return myWebDriver.getCurrentUrl();
				
	} 
	
	private boolean existsElement(String id) {
	    try {
	        myWebDriver.findElement(By.id(id));
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	    return true;
	}
	//raspotify (rodrikar)   device id does change
	private void raspotifyDevice() throws JSONException {
		String target = "https://api.spotify.com/v1/me/player/devices";
		String jsonDevices = myClient.target(target).request()
                .header("Authorization","Bearer " +  myAccessToken)
                .get(String.class);
		JSONArray id = new JSONObject(jsonDevices).getJSONArray("devices");
		for (int i = 0; i < id.length(); i++) {
			if (((String) id.getJSONObject(i).get("name")).equals("raspotify (rodrikar)")) {
				myDeviceID = (String) id.getJSONObject(i).get("id");
			}
		}
	}
	
	public Track getCurrentTrack() {
		return myCurrentTrack;
	}
	
	public void setExpirationSet(int theSubtract) {
		myExpiresIn -= theSubtract;
		
	}
	public void playingCurrenty() throws JSONException {
		checkRefreshUpdate();
		String target = "https://api.spotify.com/v1/me/player/currently-playing";
		String jsonTrack = myClient.target(target).request()
				                   .header("Authorization","Bearer " +  myAccessToken)
				                   .get(String.class);
		JSONObject objectBase = new JSONObject(jsonTrack).getJSONObject("item");
		int duration = (int) objectBase.get("duration_ms");
		String trackID = (String) objectBase.get("id");
		String trackName = (String) objectBase.get("name");
		
		String imageURL = (String) objectBase.getJSONObject("album").getJSONArray("images")
				                             .getJSONObject(1).get("url");
		
		JSONArray artistArray = objectBase.getJSONArray("artists");
		int artistLen = artistArray.length();
		String[] artists = new String[artistLen];
		for (int i = 0; i < artistLen; i++) {
			String name = (String) artistArray.getJSONObject(i).get("name");
			artists[i] = name;
		}
			
		try {
			myCurrentTrack = new Track(trackName, imageURL, duration, trackID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCurrentTrack.setArtist(artists);
		
	}
	
	//call false to begin program, true = shuffle on
	public void shuffle(boolean theShuffleState) {
		StringBuilder target = new StringBuilder("https://api.spotify.com/v1/me/player/shuffle");
		target.append("?state=" + theShuffleState);
		target.append("&device_id=" + myDeviceID);
		putRequest(target.toString());
		
	}
	
	
	// track = repeat track      off = turn of repeat 
	public void repeat(String theRepeatState) {
		StringBuilder target = new StringBuilder("https://api.spotify.com/v1/me/player/repeat");
		target.append("?state=" + theRepeatState);
		target.append("&device_id=" + myDeviceID);
		putRequest(target.toString());
	}
	
	public void pause() {
		String target = "https://api.spotify.com/v1/me/player/pause";
		putRequest(target);
	}
	
	public void play() {
		checkRefreshUpdate();
		String target = "https://api.spotify.com/v1/me/player/play";
		myClient.target(target).request()
				.header("Authorization","Bearer " +  myAccessToken)
		        .put(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
	}
	
	public void changeVolume(int theVolume) {
		StringBuilder target = new StringBuilder("https://api.spotify.com/v1/me/player/volume");
		target.append("?volume_percent=" + theVolume);
		target.append("&device_id=" + myDeviceID);
		putRequest(target.toString());
	}
	
	private void putRequest(String theTarget) {
		checkRefreshUpdate();
		myClient.target(theTarget).request()
        		.header("Authorization","Bearer " +  myAccessToken)
        		.put(Entity.text("204 NO CONTENT"));
	}
	
	public void previousTrack() {
		String target = "https://api.spotify.com/v1/me/player/previous?device_id=" + myDeviceID;
		postRequest(target);
	}
	
	public void skipTrack() {
		String target = "https://api.spotify.com/v1/me/player/next?device_id=" + myDeviceID;
		postRequest(target);
	}
	
	private void postRequest(String theTarget) {
		checkRefreshUpdate();
		myClient.target(theTarget).request()
				.header("Authorization","Bearer " +  myAccessToken)
				.post(Entity.text("204 NO CONTENT"));
	}
	
	
}
