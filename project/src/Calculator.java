public class Calculator {
    public int sum(int... a) {
        Adder adder = new Adder();
        for (int i : a) {
            adder.add(i);
        }
        return adder.getSum();
    }

    public int sub(int... a) {
        Subtractor subtractor = new Subtractor(a[0]);
        for (int i = 1; i < a.length; i++) {
            subtractor.sub(a[i]);
        }
        return subtractor.getSubtractor();
    }

    public int mupltiply(int... a) {
        Multiplier multiplier = new Multiplier();
        for (int i : a) {
            multiplier.multiply(i);
        }
        return multiplier.getMultiplier();
    }

    public double divide(int... a) {
        Divider divider = new Divider(a[0]);
        for (int i = 1; i < a.length; i++) {
            divider.divide(a[i]);
        }
        return divider.getDivider();
    }

    public byte leftShift(byte a) {
        LeftShifter leftShifter = new LeftShifter();
        return leftShifter.leftShift(a);
    }
}