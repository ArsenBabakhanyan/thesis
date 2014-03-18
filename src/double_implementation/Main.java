import java.util.Arrays;

import complex_numbers.ComplexNumber;

import matrix.Matrix;
import decomposition.OneQubit;
import decomposition.TwoQubit;


public class Main {

	public static void main(String[] args) {
		Matrix m = OneQubit.YZYMatrix(0.5, 0.2, 0.1);
		Matrix m0 = OneQubit.YZYMatrix(0.2, 0.3, 0.4);

		//test SU(2)
		System.out.println(Arrays.toString(OneQubit.KAKDec(m)));
		
		Matrix m1 = Matrix.tensor(m, m0);
		
		//test SU(2)SU(2)
		System.out.println(Arrays.toString(TwoQubit.SU2SU2(m1)));
		
		//test SO(4)
		
		Matrix z = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(0.8147,0), new ComplexNumber(0.9058,0), new ComplexNumber(0.1270, 0), new ComplexNumber(0.9134, 0)},
				{new ComplexNumber(0.9058,0), new ComplexNumber(0.0975,0), new ComplexNumber(0.2785, 0), new ComplexNumber(0.5469, 0)},
				{new ComplexNumber(0.1270,0), new ComplexNumber(0.2785,0), new ComplexNumber(0.1576, 0), new ComplexNumber(0.9706, 0)},
				{new ComplexNumber(0.9134,0), new ComplexNumber(0.5469,0), new ComplexNumber(0.9706, 0), new ComplexNumber(0.1419, 0)},
		});
		
		System.out.println(Arrays.toString(Matrix.eigen(z)));
	}

}
