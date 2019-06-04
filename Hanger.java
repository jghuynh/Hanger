import java.io.BufferedReader;
import java.util.HashSet;

//import com.sun.tools.javac.util.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

public class Hanger {

    private HashSet<String> bag = new HashSet<String>();
    private HashSet<Character> falseLetters = new HashSet<Character>();
    private String answer;
    private String displayAnswer;
    private int numFalseTries = 17;

    private Hanger() {
        answer = "";
        displayAnswer = "";
    }

    /**
     * Generates a random answer for the Hanger game
     */
    private void generateAnswer() {
        Iterator<String> bagIterator = bag.iterator();
        int index = 0;
        int targetIndex = (int) (bag.size() * Math.random());
        while (bagIterator.hasNext()) {

            // gets a random Pokemon name from the bag
            if (index == targetIndex) {
                answer = bagIterator.next();
                // creating the display Answer
                for (int aindex = 0; aindex < answer.length(); aindex ++) {
//                    displayAnswer.concat("_");
                    displayAnswer = displayAnswer + "_ ";

                }
                break;
            }
            bagIterator.next();
            index ++;
        }
    }


    /**
     * Checks if the guess is correct, then prints out the display answer
     * @param letter the user input guess
     */
    private void displayText(char letter) {
        letter = Character.toUpperCase(letter);
        // check if letter is an actual letter
        if (letter < 'A' || letter > 'Z') {
            return;
        }

        // if letter is legal
        if (falseLetters.contains(letter)) {
            System.out.println("You already chose that letter!");
        }
        else
        {
            char answerLetters[] = this.answer.toCharArray();
            char displayLetters[] = this.displayAnswer.toCharArray();
            boolean isCorrect = false;
            for (int index = 0; index < answerLetters.length; index++) {
                if (answerLetters[index] == letter) {
                    displayLetters[index*2] = letter;
                    isCorrect = true;
                }
            }
            displayAnswer = new String(displayLetters);
            // if guess a wrong character
            if (!isCorrect) {
                numFalseTries--;
                System.out.println("Awww");
                falseLetters.add(letter);
            }
            else {
                System.out.println("Yay!");
            }
        }
    }

    private void readData(Path inputPath) {
        try  {
            BufferedReader reader = Files.newBufferedReader(inputPath);
            String line = reader.readLine();

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
//                    .withHeader("Number", "Name", "Type_1", "Type_2", "Total", "HP", "Attack", "Defense",
//                            "Sp_Atk", "Sp_Def", "Speed", "Generation", "isLegendary", "Color",
//                            "hasGender", "Pr_Male", "Egg_Group_1", "Egg_Group_2", "hasMegaEvolution",
//                            "Height_m", "Weight_kg", "Catch_Rate", "Body_Style")
                    .withHeader("Number", "Name")
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("Name");
                name = name.toUpperCase();
                bag.add(name);
            }
        }
        catch (IOException e) {
            System.out.println("An IO Exception");
        }
        this.generateAnswer();
    }

    /**
     * Gets the number of unique false tries user has made
     * @return the number of false guesses
     */
    private int getNumFalseTries() {
        return this.numFalseTries;
    }

    /**
     * Gets the answer to the Hanger game
     * @return the answer
     */
    private String getAnswer() {
        return this.answer;
    }

    /**
     * Gets the display answer, or the work-in progress
     * @return the display answer
     */
    private String getDisplayAnswer() {
        return this.displayAnswer;
    }

    /**
     * Determines if the User has correctly solved the game,
     * ie. if the display answer equals the answer
     * @return true if the user has solved the game; false otherwise
     */
    private boolean ifSolved() {

        //create a character array of displayAnswer and answer
        char[] charAnswer = this.answer.toCharArray();
        char[] charDisplayAnswer = this.displayAnswer.toCharArray();
        for (int index = 0; index < answer.length(); index ++){
            // if answer is not yet solved
            if (charDisplayAnswer[index*2] != charAnswer[index]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints the number of false tries left, which letters that have been
     * incorrectly guessed, and the display answer
     */
    private void printDisplay() {
        System.out.println("Number Of False Tries Left: " + numFalseTries);
        System.out.println(displayAnswer);
        System.out.print("False Tries: ");
        for (char characters: falseLetters) {
            System.out.print(characters + " ");
        }
    }

    public static void main(String[] args) {
//        Paths myPath = new Paths.get();
        Hanger myHanger = new Hanger();
        myHanger.readData(Paths.get("src\\pokemon\\pokemon_alopez247.csv"));

        Scanner myScanner = new Scanner(System.in);
        while(myHanger.getNumFalseTries() > 0 && !myHanger.ifSolved()) {
            myHanger.printDisplay();
            System.out.print("\nGuess a letter: ");
            String StringGuess = myScanner.nextLine();
            char guess = StringGuess.charAt(0);
            myHanger.displayText(guess);
        }
        // if out of false tries, print answer
        myHanger.printDisplay();
        System.out.println("\nAnswer: " + myHanger.getAnswer());
    }

}
