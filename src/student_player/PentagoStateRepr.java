package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class PentagoStateRepr {
	
	/**
	 * Returns a representation of the state of the game as a 1d array
	 * @param boardState
	 * @return
	 */
	public static int[] stateToArray(PentagoBoardState boardState) {
		
		int nbrtiles = boardState.BOARD_SIZE*boardState.BOARD_SIZE;
		
		int[] state  = new int[nbrtiles * 3]; 
		
		
		for (int i = 0; i < nbrtiles; i++) {
			int pos_y = (int)i/3;
			int pos_x = i - (pos_y*boardState.BOARD_SIZE); 
			
			if (boardState.getPieceAt(pos_x, pos_y) == Piece.WHITE ) {
				state[3*i] = 1;
				state[(3*i)+1] = 0; 
				state[(3*i)+2] = 0; 

			}
			else if (boardState.getPieceAt(pos_x, pos_y) == Piece.BLACK ) {
				state[3*i] = 0;
				state[(3*i)+1] = 1; 
				state[(3*i)+2] = 0; 

			}
			else {
				state[3*i] = 0;
				state[(3*i)+1] = 0; 
				state[(3*i)+2] = 1; 
			}
		}
		return state;
		
	}
	
}
