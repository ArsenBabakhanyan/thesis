package decomposition;

import complex_number.ComplexNumber;
import matrix.Matrix;

public class TwoQubit {
	
	public enum Qubit2{
		First, Second
	};
	
	public static Matrix partialTrace(Matrix that, Qubit2 num) {
		if (that.len() != 4) {
			throw new IllegalArgumentException("Matrix must be of size 4x4");
		}
		Matrix m = new Matrix();
		switch (num) {
			case First:
				m.setElement(0, 0, ComplexNumber.add(that.getElement(0, 0), that.getElement(1, 1)));
				m.setElement(0, 1, ComplexNumber.add(that.getElement(0, 2), that.getElement(1, 3)));
				m.setElement(1, 0, ComplexNumber.add(that.getElement(2, 0), that.getElement(3, 1)));
				m.setElement(1, 1, ComplexNumber.add(that.getElement(2, 2), that.getElement(3, 3)));
				break;
			case Second:
				m.setElement(0, 0, ComplexNumber.add(that.getElement(0, 0), that.getElement(2, 2)));
				m.setElement(0, 1, ComplexNumber.add(that.getElement(0, 1), that.getElement(2, 3)));
				m.setElement(1, 0, ComplexNumber.add(that.getElement(1, 0), that.getElement(3, 2)));
				m.setElement(1, 1, ComplexNumber.add(that.getElement(1, 1), that.getElement(3, 3)));
		}
		return m;
	}
	
	public static double SAP2 (ComplexNumber first, ComplexNumber second) {
		return Math.sqrt(
				ComplexNumber.abs(ComplexNumber.mul(first, first)) + 
				ComplexNumber.abs(ComplexNumber.mul(second, second))
			);
	}
	
	public static double[] SU2SU2(Matrix that) {
		
		Matrix S1 = partialTrace(that, Qubit2.Second);
		Matrix S2 = partialTrace(that, Qubit2.First);
				
		double c1 = SAP2(S1.getElement(0, 0), S1.getElement(1, 0));
		double c2 = SAP2(S2.getElement(0, 0), S2.getElement(1, 0));
		
		S2.div(new ComplexNumber(c2, 0));  /// P
		S1.div(new ComplexNumber(c1, 0));  /// Q

		double[] val0 = OneQubit.KAKDec(S2);
		
		double[] val1 = OneQubit.KAKDec(S1);
		
		return new double[]{val0[0], val0[1], val0[2], val1[0], val1[1], val1[2]};
	}
}
