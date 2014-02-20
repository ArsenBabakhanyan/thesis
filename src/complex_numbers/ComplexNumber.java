package complex_number;

import java.math.BigDecimal;

public class ComplexNumber {
	
	private BigDecimal real;
	private BigDecimal imaginary;
	
	public ComplexNumber() {
		this("0", "0");
	}
	
	public ComplexNumber(ComplexNumber that) {
		real = that.getReal();
		imaginary = that.getImaginary();
	}
	
	public ComplexNumber(Object re, Object im) {
		real = new BigDecimal(re.toString());
		imaginary = new BigDecimal(im.toString());
	}
	
	public BigDecimal getReal() {
		return real;
	}
	public void setReal(BigDecimal real) {
		this.real = real;
	}
	
	public BigDecimal getImaginary() {
		return imaginary;
	}
	public void setImaginary(BigDecimal imaginary) {
		this.imaginary = imaginary;
	}
	
	public void conjugate() {
		imaginary = imaginary.negate();
	}
	
	public ComplexNumber add(ComplexNumber that) {
				
		real = real.add(that.getReal());
		imaginary = imaginary.add(that.getImaginary());
		
		return this;
	}
	
	public ComplexNumber sub(ComplexNumber that) {
		
		real = real.subtract(that.getReal());
		imaginary = imaginary.subtract(that.getImaginary());
		
		return this;
	}
	
	public ComplexNumber mul(ComplexNumber that) {
				
		real = (real.multiply(that.getReal())).subtract(imaginary.multiply(that.getImaginary()));
		imaginary = (imaginary.multiply(that.getReal())).add(real.multiply(that.getImaginary()));
		
		return this;
	}
	
	public ComplexNumber div(ComplexNumber that) {
		
		BigDecimal denom = (that.getReal().pow(2)).add(that.getImaginary().pow(2));
		
		real = (real.multiply(that.getReal()).add(imaginary.multiply(that.getImaginary()))).divide(denom);
		imaginary = (imaginary.multiply(that.getReal()).subtract(real.multiply(that.getImaginary()))).divide(denom);
		
		return this;
	}
	
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
	
}
