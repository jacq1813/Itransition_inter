package task3;

import java.util.List;

public class DiceMain {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: At least 3 dice are required.");
            System.out.println("Usage: java -jar dicegame.jar <dice1> <dice2> <dice3> [dice4...]");
            System.out.println("Example: java -jar dicegame.jar 2,2,4,4,9,9 6,8,1,1,8,6 7,5,3,7,5,3");
            System.exit(1);
        }

        try {
            DiceParser parser = new DiceParser();
            List<Dice> diceList = parser.parseDices(args);

            DiceGame game = new DiceGame(diceList);
            game.startGame();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}