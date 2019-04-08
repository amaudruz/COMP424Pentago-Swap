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
		NN2layer model1 = new NN2layer(nn_input_size, nn_layer1neur_nbr, nn_output_size, 0.01, 0);
		NN2layer model2 = new NN2layer(nn_input_size, nn_layer1neur_nbr, nn_output_size, 0.01, 0);
		
		int nbr_of_games = 10000; //the total amount of games both agents will play against each other and so learn
		
		int player1 = 0;
		int player2 = 1;
		
		
		double epsilon = 0.1; //probability of picking a random move
		
		int nbr_acc = 1;
		double[] x0_move_prop = new double[nbr_of_games/nbr_acc];
		double acc = 0;

		for (int i = 0; i < nbr_of_games; i++) {
			
			if (i % 2 == 0) {
				int l = player1;
				player2 = player1;
				player1 = l;
			}
			
			if (i % nbr_acc == 0) acc = 0;
			// construct the game
			PentagoBoardState boardState = new PentagoBoardState();
			
			//counting the illegal moves of player1 to see improvement
			double illegal_moves = 0;
			double all_moves = 0;
		
			double move_number = 0;
			//play the game until one player wins
			while (!boardState.gameOver()) {
				boolean pred = false;
				move_number++;
//				System.out.println(move_number);

				
				//player1 has to play 
				if (boardState.getTurnPlayer() == player1) {
					
					//chooses the move
					int move_index;
					double [] input = PentagoStateRepr.stateToArray(boardState, player1);
					
					if (Math.random() < epsilon) { // chooses a random move (legal or not) with probability epsilon
						move_index = PentagoStateRepr.move_to_int((PentagoMove) boardState.getRandomMove());
					}
					else { // otherwise just a move with reagrds to our policy
						if (boardState.getTurnNumber() < 6) {
							all_moves++;
						}
						pred = true;
						double [] k = model1.predict(input);


						
						move_index = arg_max(mask(k, boardState, player1));
						PentagoMove j= PentagoStateRepr.int_to_move(move_index, player1);
						if (j.getMoveCoord().getX() == 0 && boardState.getTurnNumber() < 6) {
							illegal_moves++;
						}
					}

					
					PentagoMove m = PentagoStateRepr.int_to_move(move_index, player1);
					
					
					//get the reward of the given move and state
					double reward = get_reward(boardState, m, player1);
					
					boardState.processMove(m);

					//get the next state in order to compute Q(s',a')
					PentagoBoardState s = (PentagoBoardState) boardState.clone();
					double[] opponent_pred = model2.predict(PentagoStateRepr.stateToArray(s, player2));
					int opponent_move_index = arg_max(mask(opponent_pred, s, player2));
					
					s.processMove(PentagoStateRepr.int_to_move(opponent_move_index, player2));
					double[] next = PentagoStateRepr.stateToArray(s, player1);

					
					
					//getting data ready to train model
					double target_r = 0; //r + disc * max(Q(s',a'))
					if (!boardState.gameOver()) {
						target_r = reward + discount *(max(mask(model1.predict(next ), s, player1)));


					}

					
					else {
						target_r = reward;
					}


					double [] target = model1.predict(input);
					if (move_index == -1) System.out.println(model1.getb1()[0]);

					target[move_index] = target_r;
			
					
					//train the model 
					model1.train(input, target);
//					MatrixUtil.print_vect(model1.getb1());

					

				}
				
				
				
				
				//player2 plays
				else {
					//chooses the move
					int move_index = 0;
					double [] input = PentagoStateRepr.stateToArray(boardState, player2);
					
					if (Math.random() < epsilon) { // chooses a random move (legal or not) with probability epsilon
						move_index = PentagoStateRepr.move_to_int((PentagoMove) boardState.getRandomMove());
					}
					
					else { // otherwise just a move with reagrds to our policy
						double[] k = model2.predict(input);
						move_index = arg_max(mask(k, boardState, player2));

					}
					
					
					PentagoMove m = PentagoStateRepr.int_to_move(move_index, player2);
					
					//get the reward from the move and state
					double reward = get_reward(boardState, m, player2);
					
					
					boardState.processMove(m);

					//get the next state in order to compute Q(s',a')
					PentagoBoardState s = (PentagoBoardState) boardState.clone();
					double[] opponent_pred = model1.predict(PentagoStateRepr.stateToArray(s, player1));
					int opponent_move_index = arg_max(mask(opponent_pred, s, player1));
					s.processMove(PentagoStateRepr.int_to_move(opponent_move_index, player1));
					double[] next = PentagoStateRepr.stateToArray(s, player2);

					
					double target_r = 0;
					if (!boardState.gameOver()) {
						target_r = reward + discount *(max(mask(model2.predict(next), s, player2)));
					}
					else {
						target_r = reward;
					}
					double[] target = model2.predict(input);
					if (move_index == -1) System.out.println(model2.getb1()[0]);
					target[move_index] = target_r;
					
					model2.train(input, target);

				}
				acc += illegal_moves/all_moves;
				
			}
			
			if (i % nbr_acc == 0) {
				x0_move_prop[i/nbr_acc] = (illegal_moves/nbr_acc);

			}
			
			if (i % 20 == 0) {//every 20 games show the proportion of bad moves taken by  player 1 
				System.out.println("Game " + i + " illegal moves : " + (illegal_moves/all_moves));
			}
			

			
		}
		
		
		write_illmoveprop(x0_move_prop);

		
		model1.write_to_txt("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
		
		
		
		
	}
	
	
	
	private static double[] mask(double[] pred, PentagoBoardState boardState, int playerID) {
		double[] mask = new double[pred.length];
		for (int i = 0; i < mask.length; i++) {
			PentagoMove m = PentagoStateRepr.int_to_move(i, playerID);
			if (!boardState.isLegal(m)) {
				mask[i] = Double.NEGATIVE_INFINITY;
			}
			else {
				mask[i] = 0;
			}
		}
		return MatrixUtil.ew_addition(pred, mask);
	}



	private static void write_illmoveprop(double[] illeg_move_prop) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {

			

			fw = new FileWriter("/home/louis/Documents/github/COMP424Pentago-Swap/data/ill_move_prop.txt");
			bw = new BufferedWriter(fw);
			for (int i = 0; i < illeg_move_prop.length; i++) {
				String s = "";
				s += ((Double)illeg_move_prop[i]).toString();
				s += "\n";
				bw.write(s);
			}

			System.out.println("Done");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

				ex.printStackTrace();

				}	
			}
		
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
	 * @param player1 
	 * @return
	 */
	private static double get_reward(PentagoBoardState boardState, PentagoMove m, int playerID) {
		
		
		PentagoBoardState new_state = (PentagoBoardState) boardState.clone();
		new_state.processMove(m);
		
		if (new_state.gameOver()) {
			if (new_state.getTurnPlayer() == playerID) {
				return 1;
			}
			else {
				return -1;
			}
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
