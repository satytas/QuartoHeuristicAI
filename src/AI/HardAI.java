package AI;

import java.awt.Point;
import java.util.Random;

import logicComponents.Board;
import logicComponents.GameLogic;
import logicComponents.Piece;

public class HardAI extends MediumAI{
	public static int HardAIW = 0;
	
	public HardAI(Board board, GameLogic logic) {
		super(board, logic);
	}
	
	protected boolean checkFW(int x, int y) {	
		boolean result;
		
			board.onBoard[x][y] = logic.getPickedPiece();
			logic.increasePlacedPiecesNum();;
			
			result = logic.checkForsedWin();
			
			board.onBoard[x][y] = null;
			logic.decreasePlacedPieceNum();
			
			return result;
	}
	
	public Point pickPiece() {
		gradeForMove = new int[Board.BoardSize][Board.BoardSize]; 
		int highestGrade = 1;
			
			for(int i = 0; i < Board.BoardSize; i++)
				for(int j = 0; j < Board.BoardSize; j++) {
					int result = checkPickResult(i, j);
					
					if(board.offBoard[i][j] != null)
						gradeForMove[i][j] = result;
					else
						gradeForMove[i][j] = 1;
					
					if (gradeForMove[i][j] > highestGrade)
					    highestGrade = gradeForMove[i][j];

				}
			
		while (true) {
			Random rand = new Random();
			int randomPiece = rand.nextInt(Board.PieceNum);
			Piece pickPiece = board.offBoard[randomPiece / Board.BoardSize][randomPiece % Board.BoardSize];
			
			if(pickPiece != null && gradeForMove[randomPiece / Board.BoardSize][randomPiece % Board.BoardSize] == highestGrade)
				return pickPiece.getPlaceOnBoard();
		}	
	}
	
	public Point placePiece() {
		gradeForMove = new int[Board.BoardSize][Board.BoardSize];
		int highestGrade = 0;
		
		for(int i = 0; i < Board.BoardSize; i++)
			for(int j = 0; j < Board.BoardSize; j++) {
				GameLogic.GameState result = checkMoveResult(i, j);
				if(result != null)
					switch(result) {
						case resume:
							if(checkFW(i, j) == true)
								gradeForMove[i][j] = 1;
							else 
								gradeForMove[i][j] = 2;
							break;
						case draw:
							gradeForMove[i][j] = 3;
							break;
						case win:
							gradeForMove[i][j] = 4;
					default:
						break;
					}
				else
					gradeForMove[i][j] = 0;
				
				if(gradeForMove[i][j] > highestGrade)
					highestGrade = gradeForMove[i][j];
			}
		
		
		while(true) {
			Random rand = new Random();
			int randomPlace = rand.nextInt(Board.PieceNum);
			
			if(this.board.onBoard[randomPlace / Board.BoardSize][randomPlace % Board.BoardSize] == null)
				if(gradeForMove[randomPlace / Board.BoardSize][randomPlace % Board.BoardSize] == highestGrade)
					return new Point(randomPlace / Board.BoardSize, randomPlace % Board.BoardSize);
		}
	}
}
