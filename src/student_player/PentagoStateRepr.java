package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;
import pentago_swap.PentagoMove;

public class PentagoStateRepr {
	
	/**
	 * Returns a representation of the state of the game as a 1d array
	 * @param boardState
	 * @return
	 */
	public static double[] stateToArray(PentagoBoardState boardState, int player) {

		PentagoBoardState.Piece p = null;
		if (player == 1) {
			p =  PentagoBoardState.Piece.BLACK;
			
		}
		else {
			p =  PentagoBoardState.Piece.WHITE;

		}

		int nbrtiles = boardState.BOARD_SIZE*boardState.BOARD_SIZE;
		
		double[] state  = new double[nbrtiles * 3]; 
		
		
		for (int i = 0; i < nbrtiles; i++) {
			int pos_y = (int)i/6;
			int pos_x = i - (pos_y*boardState.BOARD_SIZE); 

			if (boardState.getPieceAt(pos_x, pos_y) == p) {

				state[3*i] = 1;
				state[(3*i)+1] = 0; 
				state[(3*i)+2] = 0; 

			}
			else if (boardState.getPieceAt(pos_x, pos_y) == Piece.EMPTY ) {
				state[3*i] = 0;
				state[(3*i)+1] = 0; 
				state[(3*i)+2] = 1; 

			}
			else {
				state[3*i] = 0;
				state[(3*i)+1] = 1; 
				state[(3*i)+2] = 0; 
			}
		}
		return state;
		
	}
	
	public static void print_double(double[] state) {
		 for (int i = 0; i < state.length; i++) {
			 	
	        	System.out.print(state[i]);
	        	if ((i+1) % 3 == 0) {
	        		System.out.print(", ");
	        	}
	        	if ((i+1)% 18  == 0) {
	        		System.out.println();
	        	}
	        }
	}
	
	/**
	 * Given a move returns the index of the move in the move array
	 * @param m
	 * @return
	 */
	public static int move_to_int(PentagoMove m ) {
		int index;
		index = (6 * (m.getMoveCoord().getX() + (6 * m.getMoveCoord().getY())));
		
		if (m.getASwap() == Quadrant.TL && m.getBSwap() == Quadrant.TR ) {
			index += 1;
		}
		
		else if (m.getASwap() == Quadrant.TL && m.getBSwap() == Quadrant.BR ){
			index += 2;
		}
		else if (m.getASwap() == Quadrant.TL && m.getBSwap() == Quadrant.BL ){
			index += 3;
		}
		else if (m.getASwap() == Quadrant.TR && m.getBSwap() == Quadrant.BR ){
			index += 4;
		}
		else if (m.getASwap() == Quadrant.BL && m.getBSwap() == Quadrant.BR ){
			index += 5;
		}
		
		return index;
	}
	
	/**
	 * Gives the index of a move in the array of all moves
	 * @param index
	 * @param playerID
	 * @return
	 */
	public static PentagoMove int_to_move(int index, int playerID) {
		int i = index % 6;
		int pos = (int)(index /6);
		int pos_y = (int)(pos/(6));
		int pos_x = (int)(pos - (pos_y*6)); 
		
		if (i == 1) {
			return new PentagoMove(pos_x, pos_y, Quadrant.TL, Quadrant.TR, playerID);
			
		}
		
		else if (i == 2){
			
			return new PentagoMove(pos_x, pos_y, Quadrant.TL, Quadrant.BR, playerID);

		}
		else if (i == 3){
			return new PentagoMove(pos_x, pos_y, Quadrant.TL, Quadrant.BL, playerID);

		}
		else if (i == 4 ){
			return new PentagoMove(pos_x, pos_y, Quadrant.TR, Quadrant.BR, playerID);

		}
		else if (i == 5){
			return new PentagoMove(pos_x, pos_y, Quadrant.BL, Quadrant.BR, playerID);

		}
		else {
			return new PentagoMove(pos_x, pos_y, Quadrant.BL, Quadrant.TR, playerID);
		}
		
	}
	
}
