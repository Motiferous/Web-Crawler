import java.net.URL;
import java.util.ArrayList;

public class Seed {
    private ArrayList<URL> urls = new ArrayList<>();
    private ArrayList<String> words = new ArrayList<>();

    public void setUrlsList(ArrayList<URL> urls) {
        this.urls = urls;
    }

    public ArrayList<URL> getUrls() {
        return urls;
    }

    public void setUrls(URL urls) {
        this.urls.add(urls);
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(String word) {
        this.words.add(word);
    }

    public void setWordsList(ArrayList<String> words) {
        this.words = words;
    }
}
