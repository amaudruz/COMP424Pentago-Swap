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
	private double regularization;
	
	public ArrayList<Double> loss;

	private double[][] approx_dloss_dW1;

	private double[] approx_dloss_db2;

	private double[] approx_dloss_db1;

	private double[][] approx_dloss_dW2;
	
	
	public NN2layer(double lr, double regularization) {
		this.lr = lr;
		this.W1 = MatrixUtil.generate_matrice(layer1nodes, FEATURES_NBR);
		this.W2 = MatrixUtil.generate_matrice(OUTPUT_SIZE, layer1nodes);
		this.b1 = MatrixUtil.generate_vector(layer1nodes);
		this.b2 = MatrixUtil.generate_vector(OUTPUT_SIZE);
		this.loss = new ArrayList<Double>();
		this.regularization = regularization;

	}
	
	
	/**
	 * test the gradiant comparing approximate gradiant do gradient used in backpropagation
	 * @param input
	 * @param output
	 * @param epsilon
	 * @param comp
	 */
	public void test_gradiant(double[] input, double[] output, double epsilon) {
		//computes the gradiant using backward prop
		this.x = input;
		this.y = output;
		forward();
		backward(false);
		
		//computes the gradiant using approximation of partial derivative
		this.approx_dloss_dW1 = new double[this.dLoss_dW1.length][this.dLoss_dW1[0].length];
		this.approx_dloss_dW2 = new double[this.dLoss_dW2.length][this.dLoss_dW2[0].length];
		this.approx_dloss_db1 = new double[this.dLoss_db1.length];
		this.approx_dloss_db2 = new double[this.dLoss_db2.length];

		//computes each element for approx_dloss_dW1
		for (int i = 0; i < approx_dloss_dW1.length; i++) {
			for (int j = 0; j < approx_dloss_dW1[0].length; j++) {
				approx_dloss_dW1[i][j] = partial_der_lossW1(i,j, input, output, epsilon);
			}
		}
		
		
		
		//computes each element for approx_dloss_dW2
		for (int i = 0; i < approx_dloss_dW2.length; i++) {
			for (int j = 0; j < approx_dloss_dW2[0].length; j++) {
				approx_dloss_dW2[i][j] = partial_der_lossW2(i,j, input, output, epsilon);
			}
		}
		//computes each element for approx_dloss_db1
		for (int i = 0; i < this.dLoss_db1.length; i++) {
			approx_dloss_db1[i] = partial_der_b1(i, input, output, epsilon);
		}
		
		//computes each element for approx_dloss_db2
		for (int i = 0; i < this.dLoss_db2.length; i++) {
			approx_dloss_db2[i] = partial_der_b2(i, input, output, epsilon);
		}
		
		double[][] diff_W1 = MatrixUtil.ew_addititon_matr(this.dLoss_dW1, MatrixUtil.scalar_mul_matr(-1, approx_dloss_dW1));
		double[][] diff_W2 = MatrixUtil.ew_addititon_matr(this.dLoss_dW2, MatrixUtil.scalar_mul_matr(-1, approx_dloss_dW2));
		double[] diff_b1 = MatrixUtil.ew_addition(this.dLoss_db1, MatrixUtil.scalar_mul_vect(-1, approx_dloss_db1));
		double[] diff_b2 = MatrixUtil.ew_addition(this.dLoss_db2, MatrixUtil.scalar_mul_vect(-1, approx_dloss_db2));

	
		MatrixUtil.print_matr(diff_W1);
		MatrixUtil.print_matr(diff_W2);
		MatrixUtil.print_vect(diff_b1);
		MatrixUtil.print_vect(diff_b2);
		
		
		



	}
	
	/**
	 * computes the approx partial der of the less by b2i
	 * @param i
	 * @param input
	 * @param output
	 * @param epsilon
	 * @return
	 */
	private double partial_der_b2(int i, double[] input, double[] output, double epsilon) {
		double[] b2_p = new double[this.b2.length];
		double[] b2_m = new double[this.b2.length];

		for (int j = 0; j < this.b2.length; j++) {
			if (i == j) {
				b2_p[j] = this.b2[j] + epsilon;
				b2_m[j] = this.b2[j] - epsilon;

			}
			else {
				b2_p[j] = this.b2[j];
				b2_m[j] = this.b2[j];

			}
		}
		double l_p = this.comput_loss(input, output, this.W1, this.W2, b1, b2_p);
		double l_m = this.comput_loss(input, output, this.W1, this.W2, b1, b2_m);
		
		return (l_p - l_m) / (2*epsilon);
	}



	/**
	 * computes the approx partial der of the less by b1i
	 * @param i
	 * @param input
	 * @param output
	 * @param epsilon
	 * @return
	 */
	private double partial_der_b1(int i, double[] input, double[] output, double epsilon) {
		double[] b1_p = new double[this.b1.length];
		double[] b1_m = new double[this.b1.length];

		for (int j = 0; j < this.b1.length; j++) {
			if (i == j) {
				b1_p[j] = this.b1[j] + epsilon;
				b1_m[j] = this.b1[j] - epsilon;

			}
			else {
				b1_p[j] = this.b1[j];
				b1_m[j] = this.b1[j];

			}
		}
		double l_p = this.comput_loss(input, output, this.W1, this.W2, b1_p, b2);
		double l_m = this.comput_loss(input, output, this.W1, this.W2, b1_m, b2);
		
		return (l_p - l_m) / (2*epsilon);
		
	}



	/**
	 * Computes the approximated partial derivative of the loss by W2ij
	 * @param i
	 * @param j
	 * @param input
	 * @param output
	 * @param epsilon 
	 * @return
	 */
	private double partial_der_lossW2(int i, int j, double[] input, double[] output, double epsilon) {
		double[][] new_W2_plus = new double[this.W2.length][this.W2[0].length];
		double[][] new_W2_minus = new double[this.W2.length][this.W2[0].length];
		
		for (int m = 0; m < new_W2_minus.length; m++) {
			for(int n = 0; n < new_W2_minus[0].length; n++) {
				if (m == i && n == j) {
					new_W2_plus[m][n] = this.W2[m][n] + epsilon;
					new_W2_minus[m][n] = this.W2[m][n] - epsilon;

				}
				
				else {
					new_W2_plus[m][n] = this.W2[m][n];
					new_W2_minus[m][n] = this.W2[m][n];
				}
			}
		}
					
		double l_p = this.comput_loss(input, output, this.W1, new_W2_plus, this.b1, this.b2);
		double l_m = this.comput_loss(input, output, this.W1, new_W2_minus, this.b1, this.b2);
		
		return (l_p - l_m) / (2*epsilon);
	}



	/**
	 * Computes the approximated partial derivative of the loss by W1ij
	 * @param i
	 * @param j
	 * @param input
	 * @param output
	 * @param epsilon 
	 * @return
	 */
	private double partial_der_lossW1(int i, int j, double[] input, double[] output, double epsilon) {
		
		double[][] new_W1_plus = new double[this.W1.length][this.W1[0].length];
		double[][] new_W1_minus = new double[this.W1.length][this.W1[0].length];
		
		for (int m = 0; m < new_W1_minus.length; m++) {
			for(int n = 0; n < new_W1_minus[0].length; n++) {
				if (m == i && n == j) {
					new_W1_plus[m][n] = this.W1[m][n] + epsilon;
					new_W1_minus[m][n] = this.W1[m][n] - epsilon;

				}
				else {
					new_W1_plus[m][n] = this.W1[m][n];
					new_W1_minus[m][n] = this.W1[m][n];
				}
			}
		}
		
		double l_p = this.comput_loss(input, output, new_W1_plus, this.W2, this.b1, this.b2);
		double l_m = this.comput_loss(input, output, new_W1_minus, this.W2, this.b1, this.b2);
		
		return (l_p - l_m) / (2*epsilon);
		
	}



	/**
	 * predict output of xx
	 * @param xx
	 * @return
	 */
	public double[] predict(double[] xx) {
		double[] z1 = MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W1, xx), b1);

		double[] a1 = ReLU(z1);
		double[] yh =  MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W2, a1), b2);
		return yh;
	}
	
	
	/**
	 * predicts the output of input xx given all weights
	 * @param xx
	 * @param w1
	 * @param w2
	 * @param bb1
	 * @param bb2
	 * @return
	 */
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
		
		 
		forward();
		backward(true);
		
		
	}

	
	/**
	 * Computes the predicted value while storing intermediate results for backpropagation
	 */
	private void forward() {
		
		this.Z1 = MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W1, x), b1);

		this.A1 = ReLU(Z1);
		this.yh =  MatrixUtil.ew_addition(MatrixUtil.apply_matrice(W2, A1), b2);
		compute_loss();
		
	}
	


	/**
	 * Computes the partial derivative loss function with respect to W1, W2, b1, b2 and updates them using backpropagation
	 * to minimize loss function
	 */
	private void backward(boolean applychange) {
		double[] dloss_dyh = MatrixUtil.scalar_mul_vect(1, MatrixUtil.ew_addition(yh, MatrixUtil.scalar_mul_vect(-1, y)));
		double[] dloss_dA1 = MatrixUtil.apply_matrice(MatrixUtil.transpose(W2), dloss_dyh);
		double[] dloss_dZ1 = MatrixUtil.ew_multiplication(dloss_dA1, dReLU(Z1));
		
		

		this.dLoss_dW1= (MatrixUtil.outer_product(dloss_dZ1, x));
		this.dLoss_dW2 =  MatrixUtil.outer_product(dloss_dyh, A1);


		this.dLoss_db2 = dloss_dyh;
		this.dLoss_db1 = dloss_dZ1;
		
		if (applychange) {
			this.W1 = MatrixUtil.ew_addititon_matr(W1, MatrixUtil.scalar_mul_matr(-this.lr, this.dLoss_dW1));
			this.W2 = MatrixUtil.ew_addititon_matr(W2, MatrixUtil.scalar_mul_matr(-this.lr, this.dLoss_dW2));
			this.b1 = MatrixUtil.ew_addition(b1, MatrixUtil.scalar_mul_vect(-this.lr, this.dLoss_db1));
			this.b2 = MatrixUtil.ew_addition(b2, MatrixUtil.scalar_mul_vect(-this.lr, this.dLoss_db2));

		}
	
		
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
				result[i] =  0;
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
				result[i] = 0;
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
//		System.out.println(l);
		this.loss.add(l);
	}
	
	/**
	 * compute mse of inputs
	 * @param out
	 * @param pred
	 * @return
	 */
	public double compute_loss(double[] out , double[] pred) {
		double l = 0;
		for (int i = 0; i < out.length; i++) {
			l += (out[i] - pred[i])*(out[i] - pred[i])/2;
		}
		return l;
	}
	
	public double comput_loss(double[] input, double[] output, double[][] W1, double[][] W2, double[] b1, double[] b2) {
		double[] pred = this.predict(input, W1, W2, b1, b2);
		return this.compute_loss(output, pred);
	}
	

	
}
