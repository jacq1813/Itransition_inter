package task3;

import java.util.List;

public class ProbabilityTable {
    private final Probability calculator;

    public ProbabilityTable(Probability calculator) {
        this.calculator = calculator;
    }

    public void printProbabilityTable(List<Dice> diceList) {
        System.out.println("\nProbability of winning for each dice pair: ");
        System.out.println("(Row dice vs Column dice)");

        // Print header
        System.out.print("+------------+");
        for (int i = 0; i < diceList.size(); i++) {
            System.out.print("------------+");
        }
        System.out.println();

        System.out.printf("| %-10s |", "Dice \\ vs");
        for (int i = 0; i < diceList.size(); i++) {
            System.out.printf(" Dice %-5d |", i + 1);
        }
        System.out.println();

        System.out.print("+------------+");
        for (int i = 0; i < diceList.size(); i++) {
            System.out.print("------------+");
        }
        System.out.println();

        // Print table body
        for (int i = 0; i < diceList.size(); i++) {
            System.out.printf("| Dice %-5d |", i + 1);

            for (int j = 0; j < diceList.size(); j++) {
                if (i == j) {
                    System.out.print("    -      |");
                } else {
                    double prob = calculator.calculateWinProbability(diceList.get(i), diceList.get(j));
                    System.out.printf(" %8.4f  |", prob);
                }
            }
            System.out.println();

            System.out.print("+------------+");
            for (int k = 0; k < diceList.size(); k++) {
                System.out.print("------------+");
            }
            System.out.println();
        }
    }
}