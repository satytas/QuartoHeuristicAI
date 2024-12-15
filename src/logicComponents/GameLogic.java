package logicComponents;

import java.awt.Point;
import java.util.HashMap;

public class GameLogic {
	public enum GameState {resume, win, draw, pick, place}
	
	//Properties:
    private Board board;


    private HashMap<String, Boolean> propertiesOnBoard = new HashMap<String, Boolean>();

	private int placedPiecesNum = 0;
	private Piece pickedPiece;
	
	//Contractor:
    public GameLogic(Board board){
    	this.board = board;
    }
    
    
    //Other Functions:
    public GameState pickPiece(Point pieceLoc){
    	pickedPiece = board.offBoard[pieceLoc.x][pieceLoc.y];
    	
            if(pickedPiece == null)
                return GameState.pick;
            else {
            	board.offBoard[pieceLoc.x][pieceLoc.y] = null;
            	return GameState.place;
			}   
    }
    
    
    public GameState placePiece(Point placeLoc){
    	if(board.onBoard[placeLoc.x][placeLoc.y] == null) {
    		board.onBoard[placeLoc.x][placeLoc.y] = pickedPiece;
    		placedPiecesNum++;
    		
    		GameState game = Quarto(placeLoc);
    		
    		if(game == GameState.resume)
    			return GameState.pick;
    		
    		else if(game == GameState.draw)
    			return GameState.draw;
    		else
    			return GameState.win;
    	}
    	else {
    		placedPiecesNum--;
            return GameState.place;
    	}
    }
    
    public GameState Quarto(Point placedPieceLocation) {
        if (placedPiecesNum < Board.BoardSize)
            return GameState.resume;
        else if (placedPiecesNum == Board.PieceNum)
            return GameState.draw;

        int row = placedPieceLocation.x, col = placedPieceLocation.y;
        GameState result = GameState.resume;

        if (!checkLine(row, 0, 0, 1, 4).isEmpty())
            result = GameState.win;
        if (!checkLine(0, col, 1, 0, 4).isEmpty())
            result = GameState.win;
        if (row == col && !checkLine(0, 0, 1, 1, 4).isEmpty())
            result = GameState.win;
        if (row + col == Board.BoardSize - 1 && !checkLine(Board.BoardSize - 1, 0, -1, 1, 4).isEmpty())
            result = GameState.win;

        return result;
    }    
    
    public boolean checkForsedWin() {
    	if(placedPiecesNum < 3)
    		return false;
    	
    	propertiesOnBoard.clear();
    	
    	for(int i = 0; i < Board.BoardSize; i++) {
    		if(checkAndUpdateProps(checkLine(i, 0, 0, 1, Board.BoardSize-1))) return true;
    		if(checkAndUpdateProps(checkLine(0, i, 1, 0, Board.BoardSize-1))) return true;
    	}
    	
    	if(checkAndUpdateProps(checkLine(0, 0, 1, 1, 3))) return true;
    	if(checkAndUpdateProps(checkLine(Board.BoardSize-1, 0, -1, 1, Board.BoardSize-1))) return true;
    	
    	return false;
    }
    
    private HashMap<String, Boolean> checkLine(int startX, int startY, int stepX, int stepY, int neededPieces) {
    	HashMap<String, Boolean> machingProperties = new HashMap<String, Boolean>();
    	int piecesCnt = 0;
    	
        for (int i = 0; i < Board.BoardSize; i++)
            if (board.onBoard[startX + i * stepX][startY + i * stepY] != null)
            	piecesCnt++;
            
        if(piecesCnt != neededPieces)
        	return machingProperties;

        Piece startPiece;
        int startPos = 0;
        if(board.onBoard[startX][startY] != null)
        	startPiece = board.onBoard[startX][startY];
        else {
        	startPos++;
        	startPiece =  board.onBoard[startX + startPos*stepX][startY + startPos*stepY];
		}
        
        for (String property : Piece.properties) {
	        boolean propertyMatched = true, expectedPropertyValue = startPiece.getProperty(property);
	        	
	        for (int j = startPos+1; j < Board.BoardSize && propertyMatched == true; j++)
	        	if(board.onBoard[startX + j*stepX][startY + j*stepY] != null)
	        		if(expectedPropertyValue != board.onBoard[startX + j*stepX][startY + j*stepY].getProperty(property))
		        		propertyMatched = false;
	        
	        if (propertyMatched)
	        	machingProperties.put(property, expectedPropertyValue);
        }
        
        return machingProperties;
    }
    
    private boolean checkAndUpdateProps(HashMap<String, Boolean> lineProperties) {
    	if(lineProperties.isEmpty())
    		return false;
    	
	    for(String property : lineProperties.keySet())
	        if(propertiesOnBoard.containsKey(property) && lineProperties.get(property) != propertiesOnBoard.get(property))
	        	return true;

	    for(int col = 0; col < Board.BoardSize; col++)
	    	for(int row = 0; row < Board.BoardSize; row++)
	    		if(board.offBoard[col][row] != null) {
	    			Piece goodPiece = board.offBoard[col][row];
	    			Boolean allPropsOpiset = true;
	    			for(String property : lineProperties.keySet()) {
	    				if(lineProperties.get(property) == goodPiece.getProperty(property))
	    					allPropsOpiset = false;
	    			}
	    			if(allPropsOpiset) {
	    			    propertiesOnBoard.putAll(lineProperties);
	    			    lineProperties.clear();
	    		    	return false;
	    			}
	    		}
	    return true;
    }
    
    //G&S:
    public Piece getPickedPiece() {return pickedPiece;}
    public void setPieckedPiece(Piece pickedPiece) {this.pickedPiece = pickedPiece;}
    public void increasePlacedPiecesNum() {placedPiecesNum++;}
    public void decreasePlacedPieceNum() {placedPiecesNum--;}
}