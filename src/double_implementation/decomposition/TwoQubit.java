package decomposition;

import complex_numbers.ComplexNumber;
import matrix.Matrix;

/**
 * This class provides all functionality concerning decomposition of two qubit
 * systems
 * 
 * @author Arsen Babakhanyan
 */
public class TwoQubit {

	/**
	 * Enum containing two predefined values <br />
	 * you can use either First or Second qubit
	 */
	public enum Qubit2 {
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
			m.setElement(
					0,
					0,
					ComplexNumber.add(that.getElement(0, 0),
							that.getElement(1, 1)));
			m.setElement(
					0,
					1,
					ComplexNumber.add(that.getElement(0, 2),
							that.getElement(1, 3)));
			m.setElement(
					1,
					0,
					ComplexNumber.add(that.getElement(2, 0),
							that.getElement(3, 1)));
			m.setElement(
					1,
					1,
					ComplexNumber.add(that.getElement(2, 2),
							that.getElement(3, 3)));
			break;
		case Second:
			m.setElement(
					0,
					0,
					ComplexNumber.add(that.getElement(0, 0),
							that.getElement(2, 2)));
			m.setElement(
					0,
					1,
					ComplexNumber.add(that.getElement(0, 1),
							that.getElement(2, 3)));
			m.setElement(
					1,
					0,
					ComplexNumber.add(that.getElement(1, 0),
							that.getElement(3, 2)));
			m.setElement(
					1,
					1,
					ComplexNumber.add(that.getElement(1, 1),
							that.getElement(3, 3)));
		}
		return m;
	}

	/**
	 * returns sqrt(|first|^2 + |second|^2)
	 */
	public static double SAP2(ComplexNumber first, ComplexNumber second) {
		return Math.sqrt(ComplexNumber.abs(ComplexNumber.mul(first, first))
				+ ComplexNumber.abs(ComplexNumber.mul(second, second)));
	}

	/**
	 * Decomposition of Matrix that according SU2xSU2 procedure (multi-thraded)
	 */
	public static double[] SU2SU2(final Matrix that) {

		final double[] val0 = new double[3];
		final double[] val1 = new double[3];

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				Matrix S1 = partialTrace(that, Qubit2.Second);

				double c1 = SAP2(S1.getElement(0, 0), S1.getElement(1, 0));
				S1.div(new ComplexNumber(c1, 0)); // / Q

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
				S2.div(new ComplexNumber(c2, 0)); // / P

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

		return new double[] { val0[0], val0[1], val0[2], val1[0], val1[1],
				val1[2] };
	}

	/**
	 * Isoclinic decomposition of SO4
	 */
	public static double[] SO4(Matrix that) {
		Matrix A = associateSO4(that);
		
		double[] abcd = abcd(A);
		
		final Matrix leftFactor = leftFactor(abcd);
		final Matrix rightFactor = rightFactor(pqrs(A, abcd[0]));
		
		final double[] a = new double[3];
		final double[] b = new double[3];

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				Matrix left = Matrix.mul(Matrix.B(), leftFactor, Matrix.adjoint(Matrix.B()));
				double[] m = SU2SU2(left);
				a[0] = m[3];
				a[1] = m[4];
				a[2] = m[5];
			}
		});

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				Matrix right = Matrix.mul(Matrix.B(), rightFactor, Matrix.adjoint(Matrix.B()));
				double[] m = SU2SU2(right);
				b[0] = m[0];
				b[1] = m[1];
				b[2] = m[2];
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
		
		return new double[] { a[0], a[1], a[2], b[0], b[1], b[2]};
	}
	
	/**
	 * Returns left isoclinic factor based on a, b, c, d 
	 */
	public static Matrix leftFactor(double[] abcd) {
		return new Matrix (new ComplexNumber[][] {
				{new ComplexNumber(abcd[0], 0), new ComplexNumber(-abcd[1], 0), new ComplexNumber(-abcd[2], 0), new ComplexNumber(-abcd[3], 0)},
				{new ComplexNumber(abcd[1], 0), new ComplexNumber( abcd[0], 0), new ComplexNumber(-abcd[3], 0), new ComplexNumber( abcd[2], 0)},
				{new ComplexNumber(abcd[2], 0), new ComplexNumber( abcd[3], 0), new ComplexNumber( abcd[0], 0), new ComplexNumber(-abcd[1], 0)},
				{new ComplexNumber(abcd[3], 0), new ComplexNumber(-abcd[2], 0), new ComplexNumber( abcd[1], 0), new ComplexNumber( abcd[0], 0)}
		});
	}

	/**
	 * Returns right isoclinic factor based on p, q, r, s 
	 */
	public static Matrix rightFactor(double[] pqrs) {
		return new Matrix (new ComplexNumber[][] {
				{new ComplexNumber(pqrs[0], 0), new ComplexNumber(-pqrs[1], 0), new ComplexNumber(-pqrs[2], 0), new ComplexNumber(-pqrs[3], 0)},
				{new ComplexNumber(pqrs[1], 0), new ComplexNumber( pqrs[0], 0), new ComplexNumber( pqrs[3], 0), new ComplexNumber(-pqrs[2], 0)},
				{new ComplexNumber(pqrs[2], 0), new ComplexNumber(-pqrs[3], 0), new ComplexNumber( pqrs[0], 0), new ComplexNumber( pqrs[1], 0)},
				{new ComplexNumber(pqrs[3], 0), new ComplexNumber( pqrs[2], 0), new ComplexNumber(-pqrs[1], 0), new ComplexNumber( pqrs[0], 0)}
		});
	}

	/**
	 * Returns associate matrix of SO4
	 */
	public static Matrix associateSO4(Matrix that) {
		Matrix associate = new Matrix(4);
		double tmp;
		
		tmp =     that.getElement(0, 0).getReal()
				+ that.getElement(1, 1).getReal()
				+ that.getElement(2, 2).getReal()
				+ that.getElement(3, 3).getReal();
		associate.setElement(0, 0, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 1).getReal()
				+ that.getElement(1, 0).getReal()
				+ that.getElement(2, 3).getReal()
				- that.getElement(3, 2).getReal();
		associate.setElement(0, 1, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 2).getReal()
				- that.getElement(1, 3).getReal()
				+ that.getElement(2, 0).getReal()
				+ that.getElement(3, 1).getReal();
		associate.setElement(0, 2, new ComplexNumber(tmp/4., 0));
		
		tmp =   - that.getElement(0, 3).getReal()
				+ that.getElement(1, 2).getReal()
				- that.getElement(2, 1).getReal()
				+ that.getElement(3, 0).getReal();
		associate.setElement(0, 3, new ComplexNumber(tmp/4., 0));

		
		tmp =   - that.getElement(0, 1).getReal()
				+ that.getElement(1, 0).getReal()
				- that.getElement(2, 3).getReal()
				+ that.getElement(3, 2).getReal();
		associate.setElement(1, 0, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 0).getReal()
				- that.getElement(1, 1).getReal()
				+ that.getElement(2, 2).getReal()
				+ that.getElement(3, 3).getReal();
		associate.setElement(1, 1, new ComplexNumber(tmp/4., 0));

		tmp =     that.getElement(0, 3).getReal()
				- that.getElement(1, 2).getReal()
				- that.getElement(2, 1).getReal()
				+ that.getElement(3, 0).getReal();
		associate.setElement(1, 2, new ComplexNumber(tmp/4., 0));
		
		tmp =   - that.getElement(0, 2).getReal()
				- that.getElement(1, 3).getReal()
				- that.getElement(2, 0).getReal()
				- that.getElement(3, 1).getReal();
		associate.setElement(1, 3, new ComplexNumber(tmp/4., 0));


		tmp =   - that.getElement(0, 2).getReal()
				+ that.getElement(1, 3).getReal()
				+ that.getElement(2, 0).getReal()
				- that.getElement(3, 1).getReal();
		associate.setElement(2, 0, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 3).getReal()
				- that.getElement(1, 2).getReal()
				- that.getElement(2, 1).getReal()
				- that.getElement(3, 0).getReal();
		associate.setElement(2, 1, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 0).getReal()
				+ that.getElement(1, 1).getReal()
				- that.getElement(2, 2).getReal()
				+ that.getElement(3, 3).getReal();
		associate.setElement(2, 2, new ComplexNumber(tmp/4., 0));
		
		tmp =     that.getElement(0, 1).getReal()
				+ that.getElement(1, 0).getReal()
				- that.getElement(2, 3).getReal()
				- that.getElement(3, 2).getReal();
		associate.setElement(2, 3, new ComplexNumber(tmp/4., 0));
		
		tmp =   - that.getElement(0, 3).getReal()
				- that.getElement(1, 2).getReal()
				+ that.getElement(2, 1).getReal()
				+ that.getElement(3, 0).getReal();
		associate.setElement(3, 0, new ComplexNumber(tmp/4., 0));

		tmp =     that.getElement(0, 2).getReal()
				- that.getElement(1, 3).getReal()
				+ that.getElement(2, 0).getReal()
				- that.getElement(3, 1).getReal();
		associate.setElement(3, 1, new ComplexNumber(tmp/4., 0));

		tmp =   - that.getElement(0, 1).getReal()
				- that.getElement(1, 0).getReal()
				- that.getElement(2, 3).getReal()
				- that.getElement(3, 2).getReal();
		associate.setElement(3, 2, new ComplexNumber(tmp/4., 0));
		
		tmp =   - that.getElement(0, 0).getReal()
				+ that.getElement(1, 1).getReal()
				+ that.getElement(2, 2).getReal()
				- that.getElement(3, 3).getReal();
		associate.setElement(3, 3, new ComplexNumber(tmp/4., 0));


		return associate;
	}
	
	/**
	 * Returns quaternion from left 
	 */
	public static double[] abcd(Matrix that) {
		
		double a,b,c,d;
		
		a = Math.sqrt(Math.pow(that.getElement(0, 0).getReal(), 2)
				    + Math.pow(that.getElement(0, 1).getReal(), 2)
				    + Math.pow(that.getElement(0, 2).getReal(), 2)
				    + Math.pow(that.getElement(0, 3).getReal(), 2));
		
		b = Math.sqrt(Math.pow(that.getElement(1, 0).getReal(), 2)
					+ Math.pow(that.getElement(1, 1).getReal(), 2)
					+ Math.pow(that.getElement(1, 2).getReal(), 2)
					+ Math.pow(that.getElement(1, 3).getReal(), 2));

		c = Math.sqrt(Math.pow(that.getElement(2, 0).getReal(), 2)
			    	+ Math.pow(that.getElement(2, 1).getReal(), 2)
			    	+ Math.pow(that.getElement(2, 2).getReal(), 2)
			    	+ Math.pow(that.getElement(2, 3).getReal(), 2));

		d = Math.sqrt(Math.pow(that.getElement(3, 0).getReal(), 2)
			    	+ Math.pow(that.getElement(3, 1).getReal(), 2)
			    	+ Math.pow(that.getElement(3, 2).getReal(), 2)
			    	+ Math.pow(that.getElement(3, 3).getReal(), 2));

		// determine signs
		if (that.getElement(0, 0).getReal() < 0)
			a = -Math.abs(a);
		else 
			a = Math.abs(a);
		
		if (that.getElement(1, 0).getReal() < 0)
			b = -Math.abs(b);
		else 
			b = Math.abs(b);
		
		if (that.getElement(2, 0).getReal() < 0)
			c = -Math.abs(c);
		else 
			c = Math.abs(c);
		
		if (that.getElement(3, 0).getReal() < 0)
			d = -Math.abs(d);
		else 
			d = Math.abs(d);
				
		return new double[]{a, b, c, d};
	}
	
	/**
	 * Returns quaternion from right 
	 */
	public static double[] pqrs(Matrix that, double a) {
	
		double p,q,r,s;
		
		p = Math.sqrt(Math.pow(that.getElement(0, 0).getReal(), 2)
				    + Math.pow(that.getElement(1, 0).getReal(), 2)
				    + Math.pow(that.getElement(2, 0).getReal(), 2)
				    + Math.pow(that.getElement(3, 0).getReal(), 2));
	
		q = Math.sqrt(Math.pow(that.getElement(0, 1).getReal(), 2)
					+ Math.pow(that.getElement(1, 1).getReal(), 2)
					+ Math.pow(that.getElement(2, 1).getReal(), 2)
					+ Math.pow(that.getElement(3, 1).getReal(), 2));

		r = Math.sqrt(Math.pow(that.getElement(0, 2).getReal(), 2)
			    	+ Math.pow(that.getElement(1, 2).getReal(), 2)
			    	+ Math.pow(that.getElement(2, 2).getReal(), 2)
			    	+ Math.pow(that.getElement(3, 2).getReal(), 2));

		s = Math.sqrt(Math.pow(that.getElement(0, 3).getReal(), 2)
			    	+ Math.pow(that.getElement(1, 3).getReal(), 2)
			    	+ Math.pow(that.getElement(2, 3).getReal(), 2)
			    	+ Math.pow(that.getElement(3, 3).getReal(), 2));
		
		// determine signs
		if (that.getElement(0, 0).getReal() < 0)
			p = -Math.abs(p);
		else 
			p = Math.abs(p);
		
		if (that.getElement(0, 1).getReal() < 0)
			q = -Math.abs(q);
		else 
			q = Math.abs(q);
		
		if (that.getElement(0, 2).getReal() < 0)
			r = -Math.abs(r);
		else 
			r = Math.abs(r);
		
		if (that.getElement(0, 3).getReal() < 0)
			s = -Math.abs(s);
		else 
			s = Math.abs(s);

		if (a < 0) {
			p = -p;
			q = -q;
			r = -r; 
			s = -s;
		}
		
		return new double[]{p, q, r, s};
	}
	
}
