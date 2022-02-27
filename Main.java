import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    static HashMap<String, String> config = new HashMap<String, String>();

    public static void main(String[] args) throws URISyntaxException {


        ArrayList<Seed> everyInput = new ArrayList<>();

        GetConfig();


        try {
            GetInput(everyInput);
        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            GetResults(everyInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Output(everyInput);


    }

    private static void GetConfig() throws URISyntaxException {

        File input = MakeDir("config.txt");
        System.out.printf(input.getPath());

        Scanner myReader = null;
        try {
            myReader = new Scanner(input);
        } catch (FileNotFoundException e) {
            input = new File("config.txt");
            try {
                myReader = new Scanner(input);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }


        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            String[] dataParts = data.split("=");
            config.put(dataParts[0], dataParts[1]);
        }
    }

    public static void Output(ArrayList<Seed> everyInput) {
        final String FILE_OUTPUT = config.get("OUTPUT_NAME/PATH");
        final String SEPARATOR = config.get("OUTPUT_SEPARATED_BY");
        final String REPORT_FORM = config.get("REPORT_FORM");

        if (REPORT_FORM.equals("top_10")){
            Sort(everyInput);
        }

        try {
            File output = MakeDir(FILE_OUTPUT);
            output.createNewFile();
            PrintWriter writer = new PrintWriter(output);


            for (Seed s : everyInput) {
                for (URLartificial u : s.getUrls()) {
                    if (u.getDeepness() == 1) {
                        writer.print(u.getUrl() + SEPARATOR);
                    } else break;
                }
                for (SearchWord w : s.getWords()) {
                    writer.print(w.getCounter() + SEPARATOR);
                }
                writer.println(s.getTotalCount());
            }
            writer.close();



        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private static void Sort(ArrayList<Seed> everyInput) {
        for (int i = 0; i < everyInput.size() - 1; i++) {
            int min = i;

            for (int i1 = i + 1; i1 < everyInput.size(); i1++){
                if(everyInput.get(i).getTotalCount() < everyInput.get(i1).getTotalCount())min = i1;
            }
            Collections.swap(everyInput, i, min);


        }
    }

    public static void GetInput(ArrayList<Seed> array) throws FileNotFoundException, MalformedURLException {
        final String FILE_INPUT = config.get("INPUT_NAME/PATH");
        final String SEPARATOR = config.get("INPUT_SEPARATED_BY");


        File input = null;
        try {
            input = MakeDir(FILE_INPUT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(input.exists());

        Scanner myReader = new Scanner(input);


        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Seed anotherone = new Seed();

            ArrayList<String> separatedUrl = new ArrayList<>(List.of(data.split(SEPARATOR)));

            for (String s : separatedUrl) {
                URLartificial temp = new URLartificial(1, s);
                anotherone.getUrls().add(temp);
            }

            data = myReader.nextLine();
            ArrayList<String> separatedWords = new ArrayList<>(List.of(data.split(SEPARATOR)));
            for (String s : separatedWords) {
                SearchWord tempW = new SearchWord(0, s);
                anotherone.getWords().add(tempW);
            }
            array.add(anotherone);


        }


    }

    public static void GetResults(ArrayList<Seed> input) throws IOException {

        final int MAX_LINKS = Integer.parseInt(config.get("MAX_LINKS"));
        final int MAX_DEEPNESS = Integer.parseInt(config.get("MAX_DEPTH"));
        for (int i = 0; i < input.size(); i++) {
            for (int i1 = 0; i1 < input.get(i).getUrls().size() && i1 < MAX_LINKS && input.get(i).getUrls().get(i1).getDeepness() <= MAX_DEEPNESS; i1++) {
                Document document;
                URLartificial current = input.get(i).getUrls().get(i1);
                try {
                    document = Jsoup.connect(current.getUrl()).get();
                } catch (Exception e) {
                    continue;
                }
                document = Jsoup.parse(String.valueOf(document));
                Matches(document, input, i);
                if (input.get(i).getUrls().size() < MAX_LINKS) {
                    Elements links = document.select("a[href]");
                    for (Element link : links) {
                        if (!input.get(i).has(link.attr("abs:href"))) {

                            URLartificial anothtemp = new URLartificial(current.getDeepness() + 1, link.attr("abs:href"));
                            input.get(i).setUrls(anothtemp);

                        }
                    }
                }

            }
            input.get(i).SumUp();
        }

    }

    public static void Matches(Document text, ArrayList<Seed> input, int index) {
        for (SearchWord s : input.get(index).getWords()) {
            s.setCounter(s.getCounter() + StringUtils.countMatches(text.text(), s.getWord()));

        }
    }

    public static File MakeDir(String file) throws URISyntaxException {
        String temp = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();

        temp = temp.substring(0, temp.lastIndexOf('/')) + "/" + file;
        File input = new File(temp);
        return input;
    }


}
