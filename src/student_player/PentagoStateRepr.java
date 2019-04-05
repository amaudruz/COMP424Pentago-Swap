package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class PentagoStateRepr {
	
	/**
	 * Returns a representation of the state of the game as a 1d array
	 * @param boardState
	 * @return
	 */
	public static int[] stateToArray(PentagoBoardState boardState, int player) {
		PentagoBoardState.Piece p = null;
		if (player == '1') {
			p =  PentagoBoardState.Piece.BLACK;
			
		}
		else {
			p =  PentagoBoardState.Piece.WHITE;

		}

		int nbrtiles = boardState.BOARD_SIZE*boardState.BOARD_SIZE;
		
		int[] state  = new int[nbrtiles * 3]; 
		
		
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
	
	public static void print_int(int[] state) {
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
	
}
