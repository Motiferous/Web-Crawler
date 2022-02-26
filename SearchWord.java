public class SearchWord {


    private int counter = 0;
    private String word;

    public SearchWord(int counter, String word) {
        this.counter = counter;
        this.word = word;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
