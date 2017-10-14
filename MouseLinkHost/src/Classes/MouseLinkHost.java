/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JBotha
 */
public class MouseLinkHost extends JFrame implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
private Socket connection;
private ServerSocket server;
private ObjectOutputStream outStream;
private ObjectInputStream inStream;
private Point mouse;
private Point centre;
private int wheelChange;
private Robot rob;
private HashMap<Integer,Boolean> buttons;
private ScreenManager s;
private int resX,resY;
private ArrayList<String> console;
    /**
     * @param args the command line arguments
     */

     
     public MouseLinkHost() throws AWTException{
         buttons = new HashMap();
         buttons.put(MouseEvent.BUTTON1, false);
         buttons.put(MouseEvent.BUTTON2, false);
         buttons.put(MouseEvent.BUTTON3, false);
         setSize(500,500);
         s = new ScreenManager();
         s.setFullscreen(s.getCurrentDisplayMode(), this);
         rob = new Robot();
         centre = new Point();
         mouse = new Point();
         recenterMouse();
         mouse.x = centre.x;
         mouse.y = centre.y;
         console = new ArrayList();
         
     }
     
     public void startRunning(){
         Window w = s.getFullscreenWindow();
         w.addKeyListener(this);
         connection = new Socket();
         new Thread (this).start();
    try {
        server = new ServerSocket(8488, 1);
        showMessage("Server running on " + getExtIP() + ":8488");
        while(true){
            try {
                checkForConnections();
                setupStreams();
                try {
                    String res = (String)inStream.readObject();
                    resX = Integer.parseInt(res.split(":")[0]);
                    resY = Integer.parseInt(res.split(":")[1]);
                } catch (ClassNotFoundException ex) {
                    resX = s.getWidth();
                    resY = s.getHeight();
                }
                w.addMouseListener(this);
                w.addMouseMotionListener(this);
                w.addMouseWheelListener(this);
                whileConnected();
            } catch (EOFException eof) {
                System.out.println(connection.getInetAddress().getHostAddress() + " left.");
            } finally {
            cleanUp();
            }
        }
    } catch (IOException ex) {
        showMessage("Server already running!");
    }
     }
     
     public void checkForConnections() throws IOException{
         showMessage("Waiting for connection...");
         connection = server.accept();
         showMessage("Connecting: " + connection.getInetAddress().getHostAddress());
     }
     
     public void setupStreams() throws IOException{
         outStream = new ObjectOutputStream(connection.getOutputStream());
         outStream.flush();
         inStream = new ObjectInputStream(connection.getInputStream());
     }
     
     public void whileConnected(){
         String message;
         showMessage("Connected: " + connection.getInetAddress().getHostAddress());
         boolean connected = true;
         do {             
             try {
                message = (String) inStream.readObject();
                 if (message.equals("//CLOSE")) {
                     cleanUp();
                    showMessage(message);
                 }
             } catch (ClassNotFoundException ex) {
                 showMessage("Cannot send that!");
             }catch (IOException io){
                 showMessage ("Connection lost.");
                 connected = false;
             }
         } while (connected == true);
     }
              
     public void sendCommand(){
         if (connection.isConnected() && !connection.isInputShutdown()) {
            try {
                String command = "";
                command += mouse.x + ":" + mouse.y + ":";
                for (int i = 1; i < buttons.size()+1; i++) {
                    command += buttons.get(i) + ":";
                }
                command += wheelChange;
                outStream.writeObject(command);
                outStream.flush();
                wheelChange = 0;
            } catch (IOException e) {
                showMessage("Cannot send that message.");
            }
         }
     }
     
          public void sendCommand(String command){
         if (connection.isConnected() && !connection.isInputShutdown()) {
             try{
                outStream.writeObject(command);
                outStream.flush();
            } catch (IOException e) {
                showMessage("Cannot send that message.");
            }
         }
     }
     
     
     
     private synchronized void recenterMouse(){
        Window w = s.getFullscreenWindow();
        if (rob != null && w.isShowing()) {
            centre.x = w.getWidth()/2;
            centre.y = w.getHeight()/2;
            
            SwingUtilities.convertPointToScreen(centre, w);
            
            rob.mouseMove(centre.x,centre.y);
            
        }
    }
     public synchronized void showMessage(String text){
         console.add(text);
         while (console.size() > 35) {
             console.remove(0);
         }
     }
     
     public synchronized void cleanUp(){
    try {
        sendCommand("//CLOSE");
        inStream.close();
        outStream.close();
        connection.close();
    } catch (IOException ex) {
        Logger.getLogger(MouseLinkHost.class.getName()).log(Level.SEVERE, null, ex);
    }
         
     }
     
     public static String getExtIP () throws IOException{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        return ip;
        }
     
     public static void main(String[] args) {
    try {
        MouseLinkHost chatServer = new MouseLinkHost();
        chatServer.startRunning();
    } catch (AWTException ex) {
        Logger.getLogger(MouseLinkHost.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

     //<editor-fold defaultstate="collapsed" desc="Mouse Listener">
    @Override
    public void mouseClicked(MouseEvent e) {
   }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons.put(e.getButton(), true);
        sendCommand();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons.put(e.getButton(), false);
        sendCommand();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
     
     //</editor-fold>

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getXOnScreen() - centre.x;
        int dy = e.getYOnScreen() - centre.y;
        mouse.x += dx;
        mouse.y += dy;
        if (mouse.x > resX) {
            mouse.x = resX;
        }
        if (mouse.x < 0) {
            mouse.x = 0;
        }
        if (mouse.y > resY) {
            mouse.y = resY;
        }
        if (mouse.y < 0) {
            mouse.y = 0;
        }
        recenterMouse();
        sendCommand();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int dx = e.getXOnScreen() - centre.x;
        int dy = e.getYOnScreen() - centre.y;
        mouse.x += dx;
        mouse.y += dy;
        if (mouse.x > resX) {
            mouse.x = resX;
        }
        if (mouse.x < 0) {
            mouse.x = 0;
        }
        if (mouse.y > resY) {
            mouse.y = resY;
        }
        if (mouse.y < 0) {
            mouse.y = 0;
        }
        recenterMouse();
        sendCommand();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelChange += e.getWheelRotation();
        sendCommand();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.isShiftDown() && e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        e.consume();
    }

    @Override
    public void run() {
        while(true){
         Graphics2D g = s.getGraphics();
         g.setColor(Color.black);
         g.fillRect(0,0,s.getWidth(),s.getHeight());
         g.setColor(Color.green);
         g.drawString("Messages:", 20, 60);
         g.drawString("Mouse Position - x: " + mouse.x + "   y: " + mouse.y , 20, 40);
         g.drawString("Press Shift + ESC to exit.", 20, 20);
         for (int i = 0; i < console.size(); i++) {
             g.drawString(console.get(i),20, (i * 15) + 85);
         }
         s.update();
            try {
                Thread.sleep(45);
            } catch (InterruptedException ex) {}
        }
    }
}

