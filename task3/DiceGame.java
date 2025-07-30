package task3;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiceGame {
    private final List<Dice> diceList;
    private final Scanner scanner;
    private final Fair randomGenerator;
    private final Probability probabilityCalculator;
    private final ProbabilityTable probabilityTable;

    public DiceGame(List<Dice> diceList) {
        this.diceList = new ArrayList<>(diceList);
        this.scanner = new Scanner(System.in);
        this.randomGenerator = new Fair();
        this.probabilityCalculator = new Probability();
        this.probabilityTable = new ProbabilityTable(probabilityCalculator);
    }

    public void startGame() {
        try {
            System.out.println("\n\tNon-Transitive Dice Game");
            System.out.println("=====================================");

            showDice();

            boolean userFirst = determineFirstMove();

            Dice computerDice, userDice;

            if (userFirst) {
                userDice = selectDice(null, true);
                computerDice = selectDice(userDice, false);
            } else {
                computerDice = selectDice(null, false);
                userDice = selectDice(computerDice, true);
            }

            System.out.println("\nSelected dice:");
            System.out.println("Computer: " + computerDice);
            System.out.println("You: " + userDice);

            System.out.println("\nRolling dice...");
            int computerRoll = performRoll(computerDice, "Computer");
            int userRoll = performRoll(userDice, "You");

            determineWinner(userRoll, computerRoll);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Cryptographic error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private void showDice() {
        System.out.println("\nAvailable dices: ");
        for (int i = 0; i < diceList.size(); i++) {
            System.out.println((i + 1) + ": " + diceList.get(i));
        }
    }

    private boolean determineFirstMove() throws NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("\nDetermining who goes first...");
        Fair.FairRandomResult result = randomGenerator.generateFairNumber(0, 1);

        System.out.println("I've selected a random value (0 or 1). HMAC: " + result.hmac);
        System.out.println("Try to guess my selection:");
        System.out.println("0 - 0");
        System.out.println("1 - 1");
        System.out.println("? - Show help");
        System.out.println("X - Exit");

        int userGuess = getMenuSelection(1);

        System.out.println("My selection was: " + result.computerNumber);
        System.out.println("Secret key: " + result.secretKey);

        boolean userWon = (userGuess == result.computerNumber);
        System.out.println((userWon ? "You" : "I") + " will make the first move!");
        return userWon;
    }

    private Dice selectDice(Dice excludedDice, boolean isUserSelection) {
        List<Dice> availableDice = new ArrayList<>(diceList);

        if (excludedDice != null) {
            availableDice.removeIf(d -> d.equals(excludedDice));
        }

        int choice;
        if (isUserSelection) {
            System.out.println("\nAvailable dice:");
            for (int i = 0; i < availableDice.size(); i++) {
                System.out.println((i + 1) + " - " + availableDice.get(i));
            }
            System.out.println("? - Show probabilities");
            System.out.println("X - Exit");

            choice = getMenuSelection(availableDice.size()) - 1;
        } else {
            choice = new SecureRandom().nextInt(availableDice.size());
        }

        Dice selected = availableDice.get(choice);

        diceList.remove(selected);

        if (!isUserSelection) {
            System.out.println("Computer chose: " + selected);
        }

        return selected;
    }

    private int performRoll(Dice dice, String player) throws NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("\n" + player + "'s roll:");
        Fair.FairRandomResult result = randomGenerator.generateFairNumber(0, dice.getSize() - 1);

        System.out.println("HMAC: " + result.hmac);
        System.out.println("Select a number between 0 and " + (dice.getSize() - 1) + ":");

        int userNumber = getMenuSelection(dice.getSize() - 1);

        int finalIndex = Fair.calculateFinalResult(
                result.computerNumber,
                userNumber,
                dice.getSize());

        System.out.println("My number: " + result.computerNumber);
        System.out.println("Secret key: " + result.secretKey);
        System.out.println("Result index: " + finalIndex);

        int rollValue = dice.getValue(finalIndex);
        System.out.println(player + " rolled: " + rollValue);

        return rollValue;
    }

    private void determineWinner(int userRoll, int computerRoll) {
        System.out.println("\nFinal Results:");
        System.out.println("You rolled: " + userRoll);
        System.out.println("Computer rolled: " + computerRoll);

        if (userRoll > computerRoll) {
            System.out.println("You win! (" + userRoll + " > " + computerRoll + ")");
        } else if (computerRoll > userRoll) {
            System.out.println("Computer wins! (" + computerRoll + " > " + userRoll + ")");
        } else {
            System.out.println("It's a tie! (" + userRoll + " = " + computerRoll + ")");
        }
    }

    private int getMenuSelection(int maxOption) {
        while (true) {
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("X")) {
                System.out.println("Exiting game...");
                System.exit(0);
            } else if (input.equals("?")) {
                probabilityTable.printProbabilityTable(diceList);
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= maxOption) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and " + maxOption);
            }
        }
    }
}