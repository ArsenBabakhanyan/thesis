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
	public static Matrix[] KAKDec(Matrix G) {
		Matrix[] KAK = new Matrix[3];
		double a,b,c;
		
		// M^2 = Y G^* Y G
		Matrix M_2 = Matrix.PAULIY();
		M_2.mul(Matrix.adjoint(G));
		M_2.mul(Matrix.PAULIY());
		M_2.mul(G);
		
		double tmp = Math.acos(M_2.getElement(0, 0).getReal().doubleValue());
		b = tmp/2;
		
		double tmp1 = Math.acos((M_2.getElement(0, 0).getImaginary().doubleValue())/(Math.sin(tmp)));

		if (M_2.getElement(0, 1).getImaginary().doubleValue() < 0) {
			tmp1 = 2*Math.PI - tmp1;
		}
		
		c = tmp1/2;
		
		KAK[1] = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(Math.cos(b/2),"0"), new ComplexNumber(Math.sin(b/2),"0")},
				{new ComplexNumber(-Math.sin(b/2),"0"), new ComplexNumber(Math.cos(b/2),"0")}
		});
		KAK[2] = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(Math.cos(c),Math.sin(c)), new ComplexNumber()},
				{new ComplexNumber(), new ComplexNumber(Math.cos(c), (-Math.sin(c)))}
		});
		KAK[0] = Matrix.mul(G, Matrix.adjoint(KAK[2]));
		KAK[0].mul(Matrix.adjoint(KAK[1]));
		
		a = Math.atan2(KAK[0].getElement(0, 1).getReal().doubleValue(), KAK[0].getElement(0, 0).getReal().doubleValue());
		
		if (a < 0)
			a += Math.PI;
		
		// for testing purposes ! 
		System.out.println("a = " + a + ", b = " + b + ", c = " + c);
		
		return KAK;
	}
}
