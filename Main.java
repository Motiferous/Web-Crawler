import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        ArrayList<Seed> everyInput = new ArrayList<>();

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

        Output(everyInput);


    }

    public static void Output(ArrayList<Seed> everyInput) {
        try {
            File output = new File("output.txt");
            output.createNewFile();
            PrintWriter writer = new PrintWriter(output);



            for (Seed s : everyInput){
                    for(URLartificial u : s.getUrls()){
                        if(u.getDeepness() == 1) {
                            writer.print(u.getUrl() + ",");
                        }
                        else break;
                    }
                    for (SearchWord w : s.getWords()){
                        if (w.equals(s.getWords().get(s.getWords().size() - 1))){
                            writer.print(w.getCounter());
                        }
                        else  writer.print(w.getCounter() + ",");
                    }
                System.out.println("");
                }
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void GetInput(ArrayList<Seed> array) throws FileNotFoundException, MalformedURLException {
        File input = new File("input.txt");
        Scanner myReader = new Scanner(input);


        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Seed anotherone = new Seed();

            ArrayList<String> seperatedUrl = new ArrayList<>(List.of(data.split("\\s*,\\s*")));

            for (String s : seperatedUrl) {
                URLartificial temp = new URLartificial(1, s);
                anotherone.getUrls().add(temp);
            }
            data = myReader.nextLine();
            ArrayList<String> seperatedWords = new ArrayList<>(List.of(data.split("\\s*,\\s*")));
            for (String s : seperatedWords) {
                SearchWord tempW = new SearchWord(0, s);
                anotherone.getWords().add(tempW);
            }
            array.add(anotherone);


        }


    }

    public static void GetResults(ArrayList<Seed> input) throws IOException {
        for (int i = 0; i < input.size(); i++) {
            for (int i1 = 0; i1 < input.get(i).getUrls().size() && i1 < 100 && input.get(i).getUrls().get(i1).getDeepness() <= 8; i1++) {
                System.out.println(i1);
                Document document;
                Document text;
                System.out.println(input.get(i).getUrls().get(i1).getDeepness() + " " + input.get(i).getUrls().get(i1).getUrl());

                try {
                    document = Jsoup.connect(input.get(i).getUrls().get(i1).getUrl()).get();
                } catch (Exception e) {
                    continue;
                }
                text = Jsoup.parse(String.valueOf(document));
                Matches(text, input, i);
                if(input.get(i).getUrls().size() < 10000) {
                    Elements links = document.select("a[href]");
                    for (Element link : links) {
                        if (!input.get(i).has(link.attr("abs:href"))) {

                            URLartificial anothtemp = new URLartificial(input.get(i).getUrls().get(i1).getDeepness() + 1, link.attr("abs:href"));
                            input.get(i).setUrls(anothtemp);

                        }
                        //System.out.println(anothtemp.getUrl());
                    }
                }
                // System.out.println(input.get(i).getUrls().size() );

            }
        }

    }

    public static void Matches(Document text, ArrayList<Seed> input, int index) {
        for (SearchWord s : input.get(index).getWords()) {
            s.setCounter(s.getCounter() + StringUtils.countMatches(text.text(), s.getWord()));

        }
       // System.out.println(text.text());
      //  System.out.println("---------------------------------------------");


        //System.out.println(        input.get(index).getWords().get(0).getCounter());

        //System.out.println(input.get(index).getWords().get(3).getCounter());
    }


}
