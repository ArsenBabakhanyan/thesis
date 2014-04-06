package decomposition;

import java.util.LinkedList;
import java.util.List;

import complex_numbers.ComplexNumber;

import matrix.Matrix;



public class ManyQubit {
	
	
	
	public static DecompositionResultContainer QSD(Matrix that, boolean flag) {
		int numberOfQubits = log2(that.len());
		List<Matrix> currentLevelDecomp = new LinkedList<>();	
		List<List<Matrix>> bdiag = new LinkedList<>();
		List<List<Double>> bdiadparams = new LinkedList<>();
		
		currentLevelDecomp.add(that);
		
		for (int i = 0; i < numberOfQubits-2; i++) {
			List<Matrix> gelato = new LinkedList<>(), 
					blockZ = new LinkedList<>(),
					blockX = new LinkedList<>();
			List<Double>paramZ = new LinkedList<>(),
					paramX = new LinkedList<>();
			for (int j = 0; j < currentLevelDecomp.size(); j++) {
				LevelDecompositionResultContainer elements = levelDecompose(currentLevelDecomp.get(j), flag);
				gelato.add(elements.getKpkp1());
				gelato.add(elements.getKpph1());
				gelato.add(elements.getPhkp1());
				gelato.add(elements.getPhph1());
				
				blockZ.add(elements.getKpd());
				blockZ.add(elements.getPhd());
				blockX.add(elements.getA());
				
				paramZ.add(elements.getDiag1());
				paramZ.add(elements.getDiag2());
				paramX.add(elements.getAlpha());
			}
			currentLevelDecomp = gelato;
			
			bdiag.add(blockZ);
			bdiag.add(blockX);
			
			bdiadparams.add(paramZ);
			bdiadparams.add(paramX);
		}
		
		DecompositionResultContainer res = new DecompositionResultContainer();
		
		res.setInitialMatrix(that);
		res.setTwoQubitFactors(currentLevelDecomp);
		res.setControlledZXLevelElements(bdiag);
		res.setAnglesOfLevelElements(bdiadparams);
		
		return res;
	}
	
	
	public static LevelDecompositionResultContainer levelDecompose(Matrix that, boolean flag) {
		Matrix M2 = MSquared(that);
		uniControlXRot(M2);
		return null;
	}
	
	private static void uniControlXRot(Matrix that) {
		Object[] obj = Matrix.eigenValuesAndVector(that);
		ComplexNumber[] eigens = (ComplexNumber[]) obj[0];
		ComplexNumber[][] eigenVectors = (ComplexNumber[][]) obj[1];
		Matrix P = new Matrix(eigenVectors);
		// chop(P)
		P.makeSpecial();
		
		
		
	}
	
	private static Matrix MSquared(Matrix that) {
		return Matrix.mul(SUInvolution(Matrix.adjoint(that)), that);
	}
	
	private static Matrix SUInvolution(Matrix that) {
		int numberOfQubits = log2(that.len());
		Matrix identity2x2 = Matrix.Identity2x2();
		Matrix IZN = Matrix.PAULIZ();
		
		for (int i = 0; i < numberOfQubits-1; i++)
			IZN = Matrix.kron(identity2x2, IZN);
		return Matrix.mul(IZN, that, IZN);
	}
	
	/**
	 * Returns logarithm base 2 of the value
	 */
	private static int log2(int val) {
		double log10_val = Math.log10(val);
		double log10_2 = Math.log10(2);
		
		return (int)Math.round(log10_val/log10_2);
	}
	
	public static class DecompositionResultContainer {
		private Matrix initialMatrix;
		private List<Matrix> twoQubitFactors;
		private List<List<Matrix>> controlledZXLevelElements;
		private List<List<Double>> anglesOfLevelElements;
		public Matrix getInitialMatrix() {
			return initialMatrix;
		}
		public void setInitialMatrix(Matrix initialMatrix) {
			this.initialMatrix = initialMatrix;
		}
		public List<Matrix> getTwoQubitFactors() {
			return twoQubitFactors;
		}
		public void setTwoQubitFactors(List<Matrix> twoQubitFactors) {
			this.twoQubitFactors = twoQubitFactors;
		}
		public List<List<Matrix>> getControlledZXLevelElements() {
			return controlledZXLevelElements;
		}
		public void setControlledZXLevelElements(
				List<List<Matrix>> controlledZXLevelElements) {
			this.controlledZXLevelElements = controlledZXLevelElements;
		}
		public List<List<Double>> getAnglesOfLevelElements() {
			return anglesOfLevelElements;
		}
		public void setAnglesOfLevelElements(List<List<Double>> anglesOfLevelElements) {
			this.anglesOfLevelElements = anglesOfLevelElements;
		}
	}
	
	private class LevelDecompositionResultContainer {
		
		private Matrix kpkp1;
		private Matrix kpph1;
		private Matrix phkp1;
		private Matrix phph1;
		private Matrix kpd;
		private Matrix phd;
		private Matrix A;
		private double diag1;
		private double alpha;
		private double diag2;
		
		public Matrix getKpkp1() {
			return kpkp1;
		}
		public void setKpkp1(Matrix kpkp1) {
			this.kpkp1 = kpkp1;
		}
		public Matrix getKpph1() {
			return kpph1;
		}
		public void setKpph1(Matrix kpph1) {
			this.kpph1 = kpph1;
		}
		public Matrix getPhkp1() {
			return phkp1;
		}
		public void setPhkp1(Matrix phkp1) {
			this.phkp1 = phkp1;
		}
		public Matrix getPhph1() {
			return phph1;
		}
		public void setPhph1(Matrix phph1) {
			this.phph1 = phph1;
		}
		public Matrix getKpd() {
			return kpd;
		}
		public void setKpd(Matrix kpd) {
			this.kpd = kpd;
		}
		public Matrix getPhd() {
			return phd;
		}
		public void setPhd(Matrix phd) {
			this.phd = phd;
		}
		public Matrix getA() {
			return A;
		}
		public void setA(Matrix a) {
			A = a;
		}
		public double getDiag1() {
			return diag1;
		}
		public void setDiag1(double diag1) {
			this.diag1 = diag1;
		}
		public double getAlpha() {
			return alpha;
		}
		public void setAlpha(double alpha) {
			this.alpha = alpha;
		}
		public double getDiag2() {
			return diag2;
		}
		public void setDiag2(double diag2) {
			this.diag2 = diag2;
		}
		
	}
}
