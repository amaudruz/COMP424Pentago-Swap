package student_player;

import java.io.IOException;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {
	public double count = 0;
	double o= 0;
	public NN2layer model;
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260872326");
        System.out.println("np");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
    	if (boardState.getTurnNumber() == 0) {
    		try {
				this.model = new NN2layer("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	double[] pred = model.predict(PentagoStateRepr.stateToArray(boardState, boardState.getTurnPlayer()));
    	PentagoBoardState news = (PentagoBoardState) boardState.clone();
    	news.processMove((PentagoMove) news.getRandomMove());
    	news.processMove((PentagoMove) news.getRandomMove());
    	double[] pred2 = model.predict(PentagoStateRepr.stateToArray(news, boardState.getTurnPlayer()));

    	double[] diff = new double[pred2.length];
    	for (int i =0 ; i < pred.length; i++) {
    		diff[i] = Math.abs(pred[i] - pred2[i]);
    	}
    	
    	for (int i =0 ; i < pred.length; i++) {
    		System.out.print(diff[i] + ", ");
    	}
    	
    	int move_index = reinforcementLearning.arg_max(pred);
        PentagoMove m = PentagoStateRepr.int_to_move(move_index, boardState.getTurnPlayer());
        o++;
        if (boardState.isLegal(m)) {
        	
        	return m;
        }
        else {
        	count += 1;
        	System.out.println("model move : " + count/o );
        	System.out.println("rand move");
        	return boardState.getRandomMove();
        }
    }
    
}