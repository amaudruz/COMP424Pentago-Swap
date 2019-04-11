package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.RandomPentagoPlayer;

/**
 * To test student player vs random player
 * @author louis
 *
 */
public class PlayervsRandomtest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StudentPlayer rand1 = new  StudentPlayer();
		RandomPentagoPlayer rand2 = new  RandomPentagoPlayer();
		
		int nbr_games = 1000;
		
		int rand1_wins = 0;
		int rand2_wins = 0;
		int draws = 0;
		
		
		for (int i = 0; i < nbr_games; i++) {
			PentagoBoardState boardState = new PentagoBoardState();
			
			
			
			while (!boardState.gameOver()) {
				if (boardState.getTurnPlayer() == 0) {
		
					boardState.processMove((PentagoMove)rand1.chooseMove(boardState));
				}
				
				else {
					boardState.processMove((PentagoMove)rand2.chooseMove(boardState));

				}
				
			}
			
			if (boardState.getWinner() == 0) {
				rand1_wins++;
				
			}
			else if (boardState.getWinner() == 1){
				rand2_wins++;
			}
			
			else {
				draws++;
			}
		}
		
		System.out.println("stud wins : " + rand1_wins);
		System.out.println("rand2 wins : " + rand2_wins);
		System.out.println("Draws : " + draws);


		

	}

}
