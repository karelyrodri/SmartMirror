package application;
import view.MirrorDisplay;
import java.awt.EventQueue;



public class Main {
	//Bonney Lake [47.18, -122.19]
	public static void main(String[] args) {
  		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MirrorDisplay();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		});
			
	}

	

	
}
