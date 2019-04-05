package student_player;

import java.util.ArrayList;

public class NNtest {

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();
//		ArrayList<Double> output = new ArrayList<Double>();
//		for (double i = 0; i < 10 ; i++) {
//			
//			for (double j = 0; j < 10; j++) {
//				ArrayList<Double> ij = new ArrayList<Double>();
//				ij.add(i);
//				ij.add(j);
//				input.add(ij);
//			}
//			
//		}
//		
//		double[][] in = new double[input.size()][2];
//		double[][] out = new double[input.size()][1];
//
//		
//		for (int k = 0; k< input.size(); k++) {
//			in[k][0] = input.get(k).get(0);
//			in[k][1] = input.get(k).get(1);
//			out[k][0] = (in[k][0]*in[k][0]) + in[k][1];
////			out[k][0] = in[k][0]+ in[k][1];
//
//			System.out.println(in[k][0] + " " + in[k][1] + " " + out[k][0]);
//			
//		}
//		
		
		
		
//		
//		NN2layer model= new NN2layer(0.0001, 0);
//		
//
////		model.train_on_batch(in, out, 10000);
		double[][] input = {{10, 2}, {2, 2}};
		double[] inn2 = {2, 2};
		double[][] output = {{4}, {1000}};
		double[][] outt2 = {{12}, {2}};
//
//
//		for (int i = 0; i < 10000; i++) {
//			model.train(inn, outt);
//			model.train(inn2, outt2);
//
//		}
//		double[] pred = model.predict(inn);
//		
//		for (int i = 0 ; i < pred.length; i++) {
//			System.out.println(pred[i]);
//		}
//		
		NN2layer model = new NN2layer(0.001, 0);
		model.test_gradiant(input[0], output[0], 0.00000001, false);
		model.test_gradiant(input[1], output[1], 0.00000001, false);

//		model.train_on_batch(input, outt2, 10000, false);
//		model.train_on_batch(in, out, 10000, false);
//		System.out.println(inputt[0][0] + " "+  inputt[0][1]);
//		double[] pred = model.predict(inputt[0]);
//		
//		System.out.println(pred[0]);
//		
		
		
		
		
		
		
		
		
	
		

	}

}
