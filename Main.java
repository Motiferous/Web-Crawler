import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println(everyInput.get(0).getUrls());


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
            ArrayList<URL> seperatedURL = new ArrayList<>();

            Seed anotherone = new Seed();
            array.add(anotherone);
            try {
                connection = new URL(data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            seperatedWords.addAll(List.of(data.split("\\s*,\\s*")));
            for (String s : seperatedWords){
                URL temp = new URL(s);
                seperatedURL.add(temp);
            }

            array.get(array.size() - 1).setUrlsList(seperatedURL);
            data = myReader.nextLine();
            seperatedWords.addAll(List.of(data.split("\\s*,\\s*")));
            array.get(array.size() - 1).setWordsList(seperatedWords);

        }


    }


}
