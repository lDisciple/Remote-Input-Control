/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author JBotha
 */
public class ScreenManager {
    private GraphicsDevice vc;
    
    public ScreenManager(GraphicsDevice gd){
        vc = gd;
    }
    public ScreenManager(){
        this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
    }
    
    public DisplayMode[] getcompatibleDisplayModes(){
        return vc.getDisplayModes();
    }
    
    public DisplayMode findFirstDisplayCompatibleMode(DisplayMode[] modes){
        DisplayMode[] goodModes = vc.getDisplayModes();
        for (int i = 0; i < modes.length; i++) {
            for (int j = 0; j < goodModes.length; j++) {
                if (displayModesMatch(modes[i],goodModes[j])) {
                    return modes[i];
                }
            }
        }
        return null;
    }

    public DisplayMode getCurrentDisplayMode (){
        return vc.getDisplayMode();
    }
    
    private boolean displayModesMatch(DisplayMode dm1, DisplayMode dm2) {
        if (dm1.getWidth() != dm2.getWidth() || dm1.getHeight() != dm2.getHeight()) {
            return false;
        }
        if (dm1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && dm2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && dm1.getBitDepth() != dm2.getBitDepth()) {
            return false;
        }
        if (dm1.getRefreshRate() != dm2.getRefreshRate() && dm1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN&& dm2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN) {
            return false;
        }
        return true;
    }
    
    public void setFullscreen(DisplayMode dm){
        JFrame f = new JFrame();
        f.setUndecorated(true);
        f.setIgnoreRepaint(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);
        
        if (dm != null && vc.isDisplayChangeSupported()) {
            try{
                vc.setDisplayMode(dm);
            }catch(Exception e){
                
            }
        }
        
        f.createBufferStrategy(2);
        
    }
    public void setFullscreen(DisplayMode dm, JFrame f){
        f.setUndecorated(true);
        f.setIgnoreRepaint(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);
        
        if (dm != null && vc.isDisplayChangeSupported()) {
            try{
                vc.setDisplayMode(dm);
            }catch(Exception e){
                
            }
        }
        Window w = vc.getFullScreenWindow();
        w.createBufferStrategy(2);
        
    }
    
    public Graphics2D getGraphics(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            return (Graphics2D) s.getDrawGraphics();
        }else{
            return null;
        }
    }
    
    public void update(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            if (!s.contentsLost()) {
                s.show();
            }
        }
    }
    
    public Window getFullscreenWindow(){
        return vc.getFullScreenWindow();
    }
    
    public int getWidth(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getWidth();
        }else{
            return 0;
        }
    }
    
    public int getHeight(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getHeight();
        }else{
            return 0;
        }
    }
    
    public void restoreScreen(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        vc.setFullScreenWindow(null);
    }
    
//create compatible image
    public BufferedImage createCompatibleImage(int width, int height, int transparency){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            GraphicsConfiguration gc = w.getGraphicsConfiguration();
            return gc.createCompatibleImage(width, height, transparency);
        }else{
            return null;
        }
    }
}
