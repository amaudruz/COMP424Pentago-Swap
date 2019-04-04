package student_player;

import java.util.ArrayList;

public class NNtest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> output = new ArrayList<Double>();
		for (double i = 0; i < 10 ; i++) {
			
			for (double j = 0; j < 10; j++) {
				ArrayList<Double> ij = new ArrayList<Double>();
				ij.add(i);
				ij.add(j);
				input.add(ij);
			}
			
		}
		
		double[][] in = new double[input.size()][2];
		double[][] out = new double[input.size()][1];

		
		for (int k = 0; k< input.size(); k++) {
			in[k][0] = input.get(k).get(0);
			in[k][1] = input.get(k).get(1);
			out[k][0] = in[k][0] + in[k][1];

			
		}
		
		
		
		NN2layer model= new NN2layer(0.0001);
		

		model.train_on_batch(in, out, 10000);
		double[] inn = {1, 1};
		double[] pred = model.predict(inn);
		
		for (int i = 0 ; i < pred.length; i++) {
			System.out.println(pred[i]);
		}
		
		
		
		
		
		
		
		
		
		
		//		double pred1[] = model.predict(input[1]);
//		double l = model.compute_loss(output[1], pred1);
//		System.out.println(l);
//		for (int i = 0; i < 59; i++) {
//			model.train(input[0], output[0]);
//			model.train(input[1], output[1]);
//
//		}
//		double pred2[] = model.predict(input[1]);
//		double l2 = model.compute_loss(output[1], pred2);
//		System.out.println(l2);
		

	}

}
