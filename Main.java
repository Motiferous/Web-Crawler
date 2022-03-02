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
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    static HashMap<String, String> configValues = new HashMap<>();//We are using a hashmap to store the values of parameters.

    public static void main(String[] args) {

        ArrayList<Link> dataForAnalysis = new ArrayList<>();//An array of links.

        //We are calling a function GetInput() to get input from our files.
        try {
            GetInput(dataForAnalysis);
        } catch (FileNotFoundException | MalformedURLException | URISyntaxException e) {
            System.out.println("There was an error. Please check your files or/and URLs.");
        }

        //We are calling a function GetResults() to get statistical data from our links.
        try {
            GetResults(dataForAnalysis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //We are writing the data to files with Output().
        Output(dataForAnalysis);

    }

    //Function which gets WebCrawler's parameters.
    private static void GetConfig() throws URISyntaxException {

        //We get the file path of our parameters.
        File input = MakeDir("config.txt");

        //Reader for reading config file.
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

        //Reading config file
        while (true) {
            assert myReader != null;
            if (!myReader.hasNextLine()) break;
            String data = myReader.nextLine();

            //Splitting the read line and putting the two parts int HashMap.
            String[] dataParts = data.split("=");
            configValues.put(dataParts[0], dataParts[1]);
        }
    }

    //Function for writing output to the file.
    public static void Output(ArrayList<Link> everyInput) {
        //We are getting the needed information for output from configValues HashMap.
        final String FILE_OUTPUT = configValues.get("OUTPUT_NAME");
        final String SEPARATOR = configValues.get("OUTPUT_SEPARATED_BY");
        final String REPORT_FORM = configValues.get("REPORT_FORM");

        //If wanted, the results are sorted.
        if (REPORT_FORM.equals("top_10")) {
            Sort(everyInput);
        }

        try {
            //We get the path for our output file.
            File output = MakeDir(FILE_OUTPUT);

            //We are preparing to write into output file.
            output.createNewFile();
            PrintWriter writer = new PrintWriter(output);

            //We are writting into the file.
            for (Link s : everyInput) {
                for (CustomUrl u : s.getUrls()) {

                    //Checking if the link is original one.
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
            System.out.println("There was an error. Please recheck your config and file names.");
        }


    }

    //Function for sorting in data in descending order.
    private static void Sort(ArrayList<Link> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            int min = i;

            for (int i1 = i + 1; i1 < data.size(); i1++) {
                if (data.get(i).getTotalCount() < data.get(i1).getTotalCount()) min = i1;
            }
            Collections.swap(data, i, min);
        }
    }

    //Function for getting input.
    public static void GetInput(ArrayList<Link> dataForInput) throws FileNotFoundException, MalformedURLException, URISyntaxException {
        //Calling function to get our parameters and then using them.
        GetConfig();
        final String FILE_INPUT = configValues.get("INPUT_NAME");
        final String SEPARATOR = configValues.get("INPUT_SEPARATED_BY");

        //Getting full input file path.
        File input = MakeDir(FILE_INPUT);

        //Preparing to read data.
        Scanner myReader = new Scanner(input);

        //Reading.
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Link tempLink = new Link();

            //Adding links to an array.
            ArrayList<String> separatedUrl = new ArrayList<>(List.of(data.split(SEPARATOR)));
            for (String s : separatedUrl) {
                CustomUrl tempUrl = new CustomUrl(1, s);
                tempLink.getUrls().add(tempUrl);
            }

            //Reading the words needed for search.
            data = myReader.nextLine();
            ArrayList<String> separatedWords = new ArrayList<>(List.of(data.split(SEPARATOR)));
            for (String s : separatedWords) {
                Word tempWord = new Word(0, s);
                tempLink.getWords().add(tempWord);
            }
            dataForInput.add(tempLink);

        }
    }

    //Function that gets the results from given data.
    public static void GetResults(ArrayList<Link> dataForResults) throws IOException {
        //Getting setting from config.
        final int MAX_LINKS = Integer.parseInt(configValues.get("MAX_LINKS"));
        final int MAX_DEEPNESS = Integer.parseInt(configValues.get("MAX_DEPTH"));

        //Double loop for different searches and different urls in one search. It is limited by config file.
        for (int i = 0; i < dataForResults.size(); i++) {
            for (int i1 = 0; i1 < dataForResults.get(i).getUrls().size() && i1 < MAX_LINKS && dataForResults.get(i).getUrls().get(i1).getDeepness() <= MAX_DEEPNESS; i1++) {
                Document document;
                CustomUrl current = dataForResults.get(i).getUrls().get(i1);

                //We try to connect to the link. If failed we skip this link and go to next one.
                try {
                    document = Jsoup.connect(current.getUrl()).get();
                } catch (Exception e) {
                    continue;
                }

                //Getting HTML of the site.
                document = Jsoup.parse(String.valueOf(document));
                //Calling function to count the word matches in this page.
                Matches(document, dataForResults, i);

                //If didn`t hit the maximum amount of links then we search for new links.
                if (dataForResults.get(i).getUrls().size() < MAX_LINKS) {
                    Elements links = document.select("a[href]");
                    for (Element link : links) {
                        if (!dataForResults.get(i).has(link.attr("abs:href"))) {

                            CustomUrl tempUrl = new CustomUrl(current.getDeepness() + 1, link.attr("abs:href"));
                            //We add the found link to the end of this link array. This way we will find links in array as long as there are some and as long as config is satisfied.
                            dataForResults.get(i).setUrls(tempUrl);

                        }
                    }
                }

            }
            //We sum up word numbers.
            dataForResults.get(i).SumUp();
        }

    }

    // Function calculates the matches of words in given text. It uses StringUtils library.
    public static void Matches(Document text, ArrayList<Link> inputForMatches, int index) {
        for (Word s : inputForMatches.get(index).getWords()) {
            s.setCounter(s.getCounter() + StringUtils.countMatches(text.text(), s.getWord()));
        }
    }

    // Function that gets the full path (not really) of the file.
    public static File MakeDir(String file) throws URISyntaxException {
        String temp = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        temp = temp.substring(0, temp.lastIndexOf('/'));
        temp = temp.substring(0, temp.lastIndexOf('/')) + "/" + file;



        return new File(temp);
    }
}
