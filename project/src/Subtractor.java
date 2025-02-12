public class Subtractor {
    private int subtractor;

    public Subtractor() {
        this.subtractor = 0;
    }

    public Subtractor(int subtractor) {
        this.subtractor = subtractor;
    }

    public int getSubtractor() {
        return subtractor;
    }

    public int sub(int b) {
        return this.subtractor -= b;
    }
}
