package GraphicsFramework;

import javax.swing.*;

import AI.EasyAI;
import AI.HardAI;
import AI.MediumAI;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MenuPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Properties:
    public Boolean inGame;
    private QuartoApp frame;
	
	//Main stuff
    private JLabel titleLbl = new JLabel("Saty's Quarto");;
    private JPanel exitBtnPnl = new JPanel();
    public JButton exitGameBtn = new JButton("Quit Game");;
    //menu stuff
    private JButton pvpBtn= new JButton("    PVP    ");
    private JButton aiBtn= new JButton("       AI       ");
    private JPanel optionsPnl = new JPanel();
    private JButton optionsBtn = new JButton("Options");
    private JButton statsBtn = new JButton("Statistics");
    //options stuff
    private JPanel selectOptionPnl = new JPanel();
    private JButton audioBtn = new JButton("Audio");
    private JButton graphicsBtn = new JButton("Graphics");
    private JButton tutorialBtn = new JButton("How To Play");
    private JPanel masterPnl = new JPanel();
    private JLabel masterLbl;
    private JSlider masterSld = new JSlider();
    private JPanel musicPnl = new JPanel();
    private JLabel musicLbl;
    private JSlider musicSld = new JSlider();
    private JPanel sfxPnl = new JPanel();
    private JLabel sfxLbl;
    private JSlider sfxSld = new JSlider();
    private JPanel optionsAudioPnl = new JPanel();
    private JPanel graphicsPnl = new JPanel();
    private JPanel bordersPnl = new JPanel();
    private JLabel bordersLbl = new JLabel("Screen");
    private String[] graphicsOptions = {"Fullscreen", "Window"};
    private JComboBox<String> screenSlc = new JComboBox<>(graphicsOptions);
    private JPanel ttrPnl = new JPanel();
    private JButton nextStepBtn = new JButton("Next");
    private JLabel currentStep;
    private int curTtrFrame  = 1;
    //stats stuff
    private JLabel stats1 = new JLabel("Total Games: " + EasyAI.totalGames);
    private JLabel stats2 = new JLabel("Easy AI Wins: " + EasyAI.easyAIW);
    private JLabel stats3 = new JLabel("\nMedium AI Wins: " + MediumAI.meduimAIW);
    private JLabel stats4 = new JLabel("Hard AI Wins: ");
    //ai stuff
    private JLabel vsLbl = new JLabel("VS");
    private JPanel menuUI = new JPanel();
    private JPanel optionsUI = new JPanel();
    private JPanel statsUI = new JPanel();
    private JPanel aiUI = new JPanel();
    private JPanel aiOptionsUI = new JPanel();
    private String players[] = {"Player", "Easy AI", "Meduim AI", "Hard AI"};
    private int player1, player2;
    private JLabel p1JLbl = new JLabel("Player 1-  ");
    private JComboBox<String> p1Slc = new JComboBox<String>(players);
    private JComboBox<String> p2Slc = new JComboBox<String>(players);
    private JLabel p2JLbl = new JLabel("  -Player 2");
    private JButton aiPlayBtn = new JButton("Start");
    
    private enum UI{menu, options, ai, stats}
    private UI panelState;
    

    //Contractor:
    public MenuPanel(QuartoApp frame) {
    	this.frame = frame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(75 , 0, 75, 0));
        
        initializeComponents();

        add(titleLbl, BorderLayout.NORTH);
        enterMenu();
        add(exitBtnPnl, BorderLayout.SOUTH);
        
        inGame = false;
        player1 = 1;
        player2 = 2;
    }
    
    
    //Other Functions:
    private void initializeComponents() {
    	sfxLbl = new JLabel("Sound Effects Audio " + frame.sfxVolume + "%");
    	musicLbl = new JLabel("Music Audio " + frame.musicVolume + "%");
    	masterLbl = new JLabel("Master Audio " + frame.masterVolume + "%");
    	
    	//loading and setting the ttr png
    	try {
    	    nextStepBtn.setFont(new Font("Arial", Font.BOLD, 45));
    	    nextStepBtn.setFocusable(false);
    	    nextStepBtn.addActionListener(e -> nextStep());
    	    ttrPnl.add(nextStepBtn);

    	    currentStep = new JLabel(new ImageIcon(
    	            ImageIO.read(getClass().getResourceAsStream("/assets/textures/tutorial/step1.png"))
    	    ));
    	    ttrPnl.add(currentStep, BorderLayout.CENTER);
    	} catch (IOException | NullPointerException e) {
    	    e.printStackTrace();
    	    System.err.println("Could not load image: /assets/textures/tutorial/step1.png");
    	}
        
        // Main UI
        titleLbl.setFont(new Font("Arial", Font.BOLD, 150));
        titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        
        exitGameBtn.setFont(new Font("Arial", Font.BOLD, 50));
        exitGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitGameBtn.setFocusable(false);
        exitBtnPnl.add(exitGameBtn);
        exitGameBtn.addActionListener(e -> backButton());
        
        // Menu UI
        menuUI.setLayout(new BoxLayout(menuUI, BoxLayout.Y_AXIS));
        menuUI.setBorder(BorderFactory.createEmptyBorder(110, 300, 0, 300));
        
        pvpBtn.setFont(new Font("Arial", Font.BOLD, 100));
        pvpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pvpBtn.setFocusable(false);
        pvpBtn.addActionListener(e -> frame.enterGame(1, 2));

        aiBtn.setFont(new Font("Arial", Font.BOLD, 85));
        aiBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiBtn.setFocusable(false);
        aiBtn.addActionListener(e -> enterAi());
        
        optionsBtn.setFont(new Font("Arial", Font.BOLD, 46));
        optionsBtn.setFocusable(false);
        optionsPnl.add(optionsBtn);
        optionsBtn.addActionListener(e -> enterOptions());
        
        statsBtn.setFont(new Font("Arial", Font.BOLD, 46));
        statsBtn.setFocusable(false);
        statsBtn.addActionListener(e -> enterStats());
        optionsPnl.add(statsBtn);

        menuUI.add(pvpBtn);
        menuUI.add(Box.createVerticalStrut(50));
        menuUI.add(aiBtn);
        menuUI.add(Box.createVerticalStrut(50));
        menuUI.add(optionsPnl);
        
        //options UI:
        optionsUI.setLayout(new BoxLayout(optionsUI, BoxLayout.Y_AXIS));
        optionsUI.setBorder(BorderFactory.createEmptyBorder(25 , 0, 200, 0));
        optionsAudioPnl.setLayout(new BoxLayout(optionsAudioPnl, BoxLayout.Y_AXIS));
        graphicsPnl.setLayout(new BoxLayout(graphicsPnl, BoxLayout.Y_AXIS));
        ttrPnl.setLayout(new BoxLayout(ttrPnl, BoxLayout.Y_AXIS));
        ttrPnl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 75));
        
        audioBtn.setFont(new Font("Arial", Font.BOLD, 45));
        audioBtn.setFocusable(false);
        audioBtn.addActionListener(e -> audioButton());
        selectOptionPnl.add(audioBtn);
        
        graphicsBtn.setFont(new Font("Arial", Font.BOLD, 45));
        graphicsBtn.setFocusable(false);
        graphicsBtn.addActionListener(e -> graphicsButton());
        selectOptionPnl.add(graphicsBtn);
        
        tutorialBtn.setFont(new Font("Arial", Font.BOLD, 45));
        tutorialBtn.setFocusable(false);
        tutorialBtn.addActionListener(e -> ttrButton());
        selectOptionPnl.add(tutorialBtn);
        
        masterLbl.setFont(new Font("Arial", Font.BOLD, 30));
        masterPnl.add(masterLbl);
        masterSld.setPreferredSize(new Dimension(150, 150));
        masterSld.addChangeListener(e -> setMasterVolume(masterSld.getValue()));
        masterPnl.add(masterSld);
        optionsAudioPnl.add(masterPnl);
        
        musicLbl.setFont(new Font("Arial", Font.BOLD, 30));
        musicPnl.add(musicLbl);
        musicSld.setPreferredSize(new Dimension(150, 150));
        musicSld.addChangeListener(e -> setMusicVolume(musicSld.getValue()));
        musicPnl.add(musicSld);
        optionsAudioPnl.add(musicPnl);
        
        sfxLbl.setFont(new Font("Arial", Font.BOLD, 30));
        sfxPnl.add(sfxLbl);
        sfxSld.setPreferredSize(new Dimension(150, 150));
        sfxSld.addChangeListener(e -> setSfxVolume(sfxSld.getValue()));
        sfxPnl.add(sfxSld);
        optionsAudioPnl.add(sfxPnl);
        
        bordersLbl.setFont(new Font("Arial", Font.BOLD, 30));
        bordersPnl.add(bordersLbl);
        screenSlc.setPreferredSize(new Dimension(250, 50));
        screenSlc.addActionListener(e -> screenChange((String) screenSlc.getSelectedItem()));

        bordersPnl.add(screenSlc);
        
        graphicsPnl.add(bordersPnl);
        
        optionsUI.add(selectOptionPnl);
        optionsUI.add(optionsAudioPnl);
        
        //statsUI:
        statsUI.setLayout(new BoxLayout(statsUI, BoxLayout.Y_AXIS));
        statsUI.setBorder(BorderFactory.createEmptyBorder(150 , 0, 150, 0));
        
        stats1.setFont(new Font("Arial", Font.BOLD, 75));
        stats1.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats2.setFont(new Font("Arial", Font.BOLD, 75));
        stats2.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats3.setFont(new Font("Arial", Font.BOLD, 75));
        stats3.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats4.setFont(new Font("Arial", Font.BOLD, 75));
        stats4.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statsUI.add(stats1);
        statsUI.add(stats2);
        statsUI.add(stats3);
        statsUI.add(stats4);
        
        //aiUI:
        aiUI.setLayout(new BoxLayout(aiUI, BoxLayout.Y_AXIS));
        aiUI.setBorder(BorderFactory.createEmptyBorder(150 , 0, 300, 0));
        vsLbl.setFont(new Font("Arial", Font.BOLD, 75));
        vsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        aiOptionsUI.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        p1JLbl.setFont(new Font("Arial", Font.BOLD, 50));
        p1JLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiOptionsUI.add(p1JLbl);
        
        p1Slc.setPreferredSize(new Dimension(200, 50));
        p1Slc.setAlignmentX(Component.CENTER_ALIGNMENT);
        p1Slc.addActionListener(e -> p1Selector(p1Slc.getSelectedIndex()));
        aiOptionsUI.add(p1Slc);
        
        aiOptionsUI.add(Box.createHorizontalStrut(150));
        
        p2Slc.setPreferredSize(new Dimension(200, 50));
        p2Slc.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiOptionsUI.add(p2Slc);
        
        p2JLbl.setFont(new Font("Arial", Font.BOLD, 50));
        p2JLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p2Slc.addActionListener(e -> p2Selector(p2Slc.getSelectedIndex()));
        aiOptionsUI.add(p2JLbl);
        
        aiPlayBtn.setFont(new Font("Arial", Font.BOLD, 75));
        aiPlayBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiPlayBtn.addActionListener(e -> frame.enterGame(player1, player2));
        
        aiUI.add(vsLbl);
        aiUI.add(aiOptionsUI);
        aiUI.add(aiPlayBtn);
    }
    
    public void enterMenu(){
    	remove(optionsUI);
    	remove(statsUI);
    	remove(aiUI);
    	add(menuUI, BorderLayout.CENTER);
    	exitGameBtn.setText("Quit Game");
    	
    	frame.refreshScreen();
    	
    	panelState = UI.menu;
    }
    
    public void enterOptions(){
    	remove(menuUI);
    	remove(statsUI);
    	remove(aiUI);
    	add(optionsUI, BorderLayout.CENTER);
    	exitGameBtn.setText("Back");
    	
    	frame.refreshScreen();
    	
    	panelState = UI.options;
    }
    
    private void enterStats(){
    	remove(menuUI);
    	remove(optionsUI);
    	remove(aiUI);
    	add(statsUI, BorderLayout.CENTER);
    	exitGameBtn.setText("Back");
        stats1.setText("Total Games: " + EasyAI.totalGames);
        stats2.setText("Easy AI Wins: " + EasyAI.easyAIW);
        stats3.setText("Medium AI Wins: " + MediumAI.meduimAIW);
        stats4.setText("Hard AI Wins: " + HardAI.HardAIW);
    	
    	frame.refreshScreen();
    	
    	panelState = UI.stats;
    }
    
    private void enterAi(){
    	remove(menuUI);
    	remove(optionsUI);
    	remove(statsUI);
    	add(aiUI, BorderLayout.CENTER);
    	exitGameBtn.setText("Back");
    	
    	frame.refreshScreen();
    	
    	panelState = UI.stats;
    }
    
    //when sleting player - if human then p1 - 1, p2 - 2. if ai then the ai level + 1 for p1 and +2 for p2
    private void p1Selector(int p1) {
    	player1 = (p1 == 0) ? 1 : (p1+2);
    	}
    private void p2Selector(int p2) {
    	player2 = (p2 == 0) ? 2 : (p2+2);
    }
    
    private void backButton() {
    	if(!inGame)
        	if (panelState != UI.menu) {
        		enterMenu();
        		exitGameBtn.setName("Quit Game");
        	}
        	else
        		System.exit(0);
    	else {
    		exitGameBtn.setName("Remuse");
    		frame.back2Game();
		}
    }
    
    private void audioButton() {
    	optionsUI.removeAll();
    	optionsUI.add(selectOptionPnl);
    	optionsUI.add(optionsAudioPnl);
    	
    	frame.refreshScreen();
    }
    
    private void setMusicVolume(int volume) {
    	frame.updateMusicVolume(volume);
    	
        musicLbl.setText("Music Audio " + volume + "%");
    }
    
    private void setSfxVolume(int volume) {
    	frame.sfxVolume = volume;
    	
        sfxLbl.setText("Sound Effects Audio " + volume + "%");
    }
    
    private void setMasterVolume(int volume) {
    	frame.masterVolume = volume;
    	frame.updateMusicVolume(frame.musicVolume);
    	
        masterLbl.setText("Master Audio " + volume + "%");
    }
    
    private void graphicsButton() {
    	optionsUI.removeAll();
    	optionsUI.add(selectOptionPnl);
    	optionsUI.add(graphicsPnl);

    	frame.refreshScreen();
    }
    
    private void ttrButton() {
    	optionsUI.removeAll();
    	optionsUI.add(selectOptionPnl);
    	optionsUI.add(ttrPnl);

    	frame.refreshScreen();
    }
    
    private void nextStep() {
        if (curTtrFrame > 6) curTtrFrame = 0;

        ttrPnl.remove(currentStep);
        try {
            currentStep = new JLabel(new ImageIcon(ImageIO.read(
                    getClass().getResourceAsStream("/assets/textures/tutorial/step" + (++curTtrFrame) + ".png")
            )));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Could not load image: /assets/textures/tutorial/step" + curTtrFrame + ".png");
        }

        ttrPnl.add(currentStep);
        frame.refreshScreen();
    }

    
    private void screenChange(String opt) {
    	if(opt.equals(graphicsOptions[0]))
    		frame.toggleFullScreen(true);
    	else if(opt.equals(graphicsOptions[1]))
    		frame.toggleFullScreen(false);
    	
    	frame.refreshScreen();
    }
}