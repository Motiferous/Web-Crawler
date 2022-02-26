import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        String data = new String();
        ArrayList<Seed> everyInput = new ArrayList<Seed>();

        try {
            GetInput(everyInput);
        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }
        //System.out.println(everyInput.get(0).getWords());

        try {
            GetResults(everyInput);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public static void GetInput(ArrayList<Seed> array) throws FileNotFoundException, MalformedURLException {
        File input = new File("/home/kristupasj/Documents/src/input.txt");
        Scanner myReader = new Scanner(input);
        int urlcounter = 0;
        int wordcounter = 0;
        URL connection = null;


        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            ArrayList<String> seperatedWords = new ArrayList<>();
            ArrayList<String> seperatedUrl = new ArrayList<>();
            Seed anotherone = new Seed();

            seperatedUrl.addAll(List.of(data.split("\\s*,\\s*")));

            for (String s : seperatedUrl){
                URLartificial temp = new URLartificial(1, s);
                anotherone.getUrls().add(temp);
            }
            data = myReader.nextLine();
            seperatedWords.addAll(List.of(data.split("\\s*,\\s*")));
            for (String s : seperatedWords){
                SearchWord tempW = new SearchWord(0, s);
                anotherone.getWords().add(tempW);
            }




            array.add(anotherone);







        }


    }
    public static void GetResults(ArrayList<Seed> input) throws IOException {
        for (int i = 0; i < input.size() ; i++){
            for(int i1 = 0; i1 < input.get(i).getUrls().size() && i1 < 10000 && input.get(i).getUrls().get(i1).getDeepness() <= 8; i1++){
                System.out.println(i1);
                Document document;
                Document text;
                //System.out.println(input.get(i).getUrls().get(i1).getDeepness() + " " + input.get(i).getUrls().get(i1).getUrl());
                try {
                    document = Jsoup.connect(input.get(i).getUrls().get(i1).getUrl()).get();
                }
                catch(Exception e){
                    continue;
                }
                        text = Jsoup.parse(String.valueOf(document));
                Matches(text, input, i);
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    if(!input.get(i).has(String.valueOf(link.attr("abs:href"))) && input.get(i).getUrls().size() < 10000) {

                        URLartificial anothtemp = new URLartificial(input.get(i).getUrls().get(i1).getDeepness() + 1, String.valueOf(link.attr("abs:href")));
                        input.get(i).setUrls(anothtemp);

                    }
                    //System.out.println(anothtemp.getUrl());
                }
               // System.out.println(input.get(i).getUrls().size() );

            }
        }

    }

    private static void Matches(Document text, ArrayList<Seed> input, int index) {
        for (SearchWord s : input.get(index).getWords()){
            s.setCounter(s.getCounter() + text.text().split(s.getWord(), -1).length-1);
        }
    }


}
