package task3;

import java.util.*;

public class DiceParser {
    public List<Dice> parseDices(String[] args) {

        List<Dice> dices = new ArrayList<>();

        if (args.length < 3) {
            throw new IllegalArgumentException(
                    "At least 3 dice are required. Example: 1,2,3,4,5,6 2,2,4,4,6,6 3,3,3,3,3,3");
        }

        for (String arg : args) {
            String[] numStrs = arg.split(",");
            int[] values = new int[numStrs.length];

            for (int i = 0; i < numStrs.length; i++) {
                values[i] = Integer.parseInt(numStrs[i]);
                if (values[i] < 0 || values[i] > 9) {
                    throw new IllegalArgumentException("Dice values must be between 1 and 6: " + values[i]);
                }
            }

            Dice dice = new Dice(values);
            dices.add(dice);
        }
        return dices;
    }
}
