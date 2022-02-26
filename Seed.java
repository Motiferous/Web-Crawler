import java.util.ArrayList;

public class Seed {
    private ArrayList<URLartificial> urls = new ArrayList<>();

    public int getTotalCount() {
        return totalCount;
    }

    private int totalCount = 0;



    private ArrayList<SearchWord> words = new ArrayList<>();

    public void setUrlsList(ArrayList<URLartificial> urls) {
        this.urls = urls;
    }

    public ArrayList<URLartificial> getUrls() {
        return this.urls;
    }

    public void setUrls(URLartificial urls) {
        this.urls.add(urls);
    }

    public ArrayList<SearchWord> getWords() {
        return words;
    }

    public void setWords(String word) {
        SearchWord temp = null;
        temp.setWord(word);
        this.words.add(temp);
    }

    public void setWordsList(ArrayList<SearchWord> words) {
        this.words = words;
    }

    public boolean has(String url){
        for (URLartificial s : urls){
            //System.out.println(s.getDeepness() + " " + s.getUrl());
            if(s.getUrl().equals(url)) return true;
        }
        //System.out.println("-----------------");
        return false;

    }
    public void SumUp(){
        for(SearchWord word : words){
            totalCount += word.getCounter();
        }
    }

}


