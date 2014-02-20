package complex_number;
import java.math.BigDecimal;

/**
 * Class to work with ComplexNumbers
 * @author Arsen Babakhanyan
 *
 */
public class ComplexNumber {
	
	private BigDecimal real;
	private BigDecimal imaginary;
	
	/**
	 *  Makes a new ComplexNumber with 0 + 0i value
	 */
	public ComplexNumber() {
		this("0", "0");
	}
	
	/**
	 * Makes a copy ComplexNumber of ComplexNumber that
	 */
	public ComplexNumber(ComplexNumber that) {
		real = that.getReal();
		imaginary = that.getImaginary();
	}
	
	/**
	 * Makes a new ComplexNumber based on re and im values
	 */
	public ComplexNumber(Object re, Object im) {
		real = new BigDecimal(re.toString());
		imaginary = new BigDecimal(im.toString());
	}
	
	/**
	 * Returns real part of the ComplexNumber
	 */
	public BigDecimal getReal() {
		return real;
	}
	
	/**
	 * Sets the real part of this number to real in attribute
	 */
	public void setReal(BigDecimal real) {
		this.real = real;
	}
	
	/**
	 * Returns imaginary part of the ComplexNumber
	 */
	public BigDecimal getImaginary() {
		return imaginary;
	}
	
	/**
	 * Sets the imaginary part of this number to imaginary in attribute
	 */
	public void setImaginary(BigDecimal imaginary) {
		this.imaginary = imaginary;
	}
	
	/**
	 * Conjugates the ComplexNumber
	 */
	public void conjugate() {
		imaginary = imaginary.negate();
	}
	
	/**
	 * Computes this += that 
	 */
	public void add(ComplexNumber that) {
		BigDecimal tmp;
		tmp = real.add(that.getReal());
		imaginary = imaginary.add(that.getImaginary());
		real = tmp;
	}
	
	/**
	 * Computes this -= that
	 */
	public void sub(ComplexNumber that) {
		BigDecimal tmp;
		tmp = real.subtract(that.getReal());
		imaginary = imaginary.subtract(that.getImaginary());
		real = tmp;
	}
	
	/**
	 * Computes this *= that
	 */
	public void mul(ComplexNumber that) {
		BigDecimal tmp;
		tmp = ((real.multiply(that.getReal())).subtract(imaginary.multiply(that.getImaginary())));
		imaginary = ((imaginary.multiply(that.getReal())).add(real.multiply(that.getImaginary())));
		real = tmp;
		
	}
	
	/**
	 * Computes this /= that
	 */
	public void div(ComplexNumber that) {
		BigDecimal tmp;
		BigDecimal denom = (that.getReal().pow(2)).add(that.getImaginary().pow(2));
		
		tmp = (real.multiply(that.getReal()).add(imaginary.multiply(that.getImaginary()))).divide(denom);
		imaginary = (imaginary.multiply(that.getReal()).subtract(real.multiply(that.getImaginary()))).divide(denom);
		real = tmp;
	}

	/**
	 * Returns String { [real] +/- [imaginary]i }
	 */
	public String toString() {
		String str = "{" + real;
		
		if (imaginary.signum() >= 0) {
			str += " + " + imaginary.abs();
		}
		else {
			str += " - " + imaginary.abs();
		}
		str += "i}";
		
		return str;
	}
	

	/**
	 * Returns conjugate of ComplexNumber that without changing it
	 */
	public static ComplexNumber conjugate(ComplexNumber that) {
		ComplexNumber tmp = new ComplexNumber(that.getReal(), that.getImaginary()); 
		tmp.conjugate();
		return tmp;
	}
	
	/**
	 * Returns first + second
	 */
	public static ComplexNumber add(ComplexNumber first, ComplexNumber second) {
		ComplexNumber tmp = new ComplexNumber(first);
		tmp.add(second);
		return tmp;
	}
	
	/**
	 *  Returns first - second
	 */
	public static ComplexNumber sub(ComplexNumber first, ComplexNumber second) {
		ComplexNumber tmp = new ComplexNumber(first);
		tmp.sub(second);
		return tmp;
	}
	
	/**
	 * Returns first * second
	 */
	public static ComplexNumber mul(ComplexNumber first, ComplexNumber second) {
		ComplexNumber tmp = new ComplexNumber(first);
		tmp.mul(second);
		return tmp;
	}
	
	/**
	 * Returns first / second
	 */
	public static ComplexNumber div(ComplexNumber first, ComplexNumber second) {
		ComplexNumber tmp = new ComplexNumber(first);
		tmp.div(second);
		return tmp;
	}
	
}
