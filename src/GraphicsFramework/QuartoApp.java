package GraphicsFramework;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import logicComponents.*;

public class QuartoApp extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuPanel menuPanel;
    public GamePanel gamePanel;
    
    public int masterVolume = 50, sfxVolume = 50, musicVolume = 50;
    private Clip musicClip;
    private FloatControl volumeControl;
    
    public QuartoApp() {
        setName("Quarto");
        
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        
        initializeFrame();
        playBackgroundMusic("/assets/music/MenuMusic-LuxuryHotel.wav");

    }
    
    public void refreshScreen(){
        revalidate();
        repaint();
        setVisible(true);
    }
    
    public void toggleFullScreen(boolean ifFullscreen) {
        dispose();

        setUndecorated(ifFullscreen);

        pack();

        setVisible(true);    
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void enterGame(int p1,int p2){
        menuPanel.inGame = true;
        remove(menuPanel);
        gamePanel = new GamePanel(this, new Board(), p1, p2);
        add(gamePanel);
        
        menuPanel.inGame = true;
        
        refreshScreen();
    }
    
    public void back2Game(){
        remove(menuPanel);
        add(gamePanel);
        
        menuPanel.inGame = true;
        
        refreshScreen();
    }
    
    public void enterMenu() {
        menuPanel.inGame = false;
        remove(gamePanel);
        add(menuPanel);
        menuPanel.enterMenu();
        
        menuPanel.inGame = false;
        
        refreshScreen();
    }
    
    public void enterGameOptions() {
        remove(gamePanel);
        add(menuPanel);
        menuPanel.enterOptions();
        menuPanel.exitGameBtn.setText("Back To Game");
        
        refreshScreen();
    }
    
    public void gameAgain(int p1, int p2) {
        menuPanel.inGame = true;
        remove(gamePanel);
        gamePanel = new GamePanel(this, new Board(), p1, p2);
        add(gamePanel);
        
        menuPanel.inGame = true;
        
        refreshScreen();
    }
    
    private void initializeFrame(){
        setLocationRelativeTo(null);
        toggleFullScreen(true);
    }

    private void playBackgroundMusic(String resourcePath) {
        new Thread(() -> {
            try {
                musicClip = AudioSystem.getClip();
                
                // Load the resource from the classpath
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                        getClass().getResource(resourcePath));
                
                musicClip.open(audioStream);

                volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                updateMusicVolume(musicVolume);

                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                musicClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
                e.printStackTrace();
                System.err.println("Could not load resource: " + resourcePath);
            }
        }).start();
    }




    public void updateMusicVolume(int volume) {
    	musicVolume = volume;
 
        if (volumeControl != null) {
            float dB = (float) (Math.log10((musicVolume * masterVolume /100f) / 100.0) * 50.0);

            if(dB < -80)
            	dB = -80;
            
            volumeControl.setValue(dB);
        }
    }

}
