public class Divider {
    private int div;

    public int getDivider() {
        return this.div;
    }

    public Divider() {
        this.div = 1;
    }

    public Divider(int divider) {
        this.div = divider;
    }

    public double divide(int b) {
        return this.div /= (b * 1.0);
    }
}
