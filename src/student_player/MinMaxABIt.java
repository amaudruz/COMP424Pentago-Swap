package student_player;

import java.util.ArrayList;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Piece;

/**
 * Implementation of alpha beta pruning with iterative deepening
 * @author louis
 *
 */
public class MinMaxABIt {
	
	/**
	 * Use the algorithm developed here to chose the next move
	 * @param boardState
	 * @return
	 */
	public static Move chooseMmAbItMove(PentagoBoardState boardState, long time) {
		long starttime = System.currentTimeMillis();
    	long endtime = starttime + time;
    	Move m = MinMaxABIt.AlphaBetaIterative(boardState,starttime, endtime, boardState.getTurnPlayer());
        return m == null ? boardState.getRandomMove() : m;
	}
	
	/**
	 * Applies the minmax with alpha-beta pruning and iterative deepening
	 * @param boardState
	 * @param starttime
	 * @param endtime
	 * @param player
	 * @return
	 */
	public static Move AlphaBetaIterative(PentagoBoardState boardState, long starttime, long endtime,  int player) {
		
		int depth = 1;

		PentagoMove best = null;

		while (true) {
			long current = System.currentTimeMillis();
			if (current > endtime) {

				return best;
			}
			
			PentagoMove m = AlphaBeta(boardState, endtime, depth, player);
			if (m!=null) {
				best =  m;
			}
			
			depth++;
		}
		
	}
	
	/**
	 * Root part of the alpha beta pruning with max depth : depth
	 * @param boardState
	 * @param endtime
	 * @param depth
	 * @param player
	 * @param beta 
	 * @param alpha 
	 * @return
	 */
	private static PentagoMove AlphaBeta(PentagoBoardState boardState, long endtime, int depth, int player) {
		PentagoMove best = null;
		double maxValue = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		
		for (PentagoMove m : boardState.getAllLegalMoves()) {
			
			if (System.currentTimeMillis() > endtime) {
				return best;
			}
			
			PentagoBoardState new_boardState = (PentagoBoardState) boardState.clone();
			new_boardState.processMove(m);
			
			double value = min_value(new_boardState, endtime, 1, depth, player, alpha, beta);
			if (value == Double.NaN) {
				break;
			}
			
			alpha = Math.max(alpha, value);
			
			if (value > maxValue) {
				best = m;
				maxValue = value;
			}
		}
		return best;
	}

	/**
	 * Min part of the alpha beta pruning
	 * @param boardState
	 * @param endtime
	 * @param depth
	 * @param maxDepth
	 * @param player
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private static double min_value(PentagoBoardState boardState, long endtime, int depth, int maxDepth, int player, double alpha, double beta) {
		
		double bestVal = Double.POSITIVE_INFINITY;
		
		if (boardState.gameOver() || depth == maxDepth) {
			bestVal = eval(boardState, player);
			return bestVal;
		}
		
		if (System.currentTimeMillis() > endtime) {
			return Double.NaN;
		}
		
		else {
			for (PentagoMove m : boardState.getAllLegalMoves()) {
				
				if (System.currentTimeMillis() > endtime) {
					 return Double.NaN;
				}
				
				
				PentagoBoardState new_boardState = (PentagoBoardState) boardState.clone();
				new_boardState.processMove(m);
				
				double val = max_value(new_boardState, endtime, depth + 1, maxDepth, player, alpha , beta);
				
				if (val == Double.NaN) {
					break;
				}
				
				
				bestVal = Math.min(bestVal, val);
				beta = Math.min(beta, bestVal);
				
				if (beta <= alpha ) {
					break;
				}
					
				
				
			}
		}
		
		return bestVal;
	}

	
	
	
	/**
	 * Return the max part of the algorithm
	 * @param boardState
	 * @param endtime
	 * @param depth
	 * @param maxDepth
	 * @param player
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private static double max_value(PentagoBoardState boardState, long endtime, int depth, int maxDepth, int player,
			double alpha, double beta) {
double bestVal = Double.NEGATIVE_INFINITY;
		
		if (boardState.gameOver() || depth == maxDepth) {
			bestVal = eval(boardState, player);
			return bestVal;
		}
		
		if (System.currentTimeMillis() > endtime) {
			return Double.NaN;
		}
		
		else {
			for (PentagoMove m : boardState.getAllLegalMoves()) {
				
				if (System.currentTimeMillis() > endtime) {
					 return Double.NaN;
				}
				
				
				PentagoBoardState new_boardState = (PentagoBoardState) boardState.clone();
				new_boardState.processMove(m);
				
				double val = min_value(new_boardState, endtime, depth + 1, maxDepth, player, alpha , beta);
				
				if (val == Double.NaN) {
					break;
				}
				
				
				bestVal = Math.max(bestVal, val);
				alpha = Math.max(alpha, bestVal);
				
				if (alpha >= beta ) {
					break;
				}
					
				
				
			}
		}
		
		return bestVal;
	}
	/**
	 * evaluation function
	 * @param boardState
	 * @param player
	 * @return
	 */
	private static double eval(PentagoBoardState boardState, int player) {
		// TODO Auto-generated method stub
		if (boardState.gameOver()) {
			if (boardState.getWinner() == player) {
				return 10000;
			}
			
			else if (boardState.getWinner() == Board.DRAW) {
				return 0;
			}
			
			else return -10000;
		}
		
		else {
			PentagoMove m = new PentagoMove(0, 0, null, null, player);
			return score(boardState, m);
		}
	}
	
	
	
     public static double score(PentagoBoardState state, PentagoMove m) {
		
		
		PentagoBoardState.Piece player_piece = null;
		PentagoBoardState.Piece opponent_piece = null;


		if (m.getPlayerID() == 1) {
			player_piece = PentagoBoardState.Piece.BLACK;
			opponent_piece = PentagoBoardState.Piece.WHITE;
			

		}
		
		else {
			player_piece = PentagoBoardState.Piece.WHITE;
			opponent_piece = PentagoBoardState.Piece.BLACK;
		}
		
		
		//list containing all the lines of pieces of the same color  from the board
		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();
		
		lines.addAll(diagonals_r(state, m , player_piece, opponent_piece));
		lines.addAll(diagonals_l(state, m , player_piece, opponent_piece));
		lines.addAll(horizontal(state, m , player_piece, opponent_piece));
		lines.addAll(vertical(state, m , player_piece, opponent_piece));
		
		double length2lines_ourplayer = lklines(lines, 2, m.getPlayerID());
		double length3lines_ourplayer = lklines(lines, 3, m.getPlayerID());
		double length4lines_ourplayer = lklines(lines, 4, m.getPlayerID());

		
		double length2lines_opponent = lklines(lines, 2, 1 - m.getPlayerID());
		double length3lines_opponent = lklines(lines, 3, 1 - m.getPlayerID());
		double length4lines_opponent = lklines(lines, 4, 1 - m.getPlayerID());

		
		
		double score = ((length4lines_ourplayer - length4lines_opponent) * 20) +((length3lines_ourplayer - length3lines_opponent) * 10) + (length2lines_ourplayer - length2lines_opponent);

		
		return score;
	}




	private static double lklines(ArrayList<boardStateLine> lines, int k, int playerID) {
		double count = 0;
		for (int i = 0; i < lines.size(); i++) {
			

			if (lines.get(i).player == playerID && lines.get(i).get_length() + 1 == k && !lines.get(i).isBlocked()) {
				count++;
			}
		}
		return count;
	}



	public static  ArrayList<boardStateLine> vertical(PentagoBoardState state, PentagoMove m,
			Piece player_piece, Piece opponent_piece) {

		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();
		
		//for all vertical lines 
		for (int i = 0; i < 6 ; i++) {
			//for all element in horizontal line
			double thr = -1;
			for (int j = 0; j < 6; j++) {
//				System.out.println(i + " " + j);
				if ( j > thr) {
					boardStateLine l = null;

					if (state.getPieceAt(i, j) == player_piece) {
						l = new boardStateLine(i, j, 0, 1, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(i, j) == opponent_piece) {
						l = new boardStateLine(i, j, 0, 1, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
						lines.add(l);
						thr = j + l.get_length();
					}
				}
			}
		}
		return lines;
	}



	public static  ArrayList<boardStateLine> horizontal(PentagoBoardState state, PentagoMove m,
			Piece player_piece, Piece opponent_piece) {
		
		
		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();
		
		//for all horizontal lines 
		for (int i = 0; i < 6 ; i++) {
			//for all element in horizontal line
			double thr = -1;
			for (int j = 0; j < 6; j++) {
				if ( j > thr) {
					boardStateLine l = null;

					if (state.getPieceAt(j, i) == player_piece) {
						l = new boardStateLine(j, i, 1, 0, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(j, i) == opponent_piece) {
						l = new boardStateLine(j, i, 1, 0, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
						lines.add(l);
						thr = j + l.get_length();
					}
				}
			}
		}
		return lines;
	}



	public static  ArrayList<boardStateLine> diagonals_l(PentagoBoardState state, PentagoMove m,
			Piece player_piece, Piece opponent_piece) {

		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();

		//adds the lines that are diagonals from in x = +1, y = +1 direction
		
		//first the big diagonal 
		double thr = -1;

		for (int i = 0; i < 6; i++) {
			if ( i > thr) {
				boardStateLine l = null;

				if (state.getPieceAt(5-i, i) == player_piece) {
					l = new boardStateLine(5-i, i, -1, 1, m.getPlayerID(), state);
				}
				
				else if (state.getPieceAt(5-i, i) == opponent_piece) {
					l = new boardStateLine(5-i, i, -1, 1, 1 - m.getPlayerID(), state);

				}
				
				if (l != null) {
					lines.add(l);
					thr = i + l.get_length();
				}
			}

		
		
		}
		//now the other diagonals which can be taken care of by pairs going outwars both sides
		//first loop : for each pair of diagonals diagonals 
		for (int i = 0; i < 5; i++) {
			//second loop for each element in the two diagonals
			double thr1 = -1;
			double thr2 = -1;

			for (int j = 0; j <= i; j++) {
//				System.out.println(i + " " + j);

				//first diagonal
				if (j > thr1 ) {
					boardStateLine l = null;
					
					//if a line starts here 
					
					
					if (state.getPieceAt(i - j, j) == player_piece) {
						l = new boardStateLine(i- j, j, -1, 1, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(i- j, j) == opponent_piece) {
						l = new boardStateLine(i- j, j, -1, 1, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
//						System.out.println("fdiag");

						lines.add(l);
						thr1 = j + l.get_length();
					}
				}
				
				
				
				if (j > thr2) {
					boardStateLine l = null;

					//second diagonal
					if (state.getPieceAt(5 -j, 5 - i + j) == player_piece) {
						l = new boardStateLine(5 - j, 5 - i + j, -1, 1, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(5 - j, 5 - i + j) == opponent_piece) {
						l = new boardStateLine(5 - j, 5 - i + j, -1, 1, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
//						System.out.println("sdiag");

						lines.add(l);
						thr2 = j + l.get_length();

					}
					
				}
				
//				System.out.println(lines.size());

			}
		}
		
		return lines;
	}



	public static ArrayList<boardStateLine> diagonals_r(PentagoBoardState state, PentagoMove m,
			Piece player_piece, Piece opponent_piece) {
		
		
		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();

		//adds the lines that are diagonals from in x = +1, y = +1 direction
		
		//first the big diagonal 
		double thr = -1;

		for (int i = 0; i < 6; i++) {
			if (i > thr) {
				boardStateLine l = null;
				
				if (state.getPieceAt(i, i) == player_piece) {
					l = new boardStateLine(i, i, 1, 1, m.getPlayerID(), state);
				}
				
				else if (state.getPieceAt(i, i) == opponent_piece) {
					l = new boardStateLine(i, i, 1, 1, 1 - m.getPlayerID(), state);

				}
				
				if (l != null) {
					lines.add(l);
					thr = i + l.get_length();
				}
			}

			
		
		
		}
		
		//now the other diagonals which can be taken care of by pairs going outwars both sides
		//first loop : for each pair of diagonals diagonals 
		for (int i = 1; i < 6; i++) {
			//second loop for each element in the two diagonals
			double thr1 = -1;
			double thr2 = -1;

			for (int j = 0; j < 6-i; j++) {
//				System.out.println(i + " " + j);

				
				//first diagonal
				if (j > thr1) {
					boardStateLine l = null;
					
					//if a line starts here 
					
					
					if (state.getPieceAt(i + j, j) == player_piece) {
						l = new boardStateLine(i+j, j, 1, 1, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(i + j, j) == opponent_piece) {
						l = new boardStateLine(i+j, j, 1, 1, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
//						System.out.println("fdiag");

						lines.add(l);
						thr1 = j + l.get_length();
					}
				}
				
				
				
				if (j > thr2) {
					boardStateLine l = null;

					//second diagonal
					if (state.getPieceAt(j, i + j) == player_piece) {
						l = new boardStateLine(j, i + j, 1, 1, m.getPlayerID(), state);
					}
					
					else if (state.getPieceAt(j, i + j) == opponent_piece) {
						l = new boardStateLine(j, i + j, 1, 1, 1 - m.getPlayerID(), state);

					}
					
					if (l != null) {
//						System.out.println("sdiag");

						lines.add(l);
						thr2 = j + l.get_length();

					}
					
				}
				
//				System.out.println(lines.size());
			}
		}
		
		return lines;
	}



	
}
