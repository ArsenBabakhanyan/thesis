package decomposition;
import matrix.Matrix;
import complex_number.ComplexNumber;
//import java.math.BigDecimal;

/**
 * This class provides all functionality concerning decomposition of one qubit systems
 * @author Arsen Babakhanyan
 *
 */
public class OneQubit {
	
	/**
	 * This method decomposes Matrix G into three matrices a returns array containing the matrices
	 */
	public static double[] KAKDec(Matrix G) {
		double[] abc = new double[3];
		
		// M^2 = Y G^* Y G
		Matrix M_2 = Matrix.mul(Matrix.PAULIY(), Matrix.adjoint(G), Matrix.PAULIY(), G);
		
		abc[1] = Math.acos(M_2.getElement(0, 0).getReal())/2.;
		
		abc[2] = Math.acos((M_2.getElement(0, 0).getImaginary())/(Math.sin(abc[1]*2)));

		if (M_2.getElement(0, 1).getImaginary() < 0) {
			abc[2] = 2*Math.PI - abc[2];
		}
		
		abc[2] /= 2.;
		
		Matrix tp = Matrix.mul(G, Matrix.adjoint(RY(abc[2])), Matrix.adjoint(RZ(abc[1])));
		
		abc[0] = Math.atan2(tp.getElement(0, 1).getReal(), tp.getElement(0, 0).getReal());
		
		if (abc[0] < 0)
			abc[0] += Math.PI;
		
		return abc;
	}
	
	/**
	 * Returns rotation Matrix around X axis by andle = value 
	 */
	public static Matrix RX(double value) {

		double cosa = Math.cos(value);
		double sina = Math.sin(value);

		Matrix tmp = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(cosa, 0), new ComplexNumber(0, sina) },
				{new ComplexNumber(0, sina), new ComplexNumber(cosa, 0)}
		});

		return tmp;
	}
	
	/**
	 * Returns rotation Matrix around Y axis by angle = value 
	 */
	public static Matrix RY(double value) {

		double cosa = Math.cos(value);
		double sina = Math.sin(value);

		Matrix tmp = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(cosa, 0), new ComplexNumber(sina, 0) },
				{new ComplexNumber(-sina, 0), new ComplexNumber(cosa, 0)}
		});

		return tmp;
	}
	
	/**
	 * Returns rotation Matrix around Z axis by angle = value 
	 */
	public static Matrix RZ(double value) {
		double cosa = Math.cos(value);
		double sina = Math.sin(value);
		
		Matrix tmp = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(cosa, sina), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(cosa, -sina)}
		});
		return tmp;
	}
	
	
	/**
	 * Returns RY(a) * RZ(b) * RY(c) matrix 
	 */
	public static Matrix YZYMatrix(double a, double b, double c) {
		return Matrix.mul(RY(a), RZ(b), RY(c));
	}
}
