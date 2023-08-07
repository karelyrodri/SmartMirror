package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Track {
	private String myTrackName;
	private ImageIcon myImage;
	private String mySpotifyID;
	private String[] myArtists;
	private int myDurationMS;
	
	public Track(String theName, String theImageUrl, int theMS, String theID) throws IOException {
		myTrackName = theName;
		myImage = retrieveTrackImage(theImageUrl);
		myDurationMS = theMS;
		mySpotifyID = theID;
	}
	

	private ImageIcon retrieveTrackImage(String theUrl) throws IOException {
		URL url;
		BufferedImage image;
		try {
			url = new URL(theUrl);
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			throw new MalformedURLException();
		}
        return new ImageIcon(image.getScaledInstance(150, -1, java.awt.Image.SCALE_SMOOTH));

	}
	
	public void setArtist(String[] theArtists) {
		myArtists = theArtists;
	}
	
	public String getTrackName() {
		return myTrackName;
	}

	public ImageIcon getImage() {
		return myImage;
	}

	public int getDurationMS() {
		return myDurationMS;
	}

	public String getSpotifyID() {
		return mySpotifyID;
	}

	public String[] getArtists() {
		return myArtists;
	}


}
