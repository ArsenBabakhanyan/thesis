package decomposition;

import java.util.Arrays;

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
	
	
	
	
	
	
	public static double[] SU4(Matrix that) {
		Matrix G = Matrix.mul(Matrix.adjoint(Matrix.B()), that, Matrix.B());
		Matrix Msquare = Matrix.mul(Matrix.transpose(G), G);
		
		double[] abc = new double[3];
		Matrix[] AK = MsqDec(Msquare, abc);
		
		Matrix k2SO = AK[0];
		Matrix ASo = AK[1];
		
		double[] factorsk2SO = positiveSU2SU2(k2SO);
		
		Matrix P2 = OneQubit.YZYMatrix(factorsk2SO[0], factorsk2SO[1], factorsk2SO[2]);
		Matrix Q2 = OneQubit.YZYMatrix(factorsk2SO[3], factorsk2SO[4], factorsk2SO[5]);
		
		Matrix K2 = Matrix.kron(P2, Q2);
		Matrix A = Matrix.mul(Matrix.B(), ASo, Matrix.adjoint(Matrix.B()));
		Matrix K1 = Matrix.mul(that, Matrix.adjoint(K2), Matrix.adjoint(A));
				
		Matrix K1So = Matrix.mul(Matrix.adjoint(Matrix.B()), K1, Matrix.B());
		
		double[] abcd12 = SO4(K1So);
//					a2,b2,c2 = su2.fcartanYZYf(P1)
//				    a1,b1,c1 = su2.fcartanYZYf(Q1)
//				    p2,q2,r2 = su2.fcartanYZYf(P2)
//				    p1,q1,r1 = su2.fcartanYZYf(Q2)
//
//		return [a2,b2,c2,a1,b1,c1,a,b,c,p2,q2,r2,p1,q1,r1,K1,A,K2,P2,Q2,P1,Q1]
		
		
		return new double[] {abcd12[0], abcd12[1], abcd12[2], abcd12[3], abcd12[4], abcd12[5],
							 abc[0], abc[1], abc[2], 
							 factorsk2SO[0], factorsk2SO[1], factorsk2SO[2], factorsk2SO[3], factorsk2SO[4], factorsk2SO[5] };
	}
	
	public static Matrix[] MsqDec (Matrix Msq, double[] abc) {

		Object[] eigenvalEigenvect  = Matrix.eigenValuesAndVector(Msq);
		
		ComplexNumber[] MsqEigs = (ComplexNumber[])eigenvalEigenvect[0];
		
		ComplexNumber[][] MsqEigVecs = (ComplexNumber[][])eigenvalEigenvect[1];
		
		double[] MsqArgs = ComplexNumber.angle(MsqEigs);
		
		double[] MArgs = new double[MsqArgs.length];
		Arrays.sort(MArgs);

		for (int i = 0; i < MsqArgs.length; i++)
			MArgs[i] = MsqArgs[i]/2;
		
		double sum = 0;
		double t0, t1, t2, t3;
		for (int i = 0; i < MArgs.length; i++)
			sum += MArgs[i];
 
////		System.out.println(sum);

		ComplexNumber[][] k2C = new ComplexNumber[4][4];
		
		if( Math.abs(sum - Math.PI) < 0.00001 ) {
			t0 = MArgs[1];
			t1 = MArgs[2];
			t2 = MArgs[3]-Math.PI;
			t3 = MArgs[0];
		
			k2C[0] = MsqEigVecs[1];
			k2C[1] = MsqEigVecs[2];
			k2C[2] = MsqEigVecs[3];
			k2C[3] = MsqEigVecs[0];
			
			for (int i = 0; i < k2C[3].length; i++)
				k2C[3][i].neg();
			
		} else {
			t0 = MArgs[2];
			t1 = MArgs[3];
			t2 = MArgs[0];
			t3 = MArgs[1];
			
			k2C[0] = MsqEigVecs[2];
			k2C[1] = MsqEigVecs[3];
			k2C[2] = MsqEigVecs[0];
			k2C[3] = MsqEigVecs[1];
		}
		
		Matrix K2so4 = Matrix.transpose(new Matrix(k2C));
		K2so4.adjoint();
		
		Matrix A = Matrix.matrixFromArgs(t0, t1, t2, t3);
		
		abc[0] = (t0 + t1)/2.0;
		abc[1] = (t1 + t3)/2.0;
		abc[2] = (t0 + t3)/2.0;

		return new Matrix[]{K2so4, A};
	}
	
	public static double[] positiveSU2SU2(Matrix that) {
		Matrix A = associateSO4(that);
	
		double[] abcd = abcd(A);
		double[] pqrs = pqrs(A, abcd[0]);
		
		double t1 = abcd[0]*abcd[1];
		double t2 = abcd[0]*abcd[2];
		double t3 = pqrs[0]*pqrs[1];
		double t4 = pqrs[0]*pqrs[2];
		
		double[] abcdf = new double[4];
		double[] pqrsf = new double[4];

		if (t1<0 && t2<0 && t3>0 && t4<0 ) {   // case ZI
			abcdf[0] = -abcd[2];
			abcdf[1] = abcd[3];
			abcdf[2] = abcd[0];
			abcdf[3] = -abcd[1];
			pqrsf[0] = pqrs[2];
			pqrsf[1] = pqrs[3];
			pqrsf[2] = -pqrs[0];
			pqrsf[3] = -pqrs[1];
		} else if (t1<0 && t2>0 && t3<0 && t4<0 ) {   // case IZ
			abcdf[0] = abcd[1];
			abcdf[1] = -abcd[0];
			abcdf[2] = abcd[3];
			abcdf[3] = -abcd[2];
			pqrsf[0] = -pqrs[1];
			pqrsf[1] = pqrs[0];
			pqrsf[2] = pqrs[3];
			pqrsf[3] = -pqrs[2];
		} else if (t1>0 && t2<0 && t3<0 && t4>0 ) {   // case ZZ
			abcdf[0] = abcd[3];
			abcdf[1] = abcd[2];
			abcdf[2] = -abcd[1];
			abcdf[3] = -abcd[0];
			pqrsf[0] = -pqrs[3];
			pqrsf[1] = pqrs[2];
			pqrsf[2] = -pqrs[1];
			pqrsf[3] = pqrs[0];
		} else {   // case II
			abcdf[0] = abcd[0];
			abcdf[1] = abcd[1];
			abcdf[2] = abcd[2];
			abcdf[3] = abcd[3];
			pqrsf[0] = pqrs[0];
			pqrsf[1] = pqrs[1];
			pqrsf[2] = pqrs[2];
			pqrsf[3] = pqrs[3];
		}

		final Matrix leftFactor = leftFactor(abcdf);

		final Matrix rightFactor = rightFactor(pqrsf);

		final double[] a = new double[3];
		final double[] b = new double[3];

		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				Matrix left = Matrix.mul(Matrix.B(), leftFactor, Matrix.adjoint(Matrix.B()));
				double[] m = SU2SU2(left);
				a[0] = m[3];
				a[1] = m[4];
				a[2] = m[5];
			}
		});

		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				Matrix right = Matrix.mul(Matrix.B(), rightFactor, Matrix.adjoint(Matrix.B()));
				double[] m = SU2SU2(right);
				b[0] = m[0];
				b[1] = m[1];
				b[2] = m[2];
			}
		});

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		return new double[] { a[0], a[1], a[2], b[0], b[1], b[2]};

	}
	
	
}
