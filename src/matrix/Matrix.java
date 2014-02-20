package matrix;
import complex_number.ComplexNumber;

public class Matrix {
	
	private ComplexNumber[][] matrix;
	
	public Matrix() {
		this(2);
	}
	
	public Matrix(int n) {
		matrix = new ComplexNumber[n][n];
	}
	
	public Matrix(ComplexNumber[][] that) {
		matrix = new ComplexNumber[that.length][that.length];
		
		for (int i = 0; i < that.length; i++) {
			for (int j = 0; j < that.length; j++) {
				matrix[i][j] = new ComplexNumber(that[i][j]);
			}
		}
		
	}
	
	public ComplexNumber[][] getMatrix() {
		return matrix;
	}
	
	public ComplexNumber getElement(int col, int row) {
		
		if (col > matrix.length || row > matrix.length)
			throw new IndexOutOfBoundsException();
		
		return matrix[col][row];
	}
	
	public void setElement(int col, int row, ComplexNumber val) {
		
		if (col > matrix.length || row > matrix.length)
			throw new IndexOutOfBoundsException();
		
		matrix[col][row] = new ComplexNumber(val);
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

	// constant matrices
	
	public static final Matrix PAULIX() {
		Matrix x = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(), new ComplexNumber("1", "0")},
				{new ComplexNumber("1", "0"), new ComplexNumber()}
				});
		return x;
	}

	public static final Matrix PAULIY() {
		Matrix y = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber(), new ComplexNumber("0", "-1")},
				{new ComplexNumber("0", "1"), new ComplexNumber()}
				});
		return y;
	}

	public static final Matrix PAULIZ() {
		Matrix z = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber("1", "0"), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber("-1", "0")}
				});
		return z;
	}
	
	public static final Matrix CNOT() {
		Matrix cnot = new Matrix(new ComplexNumber[][]{
				{new ComplexNumber("1", "0"), new ComplexNumber(), new ComplexNumber(), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber("1", "0"), new ComplexNumber(), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(), new ComplexNumber(), new ComplexNumber("1", "0")},
				{new ComplexNumber(), new ComplexNumber(), new ComplexNumber("1", "0"), new ComplexNumber()},
		});
		return cnot;
	}

}

