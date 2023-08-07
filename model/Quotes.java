package model;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONException;
import org.json.JSONObject;


public class Quotes {
	private String myAPIKey;
	private String myQuote;
	private String myAuthor;
	
	public Quotes () {
		myAPIKey = "";
		callQuote();
		while (myQuote.length() > 180) {
			callQuote();
		}
	}
	
	private void callQuote() {
		
		Client client = ClientBuilder.newClient();
		try {
			WebTarget quoteTarget = client.target("https://good-quotes.p.rapidapi.com/random");
			MultivaluedMap<String,Object> quoteHeaders = new MultivaluedHashMap<String, Object>();
			quoteHeaders.add("x-rapidapi-host", "good-quotes.p.rapidapi.com");
			quoteHeaders.add("x-rapidapi-key", myAPIKey);
			String jsonQuote = quoteTarget.request().headers(quoteHeaders).get(String.class);
			identifyQuote(jsonQuote);
			
			
		} catch (Exception exc) {
			System.out.println("Failed to retrieve data from website!");
		}
		
				
	}
	
	private void identifyQuote(String theJSON) throws JSONException {
		JSONObject jsonQuote = new JSONObject(theJSON);
		myQuote = (String) jsonQuote.get("quote");
		myAuthor = (String) jsonQuote.get("author");
		
	}
	
	public String getQuote() {
		return myQuote;
	}
	
	public String getAuthor() {
		return myAuthor;
	}

}
