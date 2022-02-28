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
    static HashMap<String, String> configValues = new HashMap<>();

    public static void main(String[] args) throws URISyntaxException {


        ArrayList<Link> dataForAnalysis = new ArrayList<>();




        try {
            GetInput(dataForAnalysis);
        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            GetResults(dataForAnalysis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Output(dataForAnalysis);


    }

    private static void GetConfig() throws URISyntaxException {

        File input = MakeDir("config.txt");

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


        while (true) {
            assert myReader != null;
            if (!myReader.hasNextLine()) break;
            String data = myReader.nextLine();

            String[] dataParts = data.split("=");
            configValues.put(dataParts[0], dataParts[1]);
        }
    }

    public static void Output(ArrayList<Link> everyInput) {
        final String FILE_OUTPUT = configValues.get("OUTPUT_NAME");
        final String SEPARATOR = configValues.get("OUTPUT_SEPARATED_BY");
        final String REPORT_FORM = configValues.get("REPORT_FORM");

        if (REPORT_FORM.equals("top_10")){
            Sort(everyInput);
        }

        try {
            File output = MakeDir(FILE_OUTPUT);
            output.createNewFile();
            PrintWriter writer = new PrintWriter(output);


            for (Link s : everyInput) {
                for (CustomUrl u : s.getUrls()) {
                    if (u.getDeepness() == 1) {
                        writer.print(u.getUrl() + SEPARATOR);
                    } else break;
                }
                for (Word w : s.getWords()) {
                    writer.print(w.getCounter() + SEPARATOR);
                }
                writer.println(s.getTotalCount());
            }
            writer.close();



        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private static void Sort(ArrayList<Link> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            int min = i;

            for (int i1 = i + 1; i1 < data.size(); i1++){
                if(data.get(i).getTotalCount() < data.get(i1).getTotalCount())min = i1;
            }
            Collections.swap(data, i, min);


        }
    }

    public static void GetInput(ArrayList<Link> dataForInput) throws FileNotFoundException, MalformedURLException {
        final String FILE_INPUT = configValues.get("INPUT_NAME");
        final String SEPARATOR = configValues.get("INPUT_SEPARATED_BY");


        File input = null;
        try {
            input = MakeDir(FILE_INPUT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert input != null;

        Scanner myReader = new Scanner(input);


        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Link tempLink = new Link();

            ArrayList<String> separatedUrl = new ArrayList<>(List.of(data.split(SEPARATOR)));

            for (String s : separatedUrl) {
                CustomUrl tempUrl = new CustomUrl(1, s);
                tempLink.getUrls().add(tempUrl);
            }

            data = myReader.nextLine();
            ArrayList<String> separatedWords = new ArrayList<>(List.of(data.split(SEPARATOR)));
            for (String s : separatedWords) {
                Word tempWord = new Word(0, s);
                tempLink.getWords().add(tempWord);
            }
            dataForInput.add(tempLink);


        }


    }

    public static void GetResults(ArrayList<Link> dataForResults) throws IOException {

        final int MAX_LINKS = Integer.parseInt(configValues.get("MAX_LINKS"));
        final int MAX_DEEPNESS = Integer.parseInt(configValues.get("MAX_DEPTH"));
        for (int i = 0; i < dataForResults.size(); i++) {
            for (int i1 = 0; i1 < dataForResults.get(i).getUrls().size() && i1 < MAX_LINKS && dataForResults.get(i).getUrls().get(i1).getDeepness() <= MAX_DEEPNESS; i1++) {
                Document document;
                CustomUrl current = dataForResults.get(i).getUrls().get(i1);
                try {
                    document = Jsoup.connect(current.getUrl()).get();
                } catch (Exception e) {
                    continue;
                }
                document = Jsoup.parse(String.valueOf(document));
                Matches(document, dataForResults, i);
                if (dataForResults.get(i).getUrls().size() < MAX_LINKS) {
                    Elements links = document.select("a[href]");
                    for (Element link : links) {
                        if (!dataForResults.get(i).has(link.attr("abs:href"))) {

                            CustomUrl tempUrl = new CustomUrl(current.getDeepness() + 1, link.attr("abs:href"));
                            dataForResults.get(i).setUrls(tempUrl);

                        }
                    }
                }

            }
            dataForResults.get(i).SumUp();
        }

    }

    public static void Matches(Document text, ArrayList<Link> inputForMatches, int index) {
        for (Word s : inputForMatches.get(index).getWords()) {
            s.setCounter(s.getCounter() + StringUtils.countMatches(text.text(), s.getWord()));

        }
    }

    public static File MakeDir(String file) throws URISyntaxException {
        String temp = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();

        temp = temp.substring(0, temp.lastIndexOf('/')) + "/" + file;
        return new File(temp);
    }


}
