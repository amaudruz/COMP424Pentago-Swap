package student_player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import pentago_swap.PentagoBoard;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Quadrant;
import pentago_swap.PentagoMove;

/**
 * This was used to test the neural network, can be ignored
 * @author louis
 *
 */
public class NNtest {

	public static void main(String[] args) throws IOException {

//		double[][] input = {{10, 2}, {2, 2}};
//		double[] inn2 = {2, 2};
//		double[][] output = {{4}, {1000}};
//		double[][] outt2 = {{12}, {2}};
//
//	
//		NN2layer model = new NN2layer(0.001, 0);
//		model.test_gradiant(input[0], output[0], 0.00000001);
//		model.test_gradiant(input[1], output[1], 0.00000001);
//
//
//		
//		
//		
//		
//		BufferedWriter bw = null;
//		FileWriter fw = null;
//
//		try {
//
//			String content = "This is the content to write into file\n";
//
//			fw = new FileWriter("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
//			bw = new BufferedWriter(fw);
//			bw.write(content);
//
//			System.out.println("Done");
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		} finally {
//
//			try {
//
//				if (bw != null)
//					bw.close();
//
//				if (fw != null)
//					fw.close();
//
//			} catch (IOException ex) {
//
//				ex.printStackTrace();
//
//			}
//
//		}
		
//		NN2layer model = new NN2layer(2, 10, 2, 0.001, 0);
//		
//		MatrixUtil.print_matr(model.getW1());
//		System.out.println(model.W1_to_String());
//		MatrixUtil.print_matr(model.getW2());
//		System.out.println(model.W2_to_String());
//		MatrixUtil.print_vect(model.getb1());
//		System.out.println(model.b1_to_String());
//		MatrixUtil.print_vect(model.getb2());
//		System.out.println(model.b2_to_String());

		
		
//		try {
//			String weights = "";
//			FileReader f = new FileReader("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
//			BufferedReader b = new BufferedReader(f);
//			weights += b.lines().collect(Collectors.joining());
//			System.out.println(weights);
//			System.out.println("james");
//			b.close();
//		
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		NN2layer model = new NN2layer("/home/louis/Documents/github/COMP424Pentago-Swap/data/weights.txt");
//		boolean b = true;
//		for (int i= 0; i < 216; i++) {
//			PentagoMove m = PentagoStateRepr.int_to_move(i, 0);
//			int  j = PentagoStateRepr.move_to_int(m);
//			
//			if (i != j) b = false;
//
//		}
//		System.out.println(b);
		
//		NN2layer model = new NN2layer(2, 10, 1, 0.001, 0);
//		double[][] input = {{0, 1}};
//		double[][] output = {{10}};
//		
//		model.train_on_batch(input, output, 1000);
//		double [] j = model.predict(input[0]);
//		System.out.println(j[0]);
		
		PentagoBoardState s = new PentagoBoardState();
		PentagoMove m1 = new PentagoMove(0, 0, Quadrant.TR, Quadrant.BR, 0);
		PentagoMove m2 = new PentagoMove(4, 0, Quadrant.TR, Quadrant.BR, 0);
		PentagoMove m3= new PentagoMove(0, 0, Quadrant.TR, Quadrant.BR, 0);
		PentagoMove m4 = new PentagoMove(0, 0, Quadrant.TR, Quadrant.BR, 0);


//		s.processMove(m);
		s.printBoard();
//		reinforcementLearning.swap_quandrants(s, q1, q2);
	}

}
