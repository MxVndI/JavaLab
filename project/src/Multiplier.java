public class Multiplier {
    private int multiplier;

    public int getMultiplier() {
        return multiplier;
    }

    public Multiplier() {
        this.multiplier = 1;
    }

    public Multiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int multiply(int b) {
        return this.multiplier *= b;
    }
}
