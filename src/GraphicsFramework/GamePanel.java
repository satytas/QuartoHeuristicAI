package GraphicsFramework;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.sound.sampled.*;
import AI.EasyAI;
import AI.HardAI;
import AI.MediumAI;
import logicComponents.Board;
import logicComponents.GameLogic;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class GamePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameLogic logic;
	private QuartoApp frame;
	private Board board;
	
	private GameLogic.GameState gameState;
	private boolean gameClosed;
	
	public int currentP, p1, p2;
	private EasyAI ai1, ai2;
	int aiSPEED = 0;

	private pieceButton offBoardPieceButtons[][] = new pieceButton[Board.BoardSize][Board.BoardSize];
	private pieceButton onBoardPieceButtons[][] = new pieceButton[Board.BoardSize][Board.BoardSize];
	
	private JPanel gameStatusPnl = new JPanel();
	public JLabel gameStatusLbl = new JLabel();
	private pieceButton pickedButton = new pieceButton();
	private JButton resBtn = new JButton("Again?");
	private JPanel resBtnPnl = new JPanel();
	private JPanel pickedButtonPnl = new JPanel();
	private JPanel btnsPanel = new JPanel();
	private JButton exitBtn = new JButton("Quit To Menu");
	private JButton optBtn = new JButton("options");
	
	private JPanel gamePanel = new JPanel();
	private JPanel pieces1 = new JPanel();
	private JPanel boardPnl = new JPanel();
	private JPanel pieces2 = new JPanel();
	
	
	public GamePanel(QuartoApp frame, Board board, int p1, int p2) {
		gameClosed = false;
		this.logic = new GameLogic(board);
		
    	this.frame = frame;
    	this.board = board;
        setLayout(new BorderLayout());
        
        this.p1 = p1;
        this.p2 = p2;
        
        //creating the ai acording for p1
        if (p1 == 3)
            ai1 = new EasyAI(board, logic);
        else if (p1 == 4)
            ai1 = new MediumAI(board, logic);
        else if (p1 == 5)
            ai1 = new HardAI(board, logic);
        
        if (p2 == 3)
            ai2 = new EasyAI(board, logic);
        else if (p2 == 4)
            ai2 = new MediumAI(board, logic);
        else if (p2 == 5)
            ai2 = new HardAI(board, logic);
        
        currentP = p1;
        gameState = GameLogic.GameState.pick;
        
        initializeComponents();

        if(p1 >= 3) aiPick(ai1);
	}
	
	private void initializeComponents() {
        frame.refreshScreen();
		Color bkColor = new Color(25,183,180);
		
		setBackground(bkColor);
		gameStatusPnl.setBackground(bkColor);
		pickedButtonPnl.setBackground(bkColor);
		btnsPanel.setBackground(bkColor);
		gamePanel.setBackground(bkColor);
		pieces1.setBackground(bkColor);
		boardPnl.setBackground(bkColor);
		pieces2.setBackground(bkColor);
		gameStatusLbl.setFont(new Font("Arial", Font.BOLD, 45));
		gameStatusLbl.setHorizontalAlignment(SwingConstants.LEFT);
		
		if(nextPlayer() < 3)
			gameStatusLbl.setText("Player " + currentP + "'s turn, select a piece for Player " + nextPlayer());
		else
			gameStatusLbl.setText("Player " + currentP + "'s turn, select a piece for the " + ai2.getClass().getSimpleName());
		
		gameStatusPnl.add(gameStatusLbl, BorderLayout.NORTH);
		pickedButtonPnl.setPreferredSize(new Dimension(150, 150));
		pickedButtonPnl.setLayout(new GridLayout());
		gameStatusPnl.add(pickedButtonPnl);
		
		exitBtn.setFont(new Font("Arial", Font.BOLD, 45));
		exitBtn.setFocusable(false);
		exitBtn.addActionListener(e -> exitToMenu());
		btnsPanel.add(exitBtn);
		
		btnsPanel.add(Box.createHorizontalStrut(1250));
		
		optBtn.setFont(new Font("Arial", Font.BOLD, 45));
		optBtn.setFocusable(false);
		optBtn.addActionListener(e -> frame.enterGameOptions());
		btnsPanel.add(optBtn);
		
		pieces1.setBackground(Color.green);
		pieces1.setPreferredSize(new Dimension(300, 600));
		pieces1.setLayout(new GridLayout(4,2));
		
		gamePanel.setBorder(BorderFactory.createEmptyBorder(25 , 0, 0, 0));
		
		gamePanel.add(pieces1);
		gamePanel.add(Box.createHorizontalStrut(100));
		
		boardPnl.setBackground(new Color(40,150,150));
		boardPnl.setPreferredSize(new Dimension(850, 850));
		boardPnl.setLayout(new GridLayout(4,4));
		boardPnl.setBorder(BorderFactory.createEmptyBorder(125 , 125, 125, 125));
		
		gamePanel.add(boardPnl);
		gamePanel.add(Box.createHorizontalStrut(100));
		
		pieces2.setBackground(Color.green);
		pieces2.setPreferredSize(new Dimension(300, 600));
		pieces2.setLayout(new GridLayout(4,2));
		pieces2.setBorder(new LineBorder(new Color(10, 115, 115), 6));
		pieces1.setBorder(new LineBorder(new Color(10, 115, 115), 6));
		
		gamePanel.add(pieces2);
		
		resBtn.addActionListener(e -> frame.gameAgain(p1, p2));
		
		add(gamePanel, (BorderLayout.CENTER));
		add(gameStatusPnl, BorderLayout.NORTH);
		add(btnsPanel, BorderLayout.SOUTH);
		
		initializePieces();
	}
	
	private void initializePieces() {
		for(int i = 0; i < Board.BoardSize; i++)
			for(int j = 0; j < Board.BoardSize; j++) {
					offBoardPieceButtons[i][j] = new pieceButton(board.offBoard[i][j]);
					offBoardPieceButtons[i][j].addActionListener(e -> pickPiece((pieceButton)e.getSource()));
					
					if(i < 2)
						pieces1.add(offBoardPieceButtons[i][j]);
					else
						pieces2.add(offBoardPieceButtons[i][j]);
					
					onBoardPieceButtons[i][j] = new pieceButton(i, j);
					onBoardPieceButtons[i][j].addActionListener(e -> placePiece((pieceButton)e.getSource()));
					boardPnl.add(onBoardPieceButtons[i][j]);
			}
	}
	
	private void pickPiece(pieceButton button) {
		if(currentP < 3) {
		    if(gameState == GameLogic.GameState.pick) {
		        gameState = logic.pickPiece(button.getLocation());
		        if(gameState == GameLogic.GameState.place) {
		            pickedButton.updatePieceIcon(button.getTexure());
		            pickedButton.setIconPiece();
		            button.setIconBg();
		            pickedButtonPnl.add(pickedButton);
	
		            changePlayer();
		            
		            gameStatusLbl.setText("Player " + currentP + "'s turn, Please choose where to put your piece");
		            playSound("/assets/sound_effects/pick.wav");  
		            
		            if(currentP >= 3)
		            	if(currentP == p1)
		            		aiPlace(ai1);
		            	else
		            		aiPlace(ai2);
		        }
		        else
		        	gameStatusLbl.setText("You can't pick an empty spot! Please try again!!");
		    }
		}
	}
	
	private void placePiece(pieceButton button) {
		if(currentP < 3) {
    		if(nextPlayer() < 3)
    			gameStatusLbl.setText("Player " + currentP + "'s turn, select a piece for Player " + nextPlayer());
    		else
    			gameStatusLbl.setText("Player " + currentP + "'s turn, select a piece for the " + getAIName());
    		
			if(gameState == GameLogic.GameState.place) {
				gameState = logic.placePiece(button.getLocation());
				
				if(gameState == GameLogic.GameState.pick) {
					button.updatePieceIcon(pickedButton.getTexure());
					button.setIconBoth();
					
					pickedButton.setIcon(null);
					
					playSound("/assets/sound_effects/place.wav");
					if(logic.checkForsedWin() == true && nextPlayer() > 3) gameStatusLbl.setText("I gonna win >:D");
				}
				else if(gameState == GameLogic.GameState.win || gameState == GameLogic.GameState.draw){
					if(gameState == GameLogic.GameState.win)
						gameStatusLbl.setText("Player " + currentP + " Won!!          ");
					else
						gameStatusLbl.setText("Its a Tie!          ");
					button.updatePieceIcon(pickedButton.getTexure());
					button.setIconBoth();
					pickedButtonPnl.remove(pickedButton);

					resBtn.setHorizontalAlignment(SwingConstants.CENTER);
					resBtnPnl.setLayout(new BorderLayout());
					resBtnPnl.add(resBtn, BorderLayout.CENTER);
					resBtn.setFont(new Font("Arial", Font.BOLD, 20));
					resBtn.setFocusable(false);
					resBtnPnl.setBorder(BorderFactory.createEmptyBorder(40 , 15, 40, 15));
					resBtnPnl.setBackground(new Color(25,183,180));
					pickedButtonPnl.add(resBtnPnl);
					
					EasyAI.totalGames++;
				}
				else
					gameStatusLbl.setText("You can't place a piece on top of a piece!");
			}
		}
	}
	
	private void aiPick(EasyAI ai) {
	    if (gameClosed) return;
	    
	    if(gameState == GameLogic.GameState.pick && !gameClosed)
	    	gameStatusLbl.setText(getAIName(ai) + " trying to pick...");

	    SwingWorker<Point, Void> worker = new SwingWorker<Point, Void>() {
	        protected Point doInBackground() throws Exception {
	            Thread.sleep(aiSPEED);
	            return ai.pickPiece();
	        }
	        protected void done() {
	            try {
	                if (gameClosed) return;
	                
	                Point pickLoc = get();
	                
	                logic.pickPiece(pickLoc);
	                pickedButton.updatePieceIcon(offBoardPieceButtons[pickLoc.x][pickLoc.y].getTexure());
	                pickedButton.setIconPiece();
	                offBoardPieceButtons[pickLoc.x][pickLoc.y].setIconBg();
	                pickedButtonPnl.add(pickedButton);
	                
	                changePlayer();
	                
	                gameStatusLbl.setText("Player " + currentP + "'s turn, Please choose where to put your piece");
	                playSound("/assets/sound_effects/pick.wav");
	                gameState = GameLogic.GameState.place;
	                
	                if (currentP >= 3) {
	                    if (ai == ai1)
	                        aiPlace(ai2);
	                    else
	                        aiPlace(ai1);
	                }
	                
	            } catch (InterruptedException | ExecutionException ex) {
	                ex.printStackTrace();
	            }
	        }
	    };
	    worker.execute();
	}

	private void aiPlace(EasyAI ai) {
	    if (gameClosed) return;
	    
	    if(gameState == GameLogic.GameState.place)
	        gameStatusLbl.setText(getAIName(ai) + " trying to place...");
	    
	    SwingWorker<Point, Void> worker = new SwingWorker<Point, Void>() {
	        protected Point doInBackground() throws Exception {
	            Thread.sleep(aiSPEED);
	            return ai.placePiece();
	        }
	        protected void done() {
	            try {
	                if (gameClosed) return;
	                
	                Point placeLoc = get();
	                
	                gameState = logic.placePiece(placeLoc);
	                
	                if (gameState == GameLogic.GameState.pick) {
	                    onBoardPieceButtons[placeLoc.x][placeLoc.y].updatePieceIcon(pickedButton.getTexure());
	                    onBoardPieceButtons[placeLoc.x][placeLoc.y].setIconBoth();
	                    pickedButton.setIcon(null);
	                    playSound("/assets/sound_effects/place.wav");
	                    
	                    aiPick(ai);
	                } else if (gameState == GameLogic.GameState.win || gameState == GameLogic.GameState.draw) {
	                    onBoardPieceButtons[placeLoc.x][placeLoc.y].updatePieceIcon(pickedButton.getTexure());
	                    onBoardPieceButtons[placeLoc.x][placeLoc.y].setIconBoth();
	                    pickedButtonPnl.remove(pickedButton);
	                    resBtn.setHorizontalAlignment(SwingConstants.CENTER);
	                    resBtnPnl.setLayout(new BorderLayout());
	                    resBtnPnl.add(resBtn, BorderLayout.CENTER);
	                    resBtn.setFont(new Font("Arial", Font.BOLD, 20));
	                    resBtn.setFocusable(false);
	                    resBtnPnl.setBorder(BorderFactory.createEmptyBorder(40, 15, 40, 15));
	                    resBtnPnl.setBackground(new Color(25,183,180));
	                    pickedButtonPnl.add(resBtnPnl);
	                    
	                    if (gameState == GameLogic.GameState.win) {
	                        gameStatusLbl.setText(getAIName(ai) + " Won!!          ");
	                        
	                        if (p1 != p2) {
	                            Class<?> aiClass = ai.getClass();
	                            if (aiClass.equals(EasyAI.class))
	                                EasyAI.easyAIW++;
	                            else if (aiClass.equals(MediumAI.class))
	                                MediumAI.meduimAIW++;
	                            else if (aiClass.equals(HardAI.class))
	                                HardAI.HardAIW++;
	                        }
	                        
	                        EasyAI.totalGames++;
	                    }
	                    else if (gameState == GameLogic.GameState.draw)
	                        gameStatusLbl.setText("Its A Tie!!");
	                    
	                }
	                
	            } catch (InterruptedException | ExecutionException ex) {
	                ex.printStackTrace();
	            }
	        }
	    };
	    worker.execute();
	}
	
	private int changePlayer() {
		currentP = (currentP == p1) ? p2 : p1;
		
		return currentP;
	}
	
	private int nextPlayer() {	
		return (currentP == p1) ? p2 : p1;
	}
	
	public String getAIName(EasyAI currentAI) {
	    String name = "";

	    if (currentP >= 3) {
	    	name = (currentP == p1) ? ai1.getClass().getSimpleName() : ai2.getClass().getSimpleName();

	    	if(p1 == p2)
	    		if(currentAI == ai1)
	    			name += "(1)";
	    		else
	    			name += "(2)";
	    }

	    return name;
	}
	
	public String getAIName() {
	    String name = "";

	    if (currentP < 3)
			name = (ai1 != null) ? ai1.getClass().getSimpleName() : ai2.getClass().getSimpleName();

	    return name;
	}
	
	private void playSound(String path) {
	    try {
	        // Use Class.getResource() to get the URL of the resource
	        java.net.URL url = GamePanel.class.getResource(path);
	        if (url == null) {
	            System.err.println("Could not find sound file: " + path);
	            return;
	        }

	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);

	        // Adjust the volume
	        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB = (float) (Math.log10((frame.sfxVolume / 100f) * frame.masterVolume / 100f) / Math.log(10.0) * 50.0);
	        if (dB < -80) dB = -80;
	        gainControl.setValue(dB);

	        clip.start();

	        // Add a listener to close resources when done
	        clip.addLineListener(event -> {
	            if (event.getType() == LineEvent.Type.STOP) {
	                clip.close();
	                try {
						audioInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });
	    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | NullPointerException e) {
	        e.printStackTrace();
	        System.err.println("Could not load or play sound: " + path);
	    }
	}


	
	private void exitToMenu() {
		gameClosed = true;
		frame.enterMenu();
	}
}
