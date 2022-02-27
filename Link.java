import java.util.ArrayList;

public class Link {
    private ArrayList<CustomUrl> urls = new ArrayList<>();

    public int getTotalCount() {
        return totalCount;
    }

    private int totalCount = 0;



    private ArrayList<Word> words = new ArrayList<>();

    public ArrayList<CustomUrl> getUrls() {
        return this.urls;
    }

    public void setUrls(CustomUrl urls) {
        this.urls.add(urls);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public boolean has(String url){
        for (CustomUrl s : urls){
            //System.out.println(s.getDeepness() + " " + s.getUrl());
            if(s.getUrl().equals(url)) return true;
        }
        //System.out.println("-----------------");
        return false;

    }
    public void SumUp(){
        for(Word word : words){
            totalCount += word.getCounter();
        }
    }

}


