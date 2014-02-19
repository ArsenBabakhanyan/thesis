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
		ComplexNumber comNum = new ComplexNumber();
				
		comNum.setReal(real.add(that.getReal()));
		comNum.setImaginary(imaginary.add(that.getImaginary()));
		
		return comNum;
	}
	
	public ComplexNumber sub(ComplexNumber that) {
		ComplexNumber comNum = new ComplexNumber();
		
		comNum.setReal(real.subtract(that.getReal()));
		comNum.setImaginary(imaginary.subtract(that.getImaginary()));
		
		return comNum;
	}
	
	public ComplexNumber mul(ComplexNumber that) {
		ComplexNumber comNum = new ComplexNumber();
		
		comNum.setReal((real.multiply(that.getReal())).subtract(imaginary.multiply(that.getImaginary())));
		comNum.setImaginary((imaginary.multiply(that.getReal())).add(real.multiply(that.getImaginary())));
		
		return comNum;
	}
	
	public ComplexNumber div(ComplexNumber that) {
		ComplexNumber comNum = new ComplexNumber();
		
		BigDecimal denom = (that.getReal().pow(2)).add(that.getImaginary().pow(2));
		
		comNum.setReal((real.multiply(that.getReal()).add(imaginary.multiply(that.getImaginary()))).divide(denom));
		comNum.setImaginary((imaginary.multiply(that.getReal()).subtract(real.multiply(that.getImaginary()))).divide(denom));
		
		return comNum;
	}
	
	public String toString() {
		String str = "{" + real.toString();
		
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
