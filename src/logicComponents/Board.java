package logicComponents;

public class Board {
    public static final int PieceNum = 16;
    public static final int BoardSize = PieceNum/4;
    
    //Properties:
    public Piece[][] onBoard = new Piece[BoardSize][BoardSize];
    public Piece[][] offBoard = new Piece[BoardSize][BoardSize];

    
    //Contractors:
    public Board(){initializePieces();}
    
    
	//Other Functions:
    private void initializePieces() {		
		boolean[] pieceProperties = new boolean[]{false,true};
			
        for (int i = 0; i < BoardSize; i++)
            for (int j = 0; j < BoardSize; j++) {
                offBoard[i][j] = new Piece(pieceProperties[(i/2) % 2],
                                         pieceProperties[i%2],
                                         pieceProperties[(j/2) % 2],
                                         pieceProperties[j%2]);
                
                offBoard[i][j].setPlaceOnBoard(i, j);
            }
	}
}