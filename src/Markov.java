import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *author: Aaron Bourdeaux
 *date: 2023/03/11
 *description: A Markov text generator,
 *             a program which can generate new
 *             sentences based off of the structure
 *             of input text
 */

public class Markov {

    private static final String ENDS_IN_PUNCTUATION = "__$";
    private static final String PUNCTUATION_MARKS = ".!?$";

    private HashMap<String, ArrayList<String>> words;
    private String prevWord;

    /*
        Within the constructor, setup words and prevWord to prepare
        the program to create its first sentence.
     */
    public Markov() {
        this.words = new HashMap<String, ArrayList<String>>();
        this.words.put(this.ENDS_IN_PUNCTUATION, new ArrayList<String>());
        prevWord = this.ENDS_IN_PUNCTUATION;
    }

    /*
        Returns private words variable
     */
    HashMap<String, ArrayList<String>> getWords() {
        return words;
    }

    /*
        Populate words HashMap with text from specified filename
     */
    public void addFromFile(String filename) {
        File file = new File(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        }catch (IOException ex) {
            System.out.println("Failed to read " + filename);
        }
            String line = null;
        while (scanner != null && scanner.hasNext()) {
            line = scanner.nextLine();
            this.addLine(line);
        }
    }

    /*
        Split a line of text into words, feeding words one by one into addWord
        for further processing of text
     */
    void addLine (String line) {
        if (line.length() > 0) {
            String[] wordsInLine = line.trim().split("\\s+" );
            for (String word : wordsInLine) {
                addWord(word);
            }
        }
    }

    /*
        Adds a word to the words HashMap as appropriate
     */
    void addWord(String word) {
        /*
            If the previous words ended in punctuation, we
            store the current word within the ArrayList associated
            with the ENDS_IN_PUNCTUATION key
         */
        if (this.endsWithPunctuation(prevWord)) {
            this.words.get(ENDS_IN_PUNCTUATION).add(word);
        }
        /*
            Otherwise, store the current word within the ArrayList
            associated with the prevWord key
         */
        else {
            if (this.words.get(prevWord) == null) {
                this.words.put(prevWord, new ArrayList<String>());
            }
            this.words.get(prevWord).add(word);
        }
        this.prevWord = word;
    }

    /*
        Generates a sentence randomly, word by word, then returns the
        sentence once a word with punctuation is added
     */
    public String getSentence () {
        String currentWord = randomWord(ENDS_IN_PUNCTUATION);
        String sentence = currentWord;
        while (!endsWithPunctuation(currentWord)) {
            currentWord = randomWord(currentWord);
            sentence += " " + currentWord;
        }
        return sentence;
    }

    /*
        Returns a word randomly from the ArrayList
        associated with word
     */
    String randomWord(String word) {
        ArrayList<String> nextWords = words.get(word);
        Random random = new Random();
        int indexOfRandom = random.nextInt(nextWords.size());
        return nextWords.get(indexOfRandom);
    }

    /*
        Returns true if text ends with a punctuation mark that is
        included within PUNCTUATION_MARKS
     */
    public static Boolean endsWithPunctuation(String text) {
        String endOfString = "";
        try {
            endOfString = text.substring(text.length() - 1);

        }
        catch (Exception ex) {
            System.out.println("Error processing the word " + text);
        }
        return PUNCTUATION_MARKS.contains(endOfString);
    }

    /*
        Returns the string interpretation of words HashMap
     */
    public String toString () {
        return words.toString();
    }

}
