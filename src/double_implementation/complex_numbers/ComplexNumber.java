package complex_numbers;

//import java.math.BigDecimal;

/**
 * Class to work with ComplexNumbers
 * @author Arsen Babakhanyan
 *
 */
public class ComplexNumber {
	
	// real part of Complex Number
	private double real;
	// imaginary part of Complex Number
	private double imaginary;
	
	/**
	 *  Makes a new ComplexNumber with 0 + 0i value
	 */
	public ComplexNumber() {
		this(0, 0);
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
	public ComplexNumber(double re, double im) {
		real = re;
		imaginary = im;
	}
	
	/**
	 * Returns real part of the ComplexNumber
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Sets the real part of this number to real in attribute
	 */
	public void setReal(double real) {
		this.real = real;
	}
	
	/**
	 * Returns imaginary part of the ComplexNumber
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Sets the imaginary part of this number to imaginary in attribute
	 */
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
	
	/**
	 * Conjugates the ComplexNumber
	 */
	public void conjugate() {
		imaginary = -imaginary;
	}
	
	/**
	 * Computes this += that 
	 */
	public void add(ComplexNumber that) {
		double tmp = real + that.getReal();
		imaginary = imaginary + that.getImaginary();
		real = tmp;
	}
	
	/**
	 * Computes this -= that
	 */
	public void sub(ComplexNumber that) {
		double tmp;
		tmp = real - that.getReal();
		imaginary = imaginary - that.getImaginary();
		real = tmp;
	}
	
	/**
	 * Computes this *= that
	 */
	public void mul(ComplexNumber that) {
		double tmp;
		tmp = (real*that.getReal()) - (imaginary*that.getImaginary());
		imaginary = (imaginary*that.getReal()) + (real * (that.getImaginary()));
		real = tmp;
		
	}
	
	/**
	 * Computes this /= that
	 */
	public void div(ComplexNumber that) {
		double tmp;
		double denom = (that.getReal()*that.getReal()) + (that.getImaginary()*that.getImaginary());
		
		tmp = (real * that.getReal() + imaginary * that.getImaginary())/denom;
		imaginary = (imaginary * that.getReal() - real * that.getImaginary())/denom;
		real = tmp;
	}
	
	/**
	 * Negates the complexNumber
	 */
	public void neg() {
		real = -real;
		imaginary = -imaginary;
	}


	/**
	 * Returns String { [real] +/- [imaginary]i }
	 */
	public String toString() {
		String str = "{" + real;
		
		if (Math.signum(imaginary) >= 0) {
			str += " + " + Math.abs(imaginary);
		}
		else {
			str += " - " + Math.abs(imaginary);
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
	 * Returns sum of N complex numbers 
	 */
	public static ComplexNumber addN(ComplexNumber first, ComplexNumber... second) {
		ComplexNumber tmp = new ComplexNumber(first);
		for (ComplexNumber cn : second) {
			tmp.add(cn);
		}
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
	
	/**
	 * Returns abs of the given ComplexNumber <br />
	 * sqrt (re^2 + im^2)
	 */
	public static double abs(ComplexNumber that) {
		double absRe = Math.abs(that.getReal());
	    double absIm = Math.abs(that.getImaginary());
	    if((absRe==0)&&(absIm==0)) {
	      return 0;
	    } else if(absRe>absIm) {
	      double temp = absIm/absRe;
	      return absRe*Math.sqrt(1+temp*temp);
	    } else {
	      double temp = absRe/absIm;
	      return absIm*Math.sqrt(1+temp*temp);
	    }
	}
	
	/**
	 *	Returns sqrt of the ComplexNumber 
	 */
	public static ComplexNumber sqrt(ComplexNumber that) {
	    ComplexNumber c;
	    double absRe, absIm, w, r;
	    if((that.getReal()==0)&&(that.getImaginary()==0)) {
	      c = new ComplexNumber();
	    } else {
	      absRe = Math.abs(that.getReal());
	      absIm = Math.abs(that.getImaginary());
	      if(absRe>=absIm) {
	        r = absIm/absRe;
	        w = Math.sqrt(absRe)*Math.sqrt(0.5*(1.0+Math.sqrt(1.0+r*r)));
	      } else {
	        r = absRe/absIm;
	        w = Math.sqrt(absIm)*Math.sqrt(0.5*(r+Math.sqrt(1.0+r*r)));
	      }
	      if(that.getReal()>=0) {
	        c = new ComplexNumber(w, that.getImaginary()/(2.0*w));
	      } else {
	        if(that.getImaginary()<0) {
	          w = -w;
	        }
	        c = new ComplexNumber(that.getImaginary()/(2.0*w), w);
	      }
	    }
	    return c;
	  }
	
	/**
	 * Returns a new ComplexNumber with -that.re and -that.im
	 */
	public static ComplexNumber neg(ComplexNumber that) {
		return new ComplexNumber(-that.getReal(), -that.getImaginary());
	}
	
	/**
	 * Returns arguments of given ComplexNumbers
	 */
	public static double[] angle(ComplexNumber... vals) {
		int len = vals.length;
		double[] angles = new double[len];
		
		for (int i = 0; i < len; i++)
			angles[i] = Math.atan2(vals[i].getImaginary(), vals[i].getReal());
		
		return angles;
	}
	
	/**
	 * Returns ComplexNumber based on argument (angle tetta)
	 */
	public static ComplexNumber argToNum(double arg) {
		double re = Math.cos(arg);
		double im = Math.sin(arg);
		
		return new ComplexNumber(re, im);
	}
	
}
