public class JavaApplication1 {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Calculator calc = new Calculator();
        System.out.println("2+2=" + calc.sum(2, 2));
        System.out.println("2-2-2=" + calc.sub(2, 2, 2));
        System.out.println("2*2=" + calc.mupltiply(2, 2));
        System.out.println("2/2=" + calc.divide(2, 2));
        System.out.println("9<<=" + calc.leftShift((byte) 9));
    }
}