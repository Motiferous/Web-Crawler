import java.util.ArrayList;

//This class connects classes CustomUrl and Link.
public class Link {
    private ArrayList<CustomUrl> urls = new ArrayList<>();//Array of urls we are crawling trough.
    private ArrayList<Word> words = new ArrayList<>();//Array of words we are searching for.
    private int totalCount = 0;//Sum of every word.

    //Function returns the sum of every word.
    public int getTotalCount() {
        return totalCount;
    }

    //Function returns an array of the URLs we are crawling trough.
    public ArrayList<CustomUrl> getUrls() {
        return this.urls;
    }

    //Function adds additional URL to the array.
    public void setUrls(CustomUrl urls) {
        this.urls.add(urls);
    }

    //Function returns the array of words we are searching for.
    public ArrayList<Word> getWords() {
        return words;
    }

    //Function searches if the current URLs contain the new URL.
    public boolean has(String url) {
        for (CustomUrl s : urls) {
            if (s.getUrl().equals(url)) return true;
        }
        return false;
    }

    // Function sums of the numbers of every word into one sum.
    public void SumUp() {
        for (Word word : words) {
            totalCount += word.getCounter();
        }
    }
}


