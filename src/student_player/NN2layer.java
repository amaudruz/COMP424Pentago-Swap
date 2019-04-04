package student_player;

import java.util.ArrayList;

public class NN2layer {
	//learning rate
	private double lr;
	
	//weights
	private double[][] W1;
	private double[][] W2;
	private double[] b1;
	private double[] b2;
	
	private int layer1nodes = 10;
	private final int FEATURES_NBR = 2;
	private final int OUTPUT_SIZE = 1;
	
	
	
	//input
	private double[] x;
	//output
	private double[] y;
	//predicted output
	private double[] yh;
	//Loss = (Y -Yh)**2
	//Yh = Relu(Z2)
	//Z2 = W2 * A1 + b2
	//A1 = Relu(Z1)
	//Z1 = W1 * X + b1
	private double[] Z1;
	private double[] A1;
	
	private double [][] dLoss_dW1;
	private double [][] dLoss_dW2;
	private double [] dLoss_db1;
	private double [] dLoss_db2;
	
	public ArrayList<Double> loss;
	
	public NN2layer(double lr) {
		this.lr = lr;
		this.W1 = MatrixUtil.generate_matrice(layer1nodes, FEATURES_NBR);
		this.W2 = MatrixUtil.generate_matrice(OUTPUT_SIZE, layer1nodes);
		this.b1 = MatrixUtil.generate_vector(layer1nodes);
		this.b2 = MatrixUtil.generate_vector(OUTPUT_SIZE);
		this.loss = new ArrayList<Double>();

	}
	public void test_gradiant(double[] input, double[] output) {
		double [][] tw1_pl = this.W1;
		double[][] tw1_mo = this.W1;
		tw1_pl[0][0] +=  0.000001;
		tw1_pl[0][0] -=  0.000001;

		
		double[] yh_p = predict(input, tw1_pl, this.W2, this.b1, this.b2);
		double[] yh_m = predict(input, tw1_mo, this.W2, this.b1, this.b2);

		
		double loss_p= this.compute_loss(output, yh_p);
		double loss_m= this.compute_loss(output, yh_m);
		
		double der = (loss_p - loss_m)/0.000001;
		System.out.println(der);
		
		
	}
	
	
	public double[] predict(double[] xx) {
		double[] z1 = MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W1, xx), b1);

		double[] a1 = ReLU(z1);
		double[] yh =  MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W2, a1), b2);
		return yh;
	}
	
	public double[] predict(double[] xx, double[][] w1, double[][] w2, double[] bb1, double[] bb2) {
		double[] z1 = MatrixUtil.ew_addition(MatrixUtil.apply_matrice(w1, xx), bb1);

		double[] a1 = ReLU(z1);
		double[] yh =  MatrixUtil.ew_addition(MatrixUtil.apply_matrice(w2, a1), bb2);
		
		return yh;
	}
	
	
	
	/**
	 * Trains over the batch
	 * @param input
	 * @param output
	 */
	public void train_on_batch(double[][] input, double[][] output, int epochs) {
		this.loss = new ArrayList<Double>();
		for (int l = 0; l< epochs; l++) {
			for (int i = 0; i < input.length; i++) {
				train(input[i], output[i]);
			}
				
		}
	}

	/**
	 * Sgd with one input
	 * @param x
	 * @param y
	 */
	public void train(double[] x, double[] y) {
		this.x = x;
		this.y = y;
//		MatrixUtil.print_matr(this.W1);

		forward();
		backward();
	}

	/**
	 * Computes the predicted value while storing intermediate results for backward part
	 */
	private void forward() {
		
		
		





		this.Z1 = MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W1, x), b1);

		this.A1 = ReLU(Z1);
		this.yh =  MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W2, A1), b2);
//		System.out.println("Start :");
//		MatrixUtil.print_matr(W1);
//		MatrixUtil.print_matr(W2);
//		MatrixUtil.print_vect(b1);
//		MatrixUtil.print_vect(b2);
//		MatrixUtil.print_vect(x);
//		MatrixUtil.print_vect(this.Z1);
//		MatrixUtil.print_vect(this.A1);
//		MatrixUtil.print_vect(this.Z2);
		compute_loss();
		
	}
	


	/**
	 * Computes the partial derivative of loss function with respect to W1, W2, b1, b2
	 */
	private void backward() {
		double[] dloss_dyh = MatrixUtil.scalar_mul_vect(1, MatrixUtil.ew_addition(yh, MatrixUtil.scalar_mul_vect(-1, y)));
		double[] dloss_dA1 = MatrixUtil.apply_matrice(MatrixUtil.transpose(W2), dloss_dyh);
		double[] dloss_dZ1 = MatrixUtil.ew_multiplication(dloss_dA1, dReLU(Z1));
		
		
		double[][] dloss_dW1 = MatrixUtil.outer_product(dloss_dZ1, x);
		double[][] dloss_dW2 = MatrixUtil.outer_product(dloss_dyh, A1);
		double[] dloss_db2 = dloss_dyh;
		double[] dloss_db1 = dloss_dZ1;
		

		this.W1 = MatrixUtil.ew_addititon_matr(W1, MatrixUtil.scalar_mul_matr(-this.lr, dloss_dW1));
		this.W2 = MatrixUtil.ew_addititon_matr(W2, MatrixUtil.scalar_mul_matr(-this.lr, dloss_dW2));
		this.b1 = MatrixUtil.ew_addition(b1, MatrixUtil.scalar_mul_vect(-this.lr, dloss_db1));
		this.b2 = MatrixUtil.ew_addition(b2, MatrixUtil.scalar_mul_vect(-this.lr, dloss_db2));

		
	}
	
	/**
	 * Computes the ReLU (rectified linear unit) element wise of a
	 * @param a
	 * @return
	 */
	private double[] ReLU(double[] a) {
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			if (a[i] < 0) {
				result[i] =  a[i] * 0.01;
			}
			else {
				result[i] = a[i];
			}
		}
		return result;
	}
	
	private double[] dReLU(double[] a) {
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			if (a[i] > 0) {
				result[i] = 1;
			}
			else {
				result[i] = 0.01;
			}
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	/**
	 * computes the loss of the current predicted output
	 */
	private void compute_loss() {
		double l = 0;
		for (int i = 0; i < this.y.length; i++) {
			l += (y[i] - yh[i])*(y[i] - yh[i])/2;
		}
		this.loss.add(l);
	}
	
	public double compute_loss(double[] out , double[] pred) {
		double l = 0;
		for (int i = 0; i < out.length; i++) {
			l += (out[i] - pred[i])*(out[i] - pred[i])/2;
		}
		return l;
	}
	
	

	
}
