/* tang
 * Cheese Project
 *
 * outputs a cheese befitting of the property(s) given, or suggestions for
 * picking a cheese
 *
 * don't think it needs to be fully object oriented as of now
 * since the cheese doesn't need to be accessed after its outputted
 *
 * .replace() doesn't work, needs .replaceAll() for regex
 *
 * --------------------------------
 * Student-Family Journal:
 * https://docs.google.com/document/d/1AlPaIxAANy9JW1IKkGREvgN3PvJFqgYeDtjK09z0QAA/edit?tab=t.0
 *
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CheeseFinder {
    private static Scanner scanner = new Scanner(System.in);
    private static String urls =
        "https://en.wikipedia.org/wiki/List_of_Spanish_cheeses;"
        + "https://en.wikipedia.org/wiki/List_of_Italian_cheeses;"
        + "https://en.wikipedia.org/wiki/List_of_Greek_Protected_Designations_of_Origin_cheeses;"
        + "https://en.wikipedia.org/wiki/List_of_German_cheeses;"
        + "https://en.wikipedia.org/wiki/List_of_Dutch_cheeses;"
        + "https://en.wikipedia.org/wiki/Serbian_cheeses";

    public static void main(String[] args)
    {
        System.out.println("\nWelcome to cheese finder.");
        System.out.println("\nEnter properties (e.g., 'hard german') to get a recommended cheese "
                           + "or group of cheeses\nBe as specific as possible");

        // loop for adding other databases if wanted
        boolean urlTrueFalse = true;
        while (urlTrueFalse)
        {
            urlTrueFalse = addUrl();
        }

        // loop for finding and giving best cheese output
        while (true)
        {
            try
            {
                System.out.print("Input, or type 'quit' to exit: ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("quit"))
                {
                    System.out.println("Goodbye!");
                    break;
                }

                String bestCheese = "";
                int highestMatches = 0;

                // iterates through all urls to look for cheese
                for (int urlStart = 0; urlStart < urls.length();)
                {
                    // URLS are separated by a semicolon
                    int urlEnd = urls.indexOf(";", urlStart);
                    if (urlEnd == -1) // this does the last url since it contains no ;
                    {
                        urlEnd = urls.length();
                    }

                    // takes the page content of the current url
                    String url = urls.substring(urlStart, urlEnd).trim();
                    System.out.println("Getting cheese data from: " + url);
                    String pageContent = getPageContent(url);
                    String cheeseData = extractDiscreteCheeseData(pageContent);

                    // finds best match for cheese output for the given input
                    String result = findBestMatch(input, cheeseData);

                    // counts the words match inputs in the output, if current match is better than
                    // last best match, it replaces it
                    if (!result.equals(""))
                    {
                        int matches = countMatchingWords(input, result);
                        if (matches > highestMatches)
                        {
                            highestMatches = matches;
                            bestCheese = result;
                        }
                    }

                    urlStart = urlEnd + 1;
                }

                // removes unwanted characters,
                // and tells user if nothing matches their given input, or outputs the best cheese
                // suggestion
                if (bestCheese.contains("["))
                {
                    bestCheese = bestCheese.replace("[", "");
                }

                if (bestCheese.equals(""))
                {
                    System.out.println(
                        "No cheese matches your properties. Make sure your input is an "
                        + "actual property cheeses can have.");
                }

                else
                {
                    System.out.println("The best cheese(s) is: " + bestCheese);
                }

            } catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("An issue occurred while processing the HTML content.");
        }
        catch (NullPointerException e)
        {
            System.out.println("A null value was attempted to be used while .");
        }
        catch (Exception e)
        {
            System.out.println("An unexpected error occurred.");
        }
        }
    }

    private static String getPageContent(String urlString)
    {
        String page = "";

        try
        {
            URL wiki = new URI(urlString).toURL();
            URLConnection yc = wiki.openConnection();

            // mimic a browser
            yc.setRequestProperty("User-Agent",
                                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                      + "(KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            // sets 10 second timeouts
            yc.setConnectTimeout(10000);
            yc.setReadTimeout(10000);

            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String inputLine;
            // reads each line and adds it to the total html page string
            while ((inputLine = in.readLine()) != null)
            {
                page += inputLine;
            }

            in.close();

        } catch (FileNotFoundException ex)
        {
            System.out.println("unknown");
        } catch (MalformedURLException e)
        {
            System.out.println("badly formed url exception occurred");
        } catch (URISyntaxException e)
        {
            System.out.println("Bad URI syntax");
        } catch (IOException e)
        {
            System.out.println("IO exception occurred");
        }

        return page;
    }

    private static String extractDiscreteCheeseData(String html)
    {

        String cheeseData = "";

        // removing the introduction
        if (html.contains("Serbian cheeses - Wikipedia") ||
            html.contains("List of German cheeses - Wikipedia") ||
            html.contains("List of Greek Protected Designations of Origin cheeses - Wikipedia"))
        {
            String toDelete = html.substring(html.lastIndexOf("<p>"), html.lastIndexOf("</p>") + 4);
            html = html.replace(toDelete, "");
        }

        html = html.substring(html.lastIndexOf("</p>"));

        // extracting the info from each list in the html
        int fromIndex = 0;
        while ((fromIndex = html.indexOf("<li>", fromIndex + 1)) != -1)
        {
            int endIndex = html.indexOf("</li>", fromIndex);
            if (endIndex == -1)
            {
                break;
            }

            String item = html.substring(fromIndex + 4, endIndex);

            String cheeseName = "";

            // removes cheese name from links
            if (item.contains("<a href="))
            {
                int cheeseNameStart = item.indexOf(">", item.indexOf("<a href=")) + 1;
                int cheeseNameEnd = item.indexOf("</a>", cheeseNameStart);
                if (cheeseNameStart != -1 && cheeseNameEnd != -1)
                {
                    cheeseName = item.substring(cheeseNameStart, cheeseNameEnd);
                }
            }

            // remove all HTML tags after extracting the cheese name
            item = item.replaceAll("<[^>]*>", "");

            if (!cheeseName.equals("") && !item.contains(cheeseName))
            {
                item = cheeseName + " - " + item;
            }

            // replaces and removes html elements
            item = item.replace("&amp;", "&")
                       .replace("&lt;", "<")
                       .replace("&gt;", ">")
                       .replace("&quot;", "\"")
                       .replace("&#39;", "'")
                       .replace("&#91;", "[")
                       .replace("&#93;", "]")
                       .replaceAll("\\[\\d+\\]", "");

            cheeseData += item + ";";
            fromIndex = endIndex + 5;
        }
        return cheeseData.trim();
    }

    private static String findBestMatch(String input, String cheeseData)
    {
        String bestMatch = "";
        int highestMatches = 0;
        int fromIndex = 0;

        while (fromIndex < cheeseData.length())
        {
            // finds the end of the word using the semicolons in cheeseData
            int endIndex = cheeseData.indexOf(";", fromIndex);
            if (endIndex == -1)
            {
                endIndex = cheeseData.length();
            }

            String cheese = cheeseData.substring(fromIndex, endIndex).toLowerCase();

            int matches = countMatchingWords(input, cheese);

            // replaces the previous highest match output with the new one if applicable
            if (matches > highestMatches)
            {
                highestMatches = matches;
                bestMatch = cheese;
            }

            fromIndex = endIndex + 1;
        }

        return bestMatch;
    }

    private static int countMatchingWords(String input, String cheese)
    {
        int matches = 0;
        int wordStart = 0;

        // loop to look for count and save the number of matching words in the input
        while (wordStart < input.length())
        {
            // skips over each space to look for the start of next word
            while (wordStart < input.length() && input.charAt(wordStart) == ' ')
            {
                wordStart++;
            }
            int wordEnd = wordStart;

            // finds the end of the word
            while (wordEnd < input.length() && input.charAt(wordEnd) != ' ')
            {
                wordEnd++;
            }

            // checks if a proper word is found, if so processes to see if its a match and adds it
            // if it is
            if (wordStart < wordEnd)
            {
                String word = input.substring(wordStart, wordEnd);
                if (cheese.contains(word))
                {
                    matches++;
                }
            }
            wordStart = wordEnd + 1;
        }

        return matches;
    }

    private static boolean addUrl() {
        boolean breakOut = false;
        String addedUrl = "";
        System.out.println(
            "\nIf you have any other cheese websites, paste their URL here.\nEnter them at your "
            + "own risk, as the formatting for different pages may vary. Enter to move on.");
        addedUrl = scanner.nextLine().toLowerCase().trim();

        if (addedUrl.equals("")) {
            return false;
        }

        if ((addedUrl.contains("https://") || addedUrl.contains("http://")) == false) {
            addedUrl = "https://" + addedUrl;
        }

        // tests connectivity
        try {
            URI uri = URI.create(addedUrl);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // mimic a browser
            connection.setRequestProperty("User-Agent",
                                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                      + "(KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            // sets 2.5 second timeouts
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);

            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                urls = addedUrl + ";\n" + urls;
                System.out.println("URL added");
                return true;
            }
            else {
                System.out.println("Unable to access the URL. Response code: " + responseCode);
            }

        } catch (MalformedURLException e) {
            System.out.println("\nThis is not a valid URL\n");
        } catch (IOException e) {
            System.out.println("Error connecting to the URL");
        }
        return true;
    }
}
