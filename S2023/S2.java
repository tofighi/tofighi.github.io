import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

public class S2 {

    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
        if (args.length != 1) {
            System.out.println("Usage: java S2 <scale>");
            System.exit(1);
        }
        double scale = Double.parseDouble(args[0]);
        if (scale <= 0 || scale > 1) {
            System.out.println("Scale must be between 0 and 1");
            System.exit(1);
        }
        new S2().server(20000, scale);
    }

    ServerSocket server;
    Socket socket;

    private void server(int port, double scale) {
        try {
            server = new ServerSocket(port);
            Robot r = new Robot();

            while (true) {
                try {
                    socket = server.accept();
                    InetAddress addr = socket.getInetAddress();
                    ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());

                    // Capture the full screen
                    Rectangle captureArea = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage img = r.createScreenCapture(captureArea);

                    // Add
                    Point mouse = MouseInfo.getPointerInfo().getLocation();
					Graphics g = img.getGraphics();
					g.setColor(Color.BLACK);
					g.fillRect(mouse.x, mouse.y, 10, 10);

                    // Scale down the captured image based on the provided scale factor
                    int scaledWidth = (int) (img.getWidth() * scale);
                    int scaledHeight = (int) (img.getHeight() * scale);
                    Image scaledImage = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                    scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

                    // Calculate file size before compression
                    ByteArrayOutputStream originalImageStream = new ByteArrayOutputStream();
                    ImageIO.write(scaledBufferedImage, "jpg", originalImageStream);
                    long originalFileSize = originalImageStream.size();

                    // Compress the image using JPEG compression with a specific quality
                    ImageWriteParam param = createJPEGImageWriteParam(0.5f); // Adjust quality (e.g., 0.5f for higher compression)

                    ByteArrayOutputStream compressedImageStream = new ByteArrayOutputStream();
                    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                    ImageWriter writer = writers.next();

                    // Write the compressed image to the output stream
                    ImageIO.write(scaledBufferedImage, "jpg", compressedImageStream);

                    long compressedFileSize = compressedImageStream.size();

                   // System.out.println("Original File Size: " + originalFileSize + " bytes");
                   // System.out.println("Compressed File Size: " + compressedFileSize + " bytes");

                    // Send the compressed image
                    outstream.write(compressedImageStream.toByteArray());

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // Create ImageWriteParam for JPEG compression with the specified quality
    private ImageWriteParam createJPEGImageWriteParam(float quality) {
        ImageWriteParam param = ImageIO.getImageWritersByFormatName("jpg").next().getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        return param;
    }
}
