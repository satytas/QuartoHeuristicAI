package logicComponents;

import java.util.HashMap;

import java.awt.Point;

public class Piece {
	public static String properties[] = {"Color", "Hight", "Shape", "Top"};
	
	//Properties:
	private HashMap<String, Boolean> Properties = new HashMap<String, Boolean>();
	private Point placeOnBoard = new Point(); 

	
	//Contractor:
	public Piece(Boolean isWhite, Boolean isTall, Boolean isSquare, Boolean isFull){
		Properties.put("Color", isWhite);
		Properties.put("Hight", isTall);
		Properties.put("Shape", isSquare);
		Properties.put("Top", isFull);
	}

	
	//Other Functions:
	public String getName(){
		String name = (getProperty("Color") ? "Wht" : "Blk") +
						(getProperty("Hight") ? "Tal" : "Srt") +
						(getProperty("Shape") ? "Sqr" : "Cir") +
						(getProperty("Top") ? "Ful" : "Emp");

		return name;
	}
	
	
	//G&S:
	public Point getPlaceOnBoard() {return placeOnBoard;}

	public void setPlaceOnBoard(int row, int col) {placeOnBoard.setLocation(row,col);}

	public boolean getProperty(String property){return Properties.get(property);}
	public boolean getProperty(int property){return Properties.get(properties[property]);}
}