package student_player;

public class MatrixUtil {
	
	/**
	 * Generates random vector of size d
	 * @param d
	 * @return
	 */
	public static double[] generate_vector(int d) {
		double[] vect = new double[d];
		for (int i = 0; i < vect.length ; i++) {
			vect[i] = Math.random() * 20 -10;
		}
		return vect;
	}

	
	/**
	 * Generates random matrix of dimension nxm 
	 * @param n
	 * @param m
	 * @return
	 */
	public static double[][] generate_matrice(int n, int m) {
		double[][] matrice = new double[n][m];
		for (int i = 0; i< matrice.length; i++) {
			for (int j = 0; j< matrice[0].length ; j++) {
				matrice[i][j] = Math.random() * 20 -10;

			}
		}
		return matrice;
	}
	
	/**
	 * Matrix multiplication of a and b
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] matrice_mul(double[][] a, double[][] b) {
		double[][] result = new double[a.length][b[0].length];
		
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				result[i][j] = 0;
				for (int k = 0; k < a[0].length; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return result;
	}
	
	
	/**
	 * Application of matrix A to vector x
	 * @param A
	 * @param x
	 * @return
	 */
	public static double[] apply_matrice(double[][] A, double[] x) {
		double result[] = new double[A.length];
		for (int i = 0; i < A.length; i++) {
			result[i] = 0;
			for (int k = 0; k < x.length; k++) {
				result[i] += A[i][k] * x[k];
			}
		}
		
		return result;
	}
	
	/**
	 * applies the addition of vectors x and y element wise
	 * @param x
	 * @param y
	 * @return
	 */
	public static double[] ew_addition(double[] x, double[] y) {
		double[] result = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] + y[i];
		}
		return result;
	}
	
	/**
	 * Returns the multiplication of vectors x and y element wise
	 * @param x
	 * @param y
	 * @return
	 */
	public static double[] ew_multiplication(double[] x, double[] y) {
		double[] result = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] * y[i];
		}
		return result;
	}

	/**
	 * Returns the multiplication of a vector by a scalar
	 * @param a
	 * @param v
	 * @return
	 */
	public static double[] scalar_mul_vect(double a, double[] v) {
		double[] result = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			result[i] = a * v[i];
		}
		return  result;
	}
	
	/**
	 * Computes the transpose of matrice M
	 * @param M
	 * @return
	 */
	public static double[][] transpose(double[][] M) {
		double[][] result = new double[M[0].length][M.length];
		for (int i = 0; i < M.length ; i++) {
			for (int j = 0; j < M[0].length; j++) {
				result[j][i] = M[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Computes the outer product that is needed for backward ...
	 * @param dl
	 * @param x
	 * @return
	 */
	public static double[][] outer_product(double[] dl,double[] x ) {
		double[][] result = new double[dl.length][x.length];
		for (int i = 0 ; i < dl.length; i++) {
			for (int j = 0; j < x.length; j++) {
				result[i][j] = dl[i] * x[j];
			}
		}
		return result;
	}
	
	/**
	 * Computes the Matrix scaled element wise by a
	 * @param a
	 * @param M
	 * @return
	 */
	public static double[][] scalar_mul_matr(double a, double[][] M) {
		double[][] result = new double[M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j< M[0].length; j++) {
				result[i][j] = a * M[i][j];
			}
		}
		return result;
	}
	
	public static double[][] ew_addititon_matr(double[][] A, double[][] B) {
		double[][] result = new double[A.length][A[0].length];
		for (int i = 0; i < A.length ; i++) {
			for (int j = 0; j< A[0].length; j++) {
				result[i][j] = A[i][j] + B[i][j];
			}
		}
		return result;
	}
	
	public static void print_matr(double[][] matr) {
		System.out.println();

		for (int i = 0; i < matr.length; i++) {
			System.out.println();
			for (int j = 0; j< matr[0].length ; j++) {
				System.out.print(matr[i][j] + ", ");
			}
		}
		System.out.println();
		System.out.println();

	}
	public static void print_vect(double[] matr) {
		System.out.println();

		for (int i = 0; i < matr.length; i++) {
			
				System.out.print(matr[i] + ", ");
			
		}
		System.out.println();
	}
}
