package decomposition;

import complex_numbers.ComplexNumber;
import matrix.Matrix;

/**
 * This class provides all functionality concerning decomposition of two qubit systems
 * @author Arsen Babakhanyan
 */
public class TwoQubit {
	
	/**
	 * Enum containing two predefined values <br />
	 * you can use either First or Second qubit 
	 */
	public enum Qubit2{
		First, Second
	};
	
	/**
	 * Returns partial trace of the given 4x4 matrix
	 */
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
	
	/**
	 * returns sqrt(|first|^2 + |second|^2) 
	 */
	public static double SAP2 (ComplexNumber first, ComplexNumber second) {
		return Math.sqrt(
				ComplexNumber.abs(ComplexNumber.mul(first, first)) + 
				ComplexNumber.abs(ComplexNumber.mul(second, second))
			);
	}
	
	/**
	 * Decomposition of Matrix that according SU2xSU2 procedure  (multi-thraded) 
	 */
	public static double[] SU2SU2(final Matrix that) {
		
		final double[] val0 = new double[3];
		final double[] val1 = new double[3];
		
		Thread t1 = new Thread(new Runnable() {			
			public void run() {
				Matrix S1 = partialTrace(that, Qubit2.Second);
				
				double c1 = SAP2(S1.getElement(0, 0), S1.getElement(1, 0));
				S1.div(new ComplexNumber(c1, 0));  /// Q
				
				double[] val = OneQubit.KAKDec(S1);
				val1[0] = val[0];
				val1[1] = val[1];
				val1[2] = val[2];
			}
		});

		Thread t2 = new Thread(new Runnable() {			
			public void run() {
				Matrix S2 = partialTrace(that, Qubit2.First);

				double c2 = SAP2(S2.getElement(0, 0), S2.getElement(1, 0));
				S2.div(new ComplexNumber(c2, 0));  /// P
				
				double[] val = OneQubit.KAKDec(S2);
				val0[0] = val[0];
				val0[1] = val[1];
				val0[2] = val[2];

			}
		});
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		return new double[]{val0[0], val0[1], val0[2], val1[0], val1[1], val1[2]};
	}
}
