/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class MouseLinkClient{
private Socket connection;
private ServerSocket server;
private ObjectOutputStream outStream;
private ObjectInputStream inStream;
private String status;
private String serverIP;
private final HashMap<Integer, Boolean> btns;
    private javax.swing.JPanel pnlMain;

    public MouseLinkClient(String ip){
         btns = new HashMap();
         serverIP = ip;
         btns.put(1, false);
         btns.put(2, false);
         btns.put(3, false);
    }
    public void startRunning(){
            try {
                status = "Started";
                connectToServer();
                status = "initialising";
                setupStreams();
                status = "Connected (" + connection.getInetAddress().getHostAddress() + ")";
                sendCommand(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() + ":" + GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());

                whileConnected();
            } catch (EOFException eof) {
            }catch (IOException io) {
                System.out.println(io);
            }finally{
                cleanUp();
            }
        
    }
    
    private void connectToServer(){
        boolean connected;
        do{
            try{
                connection = new Socket(InetAddress.getByName(serverIP),8488);
                connected = true;
            }catch(IOException io){
                connected = false;
            }
        }while(!connected);
        
    }
    
    private void setupStreams() throws IOException{
         outStream = new ObjectOutputStream(connection.getOutputStream());
         outStream.flush();
         inStream = new ObjectInputStream(connection.getInputStream());
     }
     
     private void whileConnected() throws IOException{
         String command = "0:0:false:false:false:0";
         while(connection.isConnected() && !connection.isInputShutdown()){
             try {
                 command = (String) inStream.readObject();
             } catch (ClassNotFoundException ex) {
             }
                if (!command.equals("//CLOSE")) {
                    try {
                       String[] settings = command.split(":");
                       Robot rob = new Robot();
                       rob.mouseMove(Integer.parseInt(settings[0]), Integer.parseInt(settings[1]));
                        for (int i = 2; i < settings.length-1; i++) {
                            if (Boolean.valueOf(settings[i]) && !btns.get(i-1)) {
                                switch (i-1) {
                                    case 1:
                                        rob.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                                        break;
                                    case 2:
                                        rob.mousePress(MouseEvent.BUTTON2_DOWN_MASK);
                                        break;
                                    case 3:
                                        rob.mousePress(MouseEvent.BUTTON3_DOWN_MASK);
                                        break;
                                    default:
                                }
                                btns.put(i-1, true);
                            }
                            if (!Boolean.valueOf(settings[i])&& btns.get(i-1)) {
                                switch (i-1) {
                                    case 1:
                                        rob.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                                        break;
                                    case 2:
                                        rob.mouseRelease(MouseEvent.BUTTON2_DOWN_MASK);
                                        break;
                                    case 3:
                                        rob.mouseRelease(MouseEvent.BUTTON3_DOWN_MASK);
                                        break;
                                    default:
                                }
                                btns.put(i-1, false);
                            }
                        }
                        rob.mouseWheel(Integer.parseInt(settings[settings.length-1]));
                    } catch (AWTException ex) {
                    }  
                 }
         }
     }
     
     public String getStatus(){
         return status;
     }
     
    private void sendCommand(String message) throws IOException {
        outStream.writeObject(message);
        outStream.flush();
    }
              
     private void sendMessage(String message){
         try {
             if (message.indexOf("/") != 0) {
                outStream.writeObject(message);
                outStream.flush();
             }else{
                 sendCommand(message);
             }
         } catch (IOException e) {
         }
     }
     
     private void cleanUp(){
        try {
            sendCommand("//CLOSE");
            inStream.close();
            outStream.close();
            connection.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MouseLinkClient.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
     
     public void closeConnecion(){
    try {
        connection.close();
    } catch (IOException ex) {
        Logger.getLogger(MouseLinkClient.class.getName()).log(Level.SEVERE, null, ex);
    }
     }

}
