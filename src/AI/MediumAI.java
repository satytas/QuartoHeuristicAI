package AI;

import java.awt.Point;
import java.util.Random;

import logicComponents.Board;
import logicComponents.GameLogic;
import logicComponents.Piece;

public class MediumAI extends EasyAI{
	public static int meduimAIW = 0;
	protected int gradeForMove[][];

	public MediumAI(Board board, GameLogic logic) {
		super(board, logic);
	}
	
	protected GameLogic.GameState checkMoveResult(int x, int y) {
		GameLogic.GameState result;
		
		if(board.onBoard[x][y] == null) {
			board.onBoard[x][y] = logic.getPickedPiece();
			logic.increasePlacedPiecesNum();;
			
			result = logic.Quarto(new Point(x,y));
			
			board.onBoard[x][y] = null;
			logic.decreasePlacedPieceNum();
			
			return result;
		}
		else
			return null;
	}
	
	protected int checkPickResult(int x, int y) {
		int grade = 3;
		
		Piece tmpPiece = logic.getPickedPiece();
		logic.setPieckedPiece(board.offBoard[x][y]);
		
			for(int i = 0; i < Board.BoardSize && grade != 1; i++)
				for(int j = 0; j < Board.BoardSize && grade != 1; j++) {
					GameLogic.GameState result = checkMoveResult(i, j);
					
					if(result != null)
						switch(result) {
							case resume:
								if(grade >= 3)
									grade = 3;
								break;
							case draw:
								if(grade > 2)
									grade = 2;
								break;
							case win:
								if(grade > 1)
									grade = 1;
						default:
							break;
						}
				}
			
			logic.setPieckedPiece(tmpPiece);
		
		return grade;
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
	
	private int setGradeFormRes(GameLogic.GameState result) {
		if(result == null)
			return 0;
		
		if(result == GameLogic.GameState.resume)
			return 1;
		
		if(result == GameLogic.GameState.draw)
			return 2;
		
		return 3;
	}
	
	public Point placePiece() {
		gradeForMove = new int[Board.BoardSize][Board.BoardSize];
		int highestGrade = 0;
		
		for(int i = 0; i < Board.BoardSize; i++)
			for(int j = 0; j < Board.BoardSize; j++) {
					gradeForMove[i][j] = setGradeFormRes(checkMoveResult(i, j));
				
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
