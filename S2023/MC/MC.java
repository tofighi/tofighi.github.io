import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class MC {
	
	

	public static void main(String args[]){

		System.setProperty("apple.awt.UIElement", "true");

		if (args.length != 1) {
            System.out.println("Usage: java MC <ipAddress>");
            return;
        }

        String ipAddress = args[0];

		//String ipAddress = "172.20.10.3";
		
		try {
			//comment this out if you dont want the user to know your connecting to them
			//JOptionPane.showMessageDialog(null, "CONNECTING TO SERVER");
			
			//initializing connection to server (controlling computer)
			Socket socket = new Socket(ipAddress, 20002);
			
			Scanner listener = new Scanner(socket.getInputStream());
			
			//initializing Robot Object
			Robot robot = new Robot();
			
			while(!listener.nextLine().equals("end")) {
				String mouseCoordinateString = listener.nextLine();
				
				//System.out.println(mouseCoordinateString);
				
				String[] coordinatesStrings = mouseCoordinateString.split(",");
				int x = Integer.parseInt(coordinatesStrings[0]);
				int y = Integer.parseInt(coordinatesStrings[1]);
				
				Dimension screenSizeDimension = Toolkit.getDefaultToolkit().getScreenSize();
				
				
				
				robot.mouseMove(x, y);
				
			}
			
			
			
		} catch (Exception e) {
			
		}finally {
			
		}
		
	}
	
}
