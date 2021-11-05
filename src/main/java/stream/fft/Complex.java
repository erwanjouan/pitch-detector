package stream.fft;

/******************************************************************************
 *  Compilation:  javac Complex.java
 *  Execution:    java Complex
 *
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Complex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im instance variables after
 *  they've been initialized.
 *
 *  % java Complex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 ******************************************************************************/

public class Complex {
    private final double re;   // the real part
    private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(final double real, final double imag) {
        this.re = real;
        this.im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (this.im == 0) return this.re + "";
        if (this.re == 0) return this.im + "i";
        if (this.im < 0) return this.re + " - " + (-this.im) + "i";
        return this.re + " + " + this.im + "i";
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(this.re, this.im);
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
        return Math.atan2(this.im, this.re);
    }

    // return a new Complex object whose value is (this + b)
    public Complex plus(final Complex b) {
        final Complex a = this;             // invoking object
        final double real = a.re + b.re;
        final double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(final Complex b) {
        final Complex a = this;
        final double real = a.re - b.re;
        final double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(final Complex b) {
        final Complex a = this;
        final double real = a.re * b.re - a.im * b.im;
        final double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // return a new object whose value is (this * alpha)
    public Complex scale(final double alpha) {
        return new Complex(alpha * this.re, alpha * this.im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(this.re, -this.im);
    }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        final double scale = this.re * this.re + this.im * this.im;
        return new Complex(this.re / scale, -this.im / scale);
    }

    // return the real or imaginary part
    public double re() {
        return this.re;
    }

    public double im() {
        return this.im;
    }

    // return a / b
    public Complex divides(final Complex b) {
        final Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(this.re) * Math.cos(this.im), Math.exp(this.re) * Math.sin(this.im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(this.re) * Math.cosh(this.im), Math.cos(this.re) * Math.sinh(this.im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(this.re) * Math.cosh(this.im), -Math.sin(this.re) * Math.sinh(this.im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return this.sin().divides(this.cos());
    }


    // a static version of plus
    public static Complex plus(final Complex a, final Complex b) {
        final double real = a.re + b.re;
        final double imag = a.im + b.im;
        final Complex sum = new Complex(real, imag);
        return sum;
    }

    // See Section 3.3.
    public boolean equals(final Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        final Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(this.re);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.im);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // sample client for testing
    public static void main(final String[] args) {
        final Complex a = new Complex(5.0, 6.0);
        final Complex b = new Complex(-3.0, 4.0);

        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("Re(a)        = " + a.re());
        System.out.println("Im(a)        = " + a.im());
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
        System.out.println("a / b        = " + a.divides(b));
        System.out.println("(a / b) * b  = " + a.divides(b).times(b));
        System.out.println("conj(a)      = " + a.conjugate());
        System.out.println("|a|          = " + a.abs());
        System.out.println("tan(a)       = " + a.tan());
    }

}