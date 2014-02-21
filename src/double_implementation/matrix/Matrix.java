package matrix;
import complex_number.ComplexNumber;

/**
 * Class to work with NxN matrixes of ComplexNumbers
 * @author Arsen Babakhanyan
 *
 */
public class Matrix {
	
	private ComplexNumber[][] matrix;
	
	/**
	 * Creates Matrix of size 2x2
	 */
	public Matrix() {
		this(2);
	}
	
	/**
	 * Creates Matrix of size nxn
	 */
	public Matrix(int n) {
		matrix = new ComplexNumber[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = new ComplexNumber();
			}
		}
	}
	
	/**
	 * Creates Matrix based on ComplexNumber
	 */
	public Matrix(ComplexNumber[][] that) {
		matrix = new ComplexNumber[that.length][that.length];
		
		for (int i = 0; i < that.length; i++) {
			for (int j = 0; j < that.length; j++) {
				matrix[i][j] = new ComplexNumber(that[i][j]);
			}
		}
	}
	
	/**
	 * Creates copy Matrix of that
	 */
	public Matrix(Matrix that) {
		matrix = new ComplexNumber[that.matrix.length][that.matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = new ComplexNumber(that.matrix[i][j]);
			}
		}
	}
	
	/**
	 * Returns the two dimensional array of ComplexNumbers (the matrix)
	 */
	public ComplexNumber[][] getMatrix() {
		return matrix;
	}
	
	/**
	 * Returns particular element of the Matrix
	 */
	public ComplexNumber getElement(int col, int row) {
		
		if (col > matrix.length || row > matrix.length)
			throw new IndexOutOfBoundsException();
		
		return matrix[col][row];
	}
	
	/**
	 * Sets to element matrix[col][row] = val
	 */
	public void setElement(int col, int row, ComplexNumber val) {
		
		if (col > matrix.length || row > matrix.length)
			throw new IndexOutOfBoundsException();
		
		matrix[col][row] = new ComplexNumber(val);
	}
	
	/**
	 * Computes transpose of the matrix
	 */
	public void transpose() {
		ComplexNumber temp;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i+1; j < matrix.length; j++) {
				temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
	}
	
	/**
	 * Computes conjugate of the matrix
	 */
	public void conjugate() {
		
		for (ComplexNumber[] arr : matrix) {
			for (ComplexNumber el : arr)
				el.conjugate();
		}
	}
	
	/**
	 * Computes adjoint or conjugate transpose of the matrix
	 */
	public void adjoint() {
		
		this.conjugate();
		this.transpose();
		
	}
	
	/**
	 * Computes this +=that
	 */
	public void add(Matrix that) {
		if (matrix.length != that.len())
			throw new RuntimeException("Illegal matrix dimensions.");

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].add(that.matrix[i][j]);
			}
		}
	}
	
	/**
	 * Computes this -= that
	 */
	public void sub(Matrix that) {
		if (matrix.length != that.len())
			throw new RuntimeException("Illegal matrix dimensions.");
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].sub(that.matrix[i][j]);
			}
		}
	}
	
	/**
	 * Computes this = value * this
	 */
	public void mul(ComplexNumber value) {
		if (value == null)
			throw new NullPointerException();
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].mul(value);
			}
		}
	}
	
	/**
	 * Computes this *= that
	 */
	public void mul(Matrix that) {
		if (matrix.length != that.matrix.length)
			throw new RuntimeException("Illegal matrix dimensions.");
		
		Matrix mTmp = new Matrix(matrix.length);
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				for (int k = 0; k < matrix.length; k++) {
					mTmp.matrix[i][j].add(ComplexNumber.mul(matrix[i][k], that.matrix[k][j]));
				}
			}
		}
		
		matrix = mTmp.matrix;
	}
	
	/**
	 * Returnes dimension of the matrix
	 */
	public int len() {
		return matrix.length;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < matrix.length; i++) {
			str += "[";
			for (int j = 0; j < matrix.length; j++) {
				str += matrix[i][j];
				if (j != matrix.length-1)
					str +=", ";
			}
			str += "]";
			if (i != matrix.length-1)
				str += "\n";
		}
		return str;
	}

	/**
	 * Returns transpose of the given matrix without changing that
	 */
	public static Matrix transpose(Matrix that) {
		Matrix tmp = new Matrix(that);
		tmp.transpose();
		return tmp;
	}
	
	/**
	 * Returns conjugate of the given matrix without changing that
	 */
	public static Matrix conjugate(Matrix that) {
		Matrix tmp = new Matrix(that);
		tmp.conjugate();
		return tmp;
	}
	
	/**
	 * Returns adjoint of given matrix without changing it 
	 */
	public static Matrix adjoint(Matrix that) {
		Matrix tmp = new Matrix(that);
		tmp.adjoint();
		return tmp;
	}
	
	/**
	 * Returns first + second 
	 */
	public static Matrix add(Matrix first, Matrix second) {
		Matrix tmp = new Matrix(first);
		tmp.add(second);
		return tmp;
	}
	
	/**
	 * Returns first - second
	 */
	public static Matrix sub(Matrix first, Matrix second) {
		Matrix tmp = new Matrix(first);
		tmp.sub(second);
		return tmp;
	}
	
	/**
	 * Returns value * first 
	 */
	public static Matrix mul(ComplexNumber value, Matrix first) {
		Matrix tmp = new Matrix(first);
		tmp.mul(value);
		return tmp;
	}
	
	/**
	 * Returns a matrix that is computed as tmp = a * b * c ... 
	 */
	public static Matrix mul(Matrix... matrices) {
		Matrix tmp = new Matrix(matrices[0]);
		for (int i = 1; i < matrices.length; i++) {
			tmp.mul(matrices[i]);
		}
		return tmp;
	}
	
	/**
	 * Returns Pauli X matrix<br />
	 * | 0  1 | <br />
	 * | 1  0 | 
	 */
	public static final Matrix PAULIX() {
		Matrix x = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(), new ComplexNumber(1., 0)},
				{new ComplexNumber(1., 0), new ComplexNumber()}
				});
		return x;
	}

	/**
	 * Returns Pauli Y matrix <br />
	 * | 0 -i |<br />
	 * | i &nbsp;  0 |
	 */
	public static final Matrix PAULIY() {
		Matrix y = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(), new ComplexNumber(0, -1.)},
				{new ComplexNumber(0, 1.), new ComplexNumber()}
				});
		return y;
	}

	/**
	 * Returns Pauli Z matrix <br />
	 * | 1 &nbsp; 0 | <br />
	 * | 0  -1 |
	 */
	public static final Matrix PAULIZ() {
		Matrix z = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(1., 0), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(-1., 0)}
				});
		return z;
	}
	
	/**
	 * Returnes CNOT <br />
	 * | 1 0 0 0 |<br />
	 * | 0 1 0 0 |<br />
	 * | 0 0 0 1 |<br />
	 * | 0 0 1 0 |
	 */
	public static final Matrix CNOT() {
		Matrix cnot = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(1., 0), new ComplexNumber(), new ComplexNumber(), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(1., 0), new ComplexNumber(), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(), new ComplexNumber(), new ComplexNumber(1., 0)},
				{new ComplexNumber(), new ComplexNumber(), new ComplexNumber(1., 0), new ComplexNumber()},
		});
		return cnot;
	}
	
	/**
	 * Compares two matrixes if difference between them is more then 0.000001 returns false 
	 */
	public static final boolean equals(Matrix first, Matrix second) {
		if (first.len() != second.len())
			throw new RuntimeException("Illegal matrix dimensions.");
		
		double a, b;
		ComplexNumber tmp;
		for (int i = 0; i < first.len(); i++) {
			for (int j = 0; j < first.len(); j++) {
				tmp = ComplexNumber.sub(first.getElement(i, j), second.getElement(i, j));  
				a = Math.abs(tmp.getReal());
				b = Math.abs(tmp.getImaginary());
				if (a > 0.000001 || b > 0.000001)
					return false;
			}
		}
		return true;
	}

}
