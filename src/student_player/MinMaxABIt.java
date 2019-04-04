package student_player;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MinMaxABIt {
	
	/**
	 * Use the algorithm developed here to chose the next move
	 * @param boardState
	 * @return
	 */
	public static Move chooseMmAbItMove(PentagoBoardState boardState) {
		long starttime = System.currentTimeMillis();
    	long endtime = starttime + 1800;
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
			 System.out.println(depth);
			if (current > endtime) {
				System.out.println(2);

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
				return 1;
			}
			
			else if (boardState.getWinner() == Board.DRAW) {
				return 0;
			}
			
			else return -1;
		}
		
		else {
			return (Math.random() * 2) - 1;
		}
	}

	
}
