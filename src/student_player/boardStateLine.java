package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoCoord;


/**
 * Dtastructure to represent lines in board (horzontal, diagonal or vertical)
 * @author louis
 *
 */
public class boardStateLine {
	//position of first element 
	private int x_begining;
	private int y_beginnig;

	//direction of the line 
	private int xdir;
	private int ydir;
	
	//size (legth + 1 pieces)
	private int length;
	
	//the player who has this line 
	public int player;
	private PentagoBoardState state;
	
	private Piece ourpiece;
	private Piece opponent;
	
	public boardStateLine(int x_begining, int y_beginnig, int xdir, int ydir, int player, PentagoBoardState state) {
		this.x_begining = x_begining;
		this.y_beginnig = y_beginnig;

		this.length = 0;
		this.player = player;
		this.xdir = xdir;
		this.ydir = ydir;
		PentagoBoardState.Piece ourpiece = null;
		PentagoBoardState.Piece opponentpiece = null;

		if (player == 1) {
			this.ourpiece =  PentagoBoardState.Piece.BLACK;
			this.opponent =  PentagoBoardState.Piece.WHITE;

		}
		else {
			this.ourpiece =  PentagoBoardState.Piece.WHITE;
			this.opponent =  PentagoBoardState.Piece.BLACK;

		}		 
		this.state = state;
		this.adjust_size(state);
//		System.out.println("james " + this.length);
	}

	/**
	 * Adjust the length of the pieces together 
	 * @param state
	 */
	private void adjust_size(PentagoBoardState state) {
		int xPos = this.x_begining;
		int yPos = this.y_beginnig;
		PentagoBoardState.Piece p = null;
		xPos += xdir;
		yPos += ydir;
		if (player == 1) {
			p =  PentagoBoardState.Piece.BLACK;
			
		}
		else {
			p =  PentagoBoardState.Piece.WHITE;

		}		 
		 
		while (xPos < 6  && yPos < 6 && yPos >= 0 && xPos >= 0 && state.getPieceAt(xPos, yPos) == p) {
//			System.out.println("jajaja");
			this.length++;
			xPos += xdir;
			yPos += ydir;
		}
	}
	
	/**
	 * Return true if the line contains case x, y
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(int x, int y) {
		boolean res = false;
		for (int i = 0; i <= length; i++) {
			if ((this.x_begining + (i*xdir) == x) && (this.y_beginnig + (i*ydir) == y)) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * Returns true if the piece can expand this line 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean is_on_line_path(int x, int y) {
		boolean res = false;
		int xPosBehind = this.x_begining - xdir;
		int yPosBehind = this.y_beginnig - ydir;
		//if the piece is on the line just behind the beginning piece
		if (x == xPosBehind && y == yPosBehind) {
			res = true;
		}
		int xPosFront = this.x_begining + ((this.length + 1)*xdir);
		int yPosFront = this.y_beginnig - ((this.length + 1)*ydir);
		//if the piece is on the line just in front of the last piece
		if (x == xPosFront && y == yPosFront) {
			res = true;
		}

		return res;
	}
	
	public int get_length() {
		return this.length;
	}
	
	/**
	 * See if line is blocked on the sides (if it can grow to have more elements)
	 * @return
	 */
	public boolean isBlocked() {
		boolean blockedbehind = false;
		int xbehind = this.x_begining - this.xdir;
		int ybehind = this.y_beginnig - this.ydir;
		if (xbehind >= 6 || xbehind < 0 || ybehind >= 6 || ybehind < 0) {
//			System.out.println("james1");
			blockedbehind = true;
		}
		else if (state.getPieceAt(xbehind, ybehind) == opponent)  {
			blockedbehind = true;

		}
		
		boolean blockedfront = false;
		int xfront = this.x_begining + (this.xdir * (this.length+ 1));
		int yfront = this.y_beginnig + (this.ydir * (this.length+ 1));
		if (xfront >= 6|| xfront <0 || yfront >= 6 || yfront < 0 ) {
//			System.out.println("james2");

			blockedfront = true;
		}
		
		else if (state.getPieceAt(xfront, yfront) == this.opponent)  {
			blockedfront = true;

		}

		
		return blockedbehind && blockedfront;
	}
	 
}
