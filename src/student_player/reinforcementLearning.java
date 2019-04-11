package student_player;

import pentago_swap.PentagoBoardState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;

/**
 * This is where the reinforcement learning alogrithm is done
 * @author louis
 *
 */
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
		
		int nbr_acc = 1;
		double[] x0_move_prop = new double[nbr_of_games/nbr_acc];
		double acc = 0;

		//for each game 
		for (int i = 0; i < nbr_of_games; i++) {
			//so that each player switch order per game
			if (i % 2 == 1) {
				int l = player1;
				player1 = player2;

				player2 = l;
			}
			
			if (i % nbr_acc == 0) acc = 0;
			// construct the game
			PentagoBoardState boardState = new PentagoBoardState();
			
			
			//play the game until one player wins
			while (!boardState.gameOver()) {
//				boolean pred = false;
//				move_number++;

				
				//player1 has to play 
				if (boardState.getTurnPlayer() == player1) {

					
					//chooses the move
					int move_index;
					double [] input = PentagoStateRepr.stateToArray(boardState, player1);
					//exploration
					if (Math.random() < epsilon) { // chooses a move w,r to minimaxABit algo
						move_index = PentagoStateRepr.move_to_int((PentagoMove) MinMaxABIt.chooseMmAbItMove(boardState, 20));
					}
					//not exploration but the other thing
					else { // otherwise just a move with reagrds to our policy
						double [] k = model1.predict(input);
						move_index = arg_max(mask(k, boardState, player1));
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

					target[move_index] = target_r;
			
					
					//train the model 
					model1.train(input, target);

					

				}
				
				
				
				
				//player2 plays
				else {
					//chooses the move
					int move_index = 0;
					double [] input = PentagoStateRepr.stateToArray(boardState, player2);
					
					//exploration
					if (Math.random() < epsilon) { // chooses a move w,r to minimaxABit algo
						move_index = PentagoStateRepr.move_to_int((PentagoMove) boardState.getRandomMove());
					}
					//not exploration but the other thing
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

					
					//setup the train inp, out
					double target_r = 0;
					if (!boardState.gameOver()) {
						target_r = reward + discount *(max(mask(model2.predict(next), s, player2)));
					}
					else {
						target_r = reward;
					}
					//train the model 
					double[] target = model2.predict(input);
					target[move_index] = target_r;
					
					model2.train(input, target);

				}
				
			}
			
			//decrease exploration each game
			epsilon = epsilon * 0.95;
			

			
			if (i % 20 == 0) {//every 20 games show the proportion of bad moves taken by  player 1 
				System.out.println("Game " + i);
			}
			

			
		}
		
		//write the weights to file for them to be used by player
		model1.write_to_txt("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
		
		
		
		
	}
	
	
	
	/**
	 * Puts -infinty in place of moves that are illegal
	 * @param pred
	 * @param boardState
	 * @param playerID
	 * @return
	 */
	static double[] mask(double[] pred, PentagoBoardState boardState, int playerID) {
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


	/**
	 * this was used to write data on a file to debug
	 * @param illeg_move_prop
	 */
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


	/**
	 * returns the maximun of vector a 
	 * @param a
	 * @return
	 */
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
	public static double get_reward(PentagoBoardState boardState, PentagoMove m, int playerID) {
		//idea : the game is like playing on a 6x6 board where you can't swap but on 
		//6 different boards, so we need to get what the move does to each board
		PentagoBoardState new_state = (PentagoBoardState) boardState.clone();
		new_state.processMove(m);

		if (new_state.gameOver()) {
			if (new_state.getTurnPlayer() == playerID) {
				return 10;
			}
			if (new_state.getWinner() == new_state.getTurnPlayer()){
				return -10;
			}
			else {
				return 0;
			}
		}
		
		// each state represent the board with two quadrant swapped
		
        
        //gets the reward of the move based on how it affected each boards
        double score_after = score(new_state, m);
        double score_before = score(boardState, m);
        
        double alpha = 0.1;
     
        
		return ((score_after - score_before) - (boardState.getTurnNumber()));
	}
	
	/**
	 * Gets the score of a given state
	 * @param state
	 * @param m
	 * @return
	 */
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

		
		
		double score = ((length4lines_ourplayer - length4lines_opponent) * 20) +((length3lines_ourplayer - length3lines_opponent) * 10) + ((length2lines_ourplayer - length2lines_opponent) * 0.1);

		
		return score;
	}



	/**
	 * returns the lines with k pieces of lines
	 * @param lines
	 * @param k
	 * @param playerID
	 * @return
	 */
	private static double lklines(ArrayList<boardStateLine> lines, int k, int playerID) {
		double count = 0;
		for (int i = 0; i < lines.size(); i++) {
			

			if (lines.get(i).player == playerID && lines.get(i).get_length() + 1 == k && !lines.get(i).isBlocked()) {
				count++;
			}
		}
		return count;
	}



	/**
	 * Returns all vertical lines 
	 * @param state
	 * @param m
	 * @param player_piece
	 * @param opponent_piece
	 * @return
	 */
	public static  ArrayList<boardStateLine> vertical(PentagoBoardState state, PentagoMove m,
			Piece player_piece, Piece opponent_piece) {

		ArrayList<boardStateLine> lines = new ArrayList<boardStateLine>();
		
		//for all vertical lines 
		for (int i = 0; i < 6 ; i++) {
			//for all element in horizontal line
			double thr = -1;
			for (int j = 0; j < 6; j++) {
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


	/**
	 * Returns all horizontal lines 
	 * @param state
	 * @param m
	 * @param player_piece
	 * @param opponent_piece
	 * @return
	 */
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


	/**
	 * Returns all diagonal lines going (direction : from br to tl)  
	 * @param state
	 * @param m
	 * @param player_piece
	 * @param opponent_piece
	 * @return
	 */
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


	/**
	 * Returns all diagonal lines going (direction : from br to tl)  
	 * @param state
	 * @param m
	 * @param player_piece
	 * @param opponent_piece
	 * @return
	 */
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
