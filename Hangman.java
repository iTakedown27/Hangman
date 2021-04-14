import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Hangman implements KeyListener {
    private ArrayList<Word> availableWords = new ArrayList<Word>();
    private ArrayList<User> users = new ArrayList<User>();
    private int tries = 0;
    int x = 0, y = 0;
    private String line;
    private ArrayList<Word> words = new ArrayList<Word>();
    private ArrayList<Integer> indices = new ArrayList<Integer>();
    private String word = "";
    private boolean isAnswered = false; // In development
    private String newWord = "";
    private int counter = 0;
    Scanner scanner = new Scanner(System.in);

    public void generateAvailableWords() {
        if (words.isEmpty()) {
            System.out.println("There are no available words.");
        } else {
            for (int i = 0; i < words.size(); i++) {
                availableWords.add(words.get(i));
            } // Only call this if words is not empty
        }
    }

    public void generateRandomWord() {
        int index = (int) (Math.random()*availableWords.size());
        word = availableWords.get(index).getWord();

    }

    public void addWord() {
        System.out.println("Type in a word to add:");
        String w = scanner.next();
        System.out.println("Type in a simple description that does not give it away:");
        String d = scanner.next();
        d = scanner.nextLine();
        System.out.println("Type in a fun fact about it:");
        String f = scanner.next();
        f = scanner.nextLine();

        if (checkDuplicate(new Word(w, d, f))) {
            System.out.println("This word already exists.");
        }
        else {
            words.add(new Word(w, d, f));
            System.out.println("Added to words list!");
        }
    }

    public boolean checkDuplicate(Word w) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(w)) {
                return true;
            }
        }
        return false;
    }

    public void removeWord() {
        boolean isRemoved = false;
        System.out.println("Type in the word you would like to remove:");
        String w = scanner.nextLine();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWord().equals(w)) {
                words.remove(i);
                isRemoved = true;
                System.out.println(w + " has been removed from the words list!");
                i--;
            }
        }
        if (!isRemoved) {
            System.out.println(w + " was not in the words list.");
        }
    }

    public void revealAnswer(String word) {
        System.out.println("The word was " + word + "!\n");
    } 

    public void checkNumChars(String word) {
        line = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.substring(i, i+1).equals(" ")) {
                line += " ";
            }
            else {
                line += "_";
            }
        }
        System.out.println(line);
    }

    public void setGuess() {
        System.out.println("Commands:");
        System.out.println("1 - Quits the game");
        System.out.println("2 - Gives a hint");
        System.out.println("3 - Reveals the answer");
        System.out.println("Guess a letter of the word: ");
        checkNumChars(word);
        String letter = scanner.next();
        if (letter.equalsIgnoreCase("1")) {
            System.out.println("Game Over!");
        } // In development
        if (letter.equalsIgnoreCase("2")) {
            giveHint(word);
            setGuess();
        }
        if (letter.equalsIgnoreCase("3")) {
            revealAnswer(word);
            giveFunFact(word);
            endMenu();
        }
        for (int index = 0; index < word.length(); index++) {
            if (letter.equalsIgnoreCase(word.substring(index, index + 1))) {
                System.out.print(index + " "); // Test
                indices.add(counter, index);
                counter++;
            }
        }

        for (int i = 0; i < indices.size(); i++) {
            System.out.println("Index check");
            System.out.println(indices.get(i));
        }
        
        for (int i = 0; i < indices.size()-1; i++) {
            newWord += word.substring(indices.get(i), indices.get(i+1));
        }
        System.out.println(newWord);
        setGuess();
    }

    public void resetHighScores() {
        for (int i = 0; i < users.size(); i++) {
            users.remove(i);
        }
        if (users.isEmpty()) {
            System.out.println("High scores list has been reset!");
        }
    }

    public void startGame() {
        System.out.println("Would you like to put your own words or use existing ones?");
        System.out.println("1. Your own words");
        System.out.println("2. Use existing ones");
        System.out.println("3. Exit to main menu");
        int s = scanner.nextInt();
        switch(s) {
            case 1:
                System.out.println("How many words would you like to add?");
                int num = scanner.nextInt();
                for (int i = 0; i < num; i++) {
                    addWord();
                }
                generateAvailableWords();
                break;
            
            case 2:
                addDefaultWords();
                generateAvailableWords();
                generateRandomWord();
                break;

            case 3:
                showMenu();

            default:
                System.out.println("Invalid option. Please enter again.");
                startGame();
        }
        generateRandomWord();
        setGuess();
    }

    public void addDefaultWords() {
        words.add(new Word("Catastrophe", "That's a lot of damage!", "COVID-19 is Catastrophic!"));
        words.add(new Word("Bespectacled", "An adjective that describes a useful tool that is carried on a person.", "If you have glasses, you are bespectacled!"));
        words.add(new Word("AP Computer Science A", "A class that emphasizes on algorithms", "AP Computer Science A is one of the easiest APs in all of College Board. If you like computers and are good at math, take it when you can!"));
        words.add(new Word("AP Calculus BC", "The highest level of math you can reach in high school.", "The next level of math after AP Calculus BC is Multivariable Calculus!"));
        words.add(new Word("Prospect High School", "A school in Saratoga, California that has colors gold and blue.", "PHS has a very diverse community!"));
    }

    public void giveHint(String word) {
        System.out.print("Hint:");
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWord().equals(word)) {
                System.out.println(words.get(i).getHint());
            }
        }
    }

    public void giveFunFact(String word) {
        System.out.print("Fun fact: ");
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWord().equals(word)) {
                System.out.println(words.get(i).getFunFact());
            }
        }
    }

    public void printUserList() {
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }
        if (users.isEmpty()) {
            System.out.println("Either no one has played the game yet or high scores have been cleared.\n");
        }
        System.out.println("Would you like to:");
        System.out.println("1. Return to Main Menu");
        System.out.println("2. Reset high scores");
        int choice = scanner.nextInt();
        if (choice == 1) {
            showMenu();
        }
        else if (choice == 2) {
            resetHighScores();
            printUserList();
        }
        else {
            System.out.println("Invalid option. Please enter again.");
            printUserList();
        }
    }

    public void aboutMe() {
        System.out.println("Hi! I am Clement Boiteux. I am currently a sophomore in high school. My most favorite subject is math, but I also enjoy any subject that uses a lot of math. I plan to pursue engineering, programming, or finance/economics in the future.");
        System.out.println("Programming Experience: I took AP Computer Science A in my sophomore year of high school and joined Programming Club. I participated in two hackathons so far and programmed in HTML/CSS for both. I actively program in Java, C++, HTML/CSS, and JavaScript. I have a little bit of experience with Python but didn't learn it in depth.\n");
        redirect();
    }

    public void showCredits() {
        System.out.println("Developed by: Clement Boiteux in 2021");
        System.out.println("Language: Java");
        System.out.println("Inspired by Hangman");
        redirect();
    }

    public void tutorial() {
        System.out.println("Hangman is a game where a player comes up with a secret word or phrase and another player has to guess it.");
        System.out.println("The guessing player can only guess one letter at a time, and if the word/phrase contains the letter, the correctly guessed letters are filled in, thus revealing the word more.");
        System.out.println("However, if the player guesses a letter wrong, a part of the hangman is drawn. The more it is drawn, the less chances a player has. Wrong letters are noted down.");
        System.out.println("If the player has lost all their chances and guessed a letter wrong, they lose and thus the word is revealed.");
        System.out.println("However, if a player believes there are enough letters in the word/phrase that can be guessed, they can go for it, but a wrong guess will not cost the player anything.");
        redirect();
    }

    public void redirect() {
        System.out.println("Type and enter any key to return main menu.");
        scanner.next();
        showMenu();
    }

    public void showMenu() {

        System.out.println("Welcome to Clement's Hangman Game!");
        System.out.println("1. New Game              _____"); // In development
        System.out.println("2. Credits               |   |");
        System.out.println("3. How to Play           o   |");
        System.out.println("4. About the developer  ---  |");
        System.out.println("5. High scores          | |  |");
        System.out.println("6. Exit");
        int choice = scanner.nextInt();

        if (choice == 1) {
            startGame();
        }
        else if (choice == 2) {
            showCredits();
        }
        else if (choice == 3) {
            tutorial();
        }
        else if (choice == 4) {
            aboutMe();
        }
        else if (choice == 5) {
            printUserList();
        }
        else if (choice == 6) {
            System.out.println("Have a nice day! Thank you for playing!");
        }
        else {
            System.out.println("Invalid option. Enter again.");
            showMenu();
        }
    }


    public void endMenu() {
        System.out.println("Good job! You have discovered the secret word! Would you like to: ");
        System.out.println("1. Be featured on the high scores list");
        System.out.println("2. Play again");
        System.out.println("3. Quit game");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("First Name:");
            String name = scanner.next();
            System.out.println("Date completed ([month]/[day]/[year] format)");
            String date = scanner.next();
            users.add(new User(name, date, tries));
        }
        else if (choice == 2) {
            startGame();
        }
        else if (choice == 3) {
            System.out.println("Have a nice day! Thank you for playing!");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    public static void main(String[] args) {
        Hangman h = new Hangman();
        GUI g = new GUI();
        h.showMenu();
    }
}
