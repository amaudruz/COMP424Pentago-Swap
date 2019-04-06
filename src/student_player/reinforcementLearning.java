package student_player;

import pentago_swap.PentagoBoardState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import pentago_swap.PentagoMove;

public class reinforcementLearning {
	
	private static final String dest = "/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt";
	
	public NN2layer model1;
	public NN2layer model2;

	public static void main(String[] args) {
		double discount = 0.9;
		
		//computes the specification of both nural networks (one for each player)
		int nn_input_size = PentagoBoardState.BOARD_SIZE *PentagoBoardState.BOARD_SIZE * 3; 
		int nn_layer1neur_nbr = 150;
		int nn_output_size = 6 * 36; // nbr of possible moves 
		
		//one model for each of the player Q function 
		NN2layer model1 = new NN2layer(nn_input_size, nn_layer1neur_nbr, nn_output_size, 0.001, 0);
		NN2layer model2 = new NN2layer(nn_input_size, nn_layer1neur_nbr, nn_output_size, 0.001, 0);
		
		int nbr_of_games = 10000; //the total amount of games both agents will play against each other and so learn
		
		int player1 = 0;
		int player2 = 1;
		
		
		double epsilon = 0.1; //probability of picking a random move
		
		for (int i = 0; i < nbr_of_games; i++) {
			// construct the game
			PentagoBoardState boardState = new PentagoBoardState();
			double ill_moves = 0;
			double moves = 0;
//			
//			if ( i % 2 == 0 ) {
//				player1 = 0;
//				player2 = 1;
//			}
//			
//			else {
//				player1 = 1;
//				player2 = 0;
//			}
			
			
			
			//play the game until one player wins
			while (!boardState.gameOver()) {
				
				
				
				
				
				//player1 has to play 
				
				
				
				
				if (boardState.getTurnPlayer() == player1) {
//					System.out.println("p1 picks");

					//chooses the move
					int move_index = 0;
					double [] input = PentagoStateRepr.stateToArray(boardState, player1);
					
					if (Math.random() < epsilon) { // chooses a random move (legal or not) with probability epsilon
						move_index = (int) ((Math.random() * nn_output_size));
					}
					
					else { // otherwise just a move with reagrds to our policy
						double [] k = model1.predict(input);
						if (Math.random() < 0.0001) MatrixUtil.print_vect(k);
						move_index = arg_max(k);
//						System.out.println( " mi2 : " +move_index);
					}
					
					
					PentagoMove m = PentagoStateRepr.int_to_move(move_index, player1);
					//get the reward of the given move and state
					double reward = get_reward(boardState, m);
					if (boardState.isLegal(m)) {
						boardState.processMove(m);
					}
					else {
//						System.out.println("ilegal p1");
					}
					
					double[] next_state  = PentagoStateRepr.stateToArray(boardState, player1);

					double target_r = 0;
					//learn from the move 
					if (!boardState.gameOver()) {
						target_r = reward + discount *(max(model1.predict(next_state)));
					}
					
					else {
						target_r = reward;
					}
					double [] target = model1.predict(input);
					
					target[move_index] = target_r;
					
					model1.train(input, target);
//					System.out.println("p1 moved");

				}
				
				
				
				
				
				
				
				
				//player2 plays
				
				
				
				
				
				else {

					//chooses the move
					int move_index = 0;
					double [] input = PentagoStateRepr.stateToArray(boardState, player2);
					
					if (Math.random() < epsilon) { // chooses a random move (legal or not) with probability epsilon
						move_index = (int) ((Math.random() * nn_output_size)) ;
					}
					
					else { // otherwise just a move with reagrds to our policy
						double[] k = model2.predict(input);
						move_index = arg_max(k);
					}
					
					
					PentagoMove m = PentagoStateRepr.int_to_move(move_index, player2);
					
					//get the reward of the given move and state
					double reward = get_reward(boardState, m);
					
					if (boardState.isLegal(m)) {

						boardState.processMove(m);
					}
					else {

					}
					
					double[] next_state  = PentagoStateRepr.stateToArray(boardState, player2);
					double target_r = 0;
					//learn from the move 
					if (!boardState.gameOver()) {
						target_r = reward + discount *(max(model2.predict(next_state)));
					}
					else {
						target_r = reward;
					}
					double [] target = model2.predict(input);
					
					target[move_index] = target_r;
					
					model2.train(input, target);


				}
				
			}
//			if (i%20 == 0) {
//				System.out.println(ill_moves/moves);
//			}
			
			
		}
		

		
		model1.write_to_txt("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
		
		
		
		
	}
	
	
	
	private static double max(double[] a) {
		double max_value = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			max_value = Math.max(a[i], max_value);
		}
		return max_value;
	}



	/**
	 * Returns the reward of an action given a starting state
	 * @param boardState
	 * @param m
	 * @return
	 */
	private static double get_reward(PentagoBoardState boardState, PentagoMove m) {
		if (!boardState.isLegal(m)) return -10000;
		PentagoBoardState new_state = (PentagoBoardState) boardState.clone();
		new_state.processMove(m);
		if (new_state.gameOver()) {
			return new_state.getWinner() == m.getPlayerID() ? 1 : 0; 
		}
		return 0;
	}

	/**
	 * Returns the index of the max element in the list
	 * @param list
	 * @return
	 */
	public static int arg_max(double[] list) {
		int index = -1;
		double max_value = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < list.length; i++) {
			if (list[i] > max_value) {
				index = i;
				max_value = list[i];
			}
			
		}
		return index;
	}
}
