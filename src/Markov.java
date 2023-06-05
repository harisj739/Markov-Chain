import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Name: Haris Jilani
 * Date: March 30, 2023
 * Title: Markov.java
 * Abstract: Randomly generates a sentence based on a
 * given set of words from a file. The program reads
 * each line, splits it into words, and uses a
 * hashmap to track the previous and current word.
 * When it hits a punctuation, it recognizes that
 * the sentence has ended.
 */

public class Markov {

    private static final String ENDS_IN_PUNCTUATION = "__$";
    private static final String PUNCTUATION_MARKS = ".!?$";
    private String prevWord;
    private HashMap<String, ArrayList<String>> words;

    public static void main(String[] args) {
        Markov markov = new Markov();

        markov.addFromFile("hamlet.txt");
        System.out.println(markov);

        for (int i = 0; i < 10; i ++){
            System.out.println(markov.getSentence());
        }
    }


    public Markov() {
        words = new HashMap<String, ArrayList<String>>();
        this.words.put(ENDS_IN_PUNCTUATION, new ArrayList<String>());
        this.prevWord = ENDS_IN_PUNCTUATION;
    }

    public static boolean endsWithPunctuation(String punctuation) {
        try {
            for(int i = 0; i < PUNCTUATION_MARKS.length(); i++) {
                if(punctuation.charAt(punctuation.length() - 1) == PUNCTUATION_MARKS.charAt(i)) {
                    return true;
                } //if
            } //for
        } catch (IndexOutOfBoundsException e) {
            System.out.println(punctuation + " has caused this error: " + e);
        } //try-catch
        return false;
    }

    /** In this method, I corrected my previous
     * logic that was resulting in incorrect
     * hashmap values for my hamlet.txt file.
     */
    void addWord (String str) {
        boolean endsInPunc = endsWithPunctuation(str);
        //The following is the NEW code added:
        if (str.length() != 0) {
            if (!words.containsKey(prevWord)) {
                words.put(prevWord, new ArrayList<String>());
            } //if
            words.get(prevWord).add(str);
            if (endsInPunc) {
                prevWord = ENDS_IN_PUNCTUATION;
            } else {
                prevWord = str;
            } //if-else
        } //if
        //The following is the ORIGINAL code:
 /** boolean prevWordEndsInPunc = endsWithPunctuation(prevWord);
     boolean strEndsInPunc = endsWithPunctuation(str);

        if (prevWordEndsInPunc) {
            if (strEndsInPunc && words.get(ENDS_IN_PUNCTUATION).size() >= 1) {
                //if there's 1 element in ENDS_IN_PUNCTUATION
                for (String s : words.get(ENDS_IN_PUNCTUATION)) {
                    words.put(s, currWord);
                }
            } else {
                words.get(ENDS_IN_PUNCTUATION).add(str);
            } //if-else
        } else if (!prevWordEndsInPunc) {
            if (words.containsKey(prevWord)) {
                words.get(prevWord).add(str);
            } else if (!words.containsKey(prevWord)) {
                words.put(prevWord, currWord);
            } //if-else if
        } //if-else if */
    } //addWord(String)

    /**
     * In this method I removed the lines that set
     * the previous word to the current word, and
     * when the line ended, would reset to
     * ENDS_IN_PUNCTUATION. Instead, I moved those
     * two lines to the addWord(String) method.
     */
    void addLine (String str) {
        try {
            if (str.length() != 0) {
                str = str.replaceAll("(?:\\t|\\n)", "");
                str = str.replaceAll(" +", " ").trim();
                String[] currWord = str.split(" ");
                for (String s : currWord) {
                    addWord(s);
//                  prevWord = s; REMOVED THIS LINE
                } //for
//                prevWord = ENDS_IN_PUNCTUATION; REMOVED THIS LINE TOO
            } //if
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    } //addLine()

    String randomWord (String word) {
        String str = "";
        if (words.get(word) != null) {
            if (words.get(word).size() == 1) {
                str = words.get(word).get(0);
            } else if (words.get(word).size() > 1) {
                Random rand = new Random();
                int randomIndex = rand.nextInt(0, words.get(word).size());
                str = words.get(word).get(randomIndex);
            }
        } //if
        return str;
    } //randomWord(String)

    public void addFromFile (String filename) {
        File f = new File(filename);
        Scanner scan = null;
        String fileContent = "";
        try {
            scan = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } //try-catch
        while (scan != null && scan.hasNext()) {
            String currLine = scan.nextLine();
            addLine(currLine);
        } //while
    } //addFromFile()

    public HashMap<String, ArrayList<String>> getWords() {
        return words;
    } //getWords()

    @Override
    public String toString() {
        return words.toString();
    }

    public String getSentence() {
        String currentWord = randomWord(ENDS_IN_PUNCTUATION);       //Gets a random word from the key (I, Will, or I)
        String sentence = "";
        boolean endsInPunc = endsWithPunctuation(currentWord);
        while (!endsInPunc) {
            sentence += currentWord + " ";
            currentWord = randomWord(currentWord);
            endsInPunc = endsWithPunctuation(currentWord);
        } //while
        sentence += currentWord;
        return sentence;
    }
}
