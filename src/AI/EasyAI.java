package AI;

import java.awt.Point;
import java.util.Random;

import logicComponents.Board;
import logicComponents.GameLogic;

public class EasyAI {
	protected Board board;
	protected GameLogic logic;
	
	public static int totalGames = 0, easyAIW = 0;
	
	public EasyAI(Board board, GameLogic logic) {
		this.board = board;
		this.logic = logic;
	}
	
	public Point pickPiece() {
		while(true) {
			Random rand = new Random();
			int randomPiece = rand.nextInt(Board.PieceNum);
			
			if(board.offBoard[randomPiece / Board.BoardSize][randomPiece % Board.BoardSize] != null)
				return board.offBoard[randomPiece / Board.BoardSize][randomPiece % Board.BoardSize].getPlaceOnBoard();
		}				
	}
	
	public Point placePiece() {
		while(true) {
			Random rand = new Random();
			int randomPlace = rand.nextInt(Board.PieceNum);
			
			if(board.onBoard[randomPlace / Board.BoardSize][randomPlace % Board.BoardSize] == null)
				return new Point(randomPlace / Board.BoardSize, randomPlace % Board.BoardSize);
		}
	}
}
