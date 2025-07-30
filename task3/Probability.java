package task3;

public class Probability {
    public double calculateWinProbability(Dice a, Dice b) {
        int wins = 0;
        int total = a.getSize() * b.getSize();

        for (int i = 0; i < a.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                if (a.getValue(i) > b.getValue(j)) {
                    wins++;
                }
            }
        }

        return (double) wins / total;
    }
}