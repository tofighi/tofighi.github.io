import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MS {

    private static String previousCordinfo = null;

    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
        
        try {
            ServerSocket serverSocket = new ServerSocket(20001);
            System.out.println("Server has started running");
            Socket client = serverSocket.accept();
            System.out.println("Client accepted");
            PrintWriter printWriter = new PrintWriter(client.getOutputStream());

            
            while (true) {
                Point cursor = MouseInfo.getPointerInfo().getLocation();
                int x = cursor.x;
                int y = cursor.y;

                
                String cordinfo = x + "," + y;

                if (cordinfo.equals(previousCordinfo)) {



					


                    continue;  // Skip sending if cordinfo has not changed


                }

                previousCordinfo = cordinfo;

                printWriter.println(cordinfo);
                printWriter.flush();
                Thread.sleep(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
