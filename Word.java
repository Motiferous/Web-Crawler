//This class contains the word and the number of times it was spotted.
public class Word {

    private int counter;//Variable for the amount of times the word was found.
    private final String word;//The word we are searching for.

    //Constructor.
    public Word(int counter, String word) {
        this.counter = counter;
        this.word = word;
    }

    //Get how many times was the word spotted.
    public int getCounter() {
        return counter;
    }

    //Set how many times was the word spotted.
    public void setCounter(int counter) {
        this.counter = counter;
    }

    //Set get the word we are searching for.
    public String getWord() {
        return word;
    }

}
