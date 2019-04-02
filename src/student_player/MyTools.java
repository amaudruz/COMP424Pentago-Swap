package student_player;

import java.util.ArrayList;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    
    /**
     * MinMax algorithm to find the best move, currently not using any eval function
     * @param boardState
     * @return
     */
    public static Move minMax(PentagoBoardState boardState, int myplayer, int maxDepth) {
    	ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
    	System.out.println("Depth 0 branching f : " + legalMoves.size());
    	double maxEval = -10;
    	PentagoMove best = null;
		for (int i = 0; i < legalMoves.size(); i++) {
			PentagoBoardState new_BoardState = (PentagoBoardState)boardState.clone();
			new_BoardState.processMove(legalMoves.get(i));
			double ev = eval(new_BoardState, myplayer, 0, maxDepth);
			if (ev > maxEval) {
				best = legalMoves.get(i);
				maxEval = ev;
			}
		}
		
		return best;
    	}
    
    /**
     * Gives the evaluation of a given move
     * @param boardState
     * @param myplayer
     * @return
     */
	private static double eval(PentagoBoardState boardState, int myplayer, int currentDepth, int maxDepth) {
		if (boardState.gameOver() || currentDepth >= maxDepth) {
			return utility(boardState, myplayer);
		}
		else if (boardState.getTurnPlayer() == myplayer) {
			return max_eval(boardState, myplayer,  currentDepth,  maxDepth);
		}
		
		else {
			return min_eval(boardState, myplayer,  currentDepth,  maxDepth);
		}
	}
	
	/**
	 * Gives the minimum of all the evaluations the next legal moves
	 * @param boardState
	 * @param myplayer
	 * @return
	 */
	private static double min_eval(PentagoBoardState boardState, int myplayer, int currentDepth, int maxDepth) {
		double minEval = 10;
    	ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		for (int i = 0; i < legalMoves.size(); i++) {
			PentagoBoardState new_BoardState = (PentagoBoardState)boardState.clone();
			new_BoardState.processMove(legalMoves.get(i));
			double ev = eval(new_BoardState, myplayer, currentDepth + 1, maxDepth);
			if (ev < minEval) {
				minEval = ev;
			}
		}
		return minEval;
	}

	/**
	 * Gives the maximum of all the evaluations the next legal moves
	 * @param boardState
	 * @param myplayer
	 * @return
	 */
	private static double max_eval(PentagoBoardState boardState, int myplayer, int currentDepth, int maxDepth) {
		// TODO Auto-generated method stub
		double maxEval = -10;
    	ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		for (int i = 0; i < legalMoves.size(); i++) {
			PentagoBoardState new_BoardState = (PentagoBoardState)boardState.clone();
			new_BoardState.processMove(legalMoves.get(i));
			double ev = eval(new_BoardState, myplayer, currentDepth + 1, maxDepth);
			if (ev > maxEval) {
				maxEval = ev;
			}
		}
		return maxEval;
	}
	
	/**
	 * Returns the Utility of a given move 
	 * @param boardState
	 * @param myplayer
	 * @return
	 */
	private static double utility(PentagoBoardState boardState, int myplayer) {
		return 1;
//		if (boardState.getWinner() == myplayer) {
//			return 1;
//		}
//		else if (boardState.getWinner() == Board.DRAW) {
//			return 0;
//		}
//		
//		else {
//			return -1;
//		}
	}
}