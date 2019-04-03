package student_player;

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
			
			if (current > endtime) {
				System.out.println(depth);
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
			
			double[] values = min_value(new_boardState, endtime, 1, depth, player, alpha, beta);
			if (values == null) {
				continue;
			}
			alpha = values[1];
			beta = values[2];
			
			if (values[0] > maxValue) {
				maxValue = values[0];
				best = m;
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
	private static double[] min_value(PentagoBoardState boardState, long endtime, int depth, int maxDepth, int player, double alpha, double beta) {
		double[] values = new double[3]; 
		values[1] = alpha;
		values[2] = beta;
		values[0] = Double.POSITIVE_INFINITY;

		
		if (boardState.gameOver() || depth == maxDepth) {
			values[0] = eval(boardState, player);
			return values;
		}
		
		if (System.currentTimeMillis() < endtime) {
			return null;
		}
		
		else {
			for (PentagoMove m : boardState.getAllLegalMoves()) {
				if (System.currentTimeMillis() > endtime) {
					return null;
				}
				
				
				PentagoBoardState new_boardState = (PentagoBoardState) boardState.clone();
				new_boardState.processMove(m);
				
				double[] newValues = max_value(new_boardState, endtime, depth + 1, maxDepth, player, values[1] , values[2]);
				
				if (values[0] > newValues[0]) {
					values[0] = newValues[0];
				}
				if (newValues[0] <= values[1] ) {
					return values;
				}
					
				if (values[2] > newValues[2]) {
					values[2] = newValues[2];
				}
				
				
			}
		}
		
		return values;
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
	private static double[] max_value(PentagoBoardState boardState, long endtime, int depth, int maxDepth, int player,
			double alpha, double beta) {
		double[] values = new double[3]; 
		values[1] = alpha;
		values[2] = beta;
		values[0] = Double.NEGATIVE_INFINITY;

		
		if (boardState.gameOver() || depth == maxDepth) {
			values[0] = eval(boardState, player);
			return values;
		}
		
		if (System.currentTimeMillis() < endtime) {
			return null;
		}
		
		else {
			for (PentagoMove m : boardState.getAllLegalMoves()) {
				if (System.currentTimeMillis() > endtime) {
					return null;
				}
				
				
				PentagoBoardState new_boardState = (PentagoBoardState) boardState.clone();
				new_boardState.processMove(m);
				
				double[] newValues = min_value(new_boardState, endtime, depth + 1, maxDepth, player, values[1] , values[2]);
				
				if (values[0] < newValues[0]) {
					values[0] = newValues[0];
				}
				if (newValues[0] >= values[2] ) {
					return values;
				}
					
				if (values[1] < newValues[1]) {
					values[1] = newValues[1];
				}
				
				
			}
		}
		
		return values;
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
			return boardState.getWinner() == player ? 1 : -1;
		}
		
		else {
			return (Math.random() * 2) - 1;
		}
	}

	
}
