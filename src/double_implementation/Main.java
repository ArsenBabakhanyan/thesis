import java.util.Arrays;

import matrix.Matrix;
import complex_numbers.ComplexNumber;
import decomposition.TwoQubit;


public class Main {

	public static void main(String[] args) {
		Matrix a = new Matrix(new ComplexNumber[][] {
				{new ComplexNumber(0.127774690458, 0.628226236624), new ComplexNumber(-0.284765147979, 0.174667496593), new ComplexNumber(0.16219383748, -0.590367257527), new ComplexNumber(0.315586101297, -0.0545029652155)},
				{new ComplexNumber(-0.568506183644, -0.1684967195), new ComplexNumber(-0.0455575166338, -0.347446440038), new ComplexNumber(-0.511944142827, -0.436709206973), new ComplexNumber(0.121322978658, -0.241027087023)},
				{new ComplexNumber(0.379884668354, -0.115050663833), new ComplexNumber(-0.434721987989, -0.474929243691), new ComplexNumber(-0.254142656678, 0.00894505609475), new ComplexNumber(0.191835840767, 0.571349714599)},
				{new ComplexNumber(-0.0310624392189, 0.280893408862), new ComplexNumber(0.583338666096, -0.103829511202), new ComplexNumber(-0.177080136504, 0.27628254519), new ComplexNumber(0.667145481204, 0.127659462462)}
		});
//		Peters SU4 output    ------- can be due to eigenvalue function --- maybe
//		output start
//		[0.27488930302907993, 
//		 0.56372369454403393, 
//		 0.45083977982533319, 
//		 1.3953589923999798, 
//		 0.45466213951232826, 
//		 2.5651081541285619, 
//		 0.80404659562636416, 
//		 0.7434710817009913, 
//		 0.73175925243292173, 
//		 0.5161030777426554, 
//		 0.57988749564247932, 
//		 0.77156029209349941, 
//		 2.0413503278276099, 
//		 1.3519900722293463, 
//		 2.9486259648959403]
//		 output end

		
		System.out.println(Arrays.toString(TwoQubit.SU4(a)));
	}

}
