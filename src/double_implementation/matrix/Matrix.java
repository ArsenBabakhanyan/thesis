package matrix;

import opensourcephysics.Complex;
import opensourcephysics.ComplexEigenvalueDecomposition;
import complex_numbers.ComplexNumber;

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
	 * Tensor product of this with that
	 */
	public void tensor(Matrix that) {
		ComplexNumber[][] m = new ComplexNumber[len()*that.len()][len()*that.len()];
		for (int i = 0; i < len(); i++) {
			for (int j = 0; j < len(); j++) {
				Matrix tmp = mul(matrix[i][j], that);
				for (int k = 0; k < tmp.len(); k++) {
					for (int l = 0; l < tmp.len(); l++){
						m[i*tmp.len()+k][j*tmp.len()+l] = tmp.matrix[k][l];
					}
				}
			}
		}
		matrix = m;
	}
	
	/**
	 * Devides all emenents of this matrix by value
	 */
	public void div(ComplexNumber value) {
		for (int i = 0; i < len(); i++) {
			for (int j = 0; j < len(); j++) {
				matrix[i][j].div(value); 
			}
		}
	}
	
	/**
	 * Given Unitary Matrix makes it Special Unitary 
	 */
	public void makeSpecial() {
		ComplexNumber determ = Matrix.determinant(this);
		if (ComplexNumber.abs(determ) == 0)
			throw new RuntimeException("determinant is zero");
		int size = len();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j].div(determ);
			}
		}
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
	 * Returns a matrix that is tensor product of two matrixes
	 */
	public static Matrix tensor(Matrix first, Matrix second) {
		ComplexNumber[][] m = new ComplexNumber[first.len()*second.len()][first.len()*second.len()];
		for (int i = 0; i < first.len(); i++) {
			for (int j = 0; j < second.len(); j++) {
				Matrix tmp = mul(first.matrix[i][j], second);
				for (int k = 0; k < tmp.len(); k++) {
					for (int l = 0; l < tmp.len(); l++){
						m[i*tmp.len()+k][j*tmp.len()+l] = tmp.matrix[k][l];
					}
				}
			}
		}
		return new Matrix(m);
	}
	
	
	/**
	 * Returns diagonal matrix, diagonal elements are equal to that elements 
	 */
	public static Matrix diag(ComplexNumber[] that) {
		int len = that.length;
		ComplexNumber[][] comp = new ComplexNumber[len][len];
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (i == j) {
					comp[i][j] = new ComplexNumber(that[i]);
				}
				else {
					comp[i][j] = new ComplexNumber();
				}
			}
		}
		
		return new Matrix(comp);
	}

	/**
	 * Returns diagonal Matrix with values e^(i*tetta0), e^(i*tetta1) ....  
	 */
	public static Matrix matrixFromArgs(double... args) {
		int len = args.length;
		ComplexNumber[] diagonalElements = new ComplexNumber[len];
		for (int i = 0; i < len; i++) {
			diagonalElements[i] = ComplexNumber.argToNum(args[i]);
		}
		
		ComplexNumber[][] cn = new ComplexNumber[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (i == j)
					cn[i][j] = diagonalElements[i];
				else
					cn[i][j] = new ComplexNumber();
					
			}
		}
		
		return new Matrix(cn);
	}

	/**
	 * Compares two matrixes if difference between them is more then 0.000001 returns false 
	 */
	public static boolean equals(Matrix first, Matrix second) {
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

	/**
	 *	Returns Eigenvalues of the given array 
	 */
	public static ComplexNumber[] eigen (Matrix that) {
		ComplexNumber[][] m = that.getMatrix();
		int len = m.length;
		ComplexNumber[] eigens = new ComplexNumber[len];
	
		//wraps my objects to objects of opensourcephysics
		//and uses opensourcephysics functionality to get eigenvalues
		Complex[][] m_ = new Complex[len][len];
		Complex[] eigens_ = new Complex[len];
		Complex[][] vec = new Complex[len][len];
		boolean[] e = new boolean[len];
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				m_[i][j] = new Complex(m[i][j].getReal(), m[i][j].getImaginary());
			}
		}
		
		ComplexEigenvalueDecomposition.eigen(m_, eigens_, vec, e);
		
		for (int i = 0; i < len; i++) {
			eigens[i] = new ComplexNumber(eigens_[i].re(), eigens_[i].im());
		}
		
		return eigens;
	}

	/**
	 *	Returns EigenVectors of the given array 
	 */
	public static ComplexNumber[][] eigenVector (Matrix that) {
		ComplexNumber[][] m = that.getMatrix();
		int len = m.length;
		ComplexNumber[][] eigenVectors = new ComplexNumber[len][len];
	
		//wraps my objects to objects of opensourcephysics
		//and uses opensourcephysics functionality to get eigenvalues
		Complex[][] m_ = new Complex[len][len];
		Complex[] eigens_ = new Complex[len];
		Complex[][] vec = new Complex[len][len];
		boolean[] e = new boolean[len];
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				m_[i][j] = new Complex(m[i][j].getReal(), m[i][j].getImaginary());
			}
		}
		
		ComplexEigenvalueDecomposition.eigen(m_, eigens_, vec, e);
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++)
				eigenVectors[i][j] = new ComplexNumber(vec[i][j].re(), vec[i][j].im());
		}
		
		return eigenVectors;
	}

	/**
	 *	Returns Object array where first element is Eigenvalues, the second is Eigenvectors 
	 */
	public static Object[] eigenValuesAndVector (Matrix that) {
		ComplexNumber[][] m = that.getMatrix();
		int len = m.length;
		ComplexNumber[] eigens = new ComplexNumber[len];
		ComplexNumber[][] eigenVectors = new ComplexNumber[len][len];
	
		//wraps my objects to objects of opensourcephysics
		//and uses opensourcephysics functionality to get eigenvalues
		Complex[][] m_ = new Complex[len][len];
		Complex[] eigens_ = new Complex[len];
		Complex[][] vec = new Complex[len][len];
		boolean[] e = new boolean[len];
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				m_[i][j] = new Complex(m[i][j].getReal(), m[i][j].getImaginary());
			}
		}
		
		ComplexEigenvalueDecomposition.eigen(m_, eigens_, vec, e);
	
		for (int i = 0; i < len; i++) {
			eigens[i] = new ComplexNumber(eigens_[i].re(), eigens_[i].im());
		}
	
		
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++)
				eigenVectors[i][j] = new ComplexNumber(vec[i][j].re(), vec[i][j].im());
		}
		
		return new Object[]{eigens, eigenVectors};
	}
	
	/**
	 * Returns sum of the row elements in the given Matrix
	 */
	public static ComplexNumber rowSum(Matrix that, int row) {
		ComplexNumber sum = new ComplexNumber();
		if (row > that.len())
			return null;
		for (int i = 0; i < that.len(); i++)
			sum.add(that.getElement(row, i));
		
		return sum;
	}

	/**
	 * Returns sum of the column elements in the given Matrix
	 */
	public static ComplexNumber colSum(Matrix that, int col) {
		ComplexNumber sum = new ComplexNumber();
		if (col > that.len())
			return null;
		for (int i = 0; i < that.len(); i++)
			sum.add(that.getElement(i, col));
		
		return sum;
	}

	/**
	 * Returns Kronecker product of two given Matrixes
	 */
	public static Matrix kron(Matrix a, Matrix b) {
		int lenA = a.len();
		int lenB = b.len();
		
		Matrix c = new Matrix(lenA*lenB);
		
		for (int i = 0; i < lenA; i++) {
			int iOffset = i*lenB;
			for (int j = 0; j < lenA; j++) {
				int jOffset = j*lenB;
				ComplexNumber aij = a.getElement(i, j);
				
				for (int k = 0; k < lenB; k++) {
					for (int l = 0; l < lenB; l++) {
						c.setElement(iOffset+k, jOffset+l, ComplexNumber.mul(aij, b.getElement(k, l)));
					}
				}
			}
		}		
		
		return c;
	}
	
	/**
	 * Returns determinant of the Matrix
	 */
	public static ComplexNumber determinant(Matrix that) {
		int n = that.len();
		ComplexNumber determinant = new ComplexNumber(1., 0);
		ComplexNumber[][] B = new ComplexNumber[n][n];
		int row[] = new int[n];
		int hold, I_pivot;
		ComplexNumber pivot;
		double abs_pivot;
		
		for (int i = 0; i < n; i++) {
			row[i] = i;
			for (int j = 0; j < n; j++) {
				B[i][j] = that.getElement(i, j);
			}
		}
		
		for (int k = 0; k < n-1; k++) {
			pivot = B[row[k]][k];
			abs_pivot = ComplexNumber.abs(pivot);
			I_pivot = k;
			for(int i = k; i<n; i++) {
				if(ComplexNumber.abs(B[row[i]][k])>abs_pivot) {
					I_pivot = i;
					pivot = B[row[i]][k];
					abs_pivot = ComplexNumber.abs(pivot);
				}
			}
		
			if(I_pivot != k) {
				hold = row[k];
				row[k] = row[I_pivot];
				row[I_pivot] = hold;
				determinant.neg();
			}

			if(abs_pivot<1.0E-10) {
				return new ComplexNumber(0.0, 0.0);
			}
			determinant.mul(pivot);

			for(int j = k+1; j<n; j++) {
				B[row[k]][j].div(B[row[k]][k]);
			}

			for(int i = 0; i<n; i++) {
				if(i!=k) {
					for(int j = k+1; j<n; j++) {
						B[row[i]][j].sub(ComplexNumber.mul(B[row[i]][k], B[row[k]][j]));
					}
				}
			}

		}
		
		return ComplexNumber.mul(determinant, B[row[n-1]][n-1]);
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
	 * Returns Identity Matrix <br />
	 * | 1 0 |<br />
	 * | 0 1 |
	 */
	public static final Matrix Identity2x2() {
		Matrix id = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(1., 0), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(1., 0)}
				});
		return id;
	}
	
	/**
	 * Returns CNOT <br />
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
	 * Returns Bell state matrix (magic basis)<br />
	 * |1.4 0     0    1.4i| <br />
	 * |0   1.4i  1.4  0   | <br />
	 * |0   1.4i -1.4  0   | <br />
	 * |1.4 0     0   -1.4 |
	 */
	public static final Matrix B() {
		return new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(1, 0), new ComplexNumber(), new ComplexNumber(), new ComplexNumber(0, 1)},
				{new ComplexNumber(), new ComplexNumber(0, 1), new ComplexNumber(1, 0), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(0, 1), new ComplexNumber(-1, 0), new ComplexNumber()},
				{new ComplexNumber(1, 0), new ComplexNumber(), new ComplexNumber(), new ComplexNumber(0, -1)}
		});
	}
}
