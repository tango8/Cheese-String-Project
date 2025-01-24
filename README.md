# Cheese-string-Project
Cheese Finder is a project focused primarily on the usage of strings and string methods to help you find the best cheese for your input properties. 
It webscrapes online databases to find the best cheese or cheese property for the given properties.

## Motivation
The motivation for this project is to create a cheese finder for those bored of the normal cheeses of everyday life, and for the practicing of string methods and webscraping. 

## Demo
[![Watch the video](https://github.com/tango8/Cheese-String-Project/raw/main/assets/CheeseStringProjectDemoThumbnail.png
)](https://github.com/tango8/Cheese-String-Project/raw/main/CheeseStringProjectDemo.mov)

## How it works
1. The user inputs any extra urls, or skips
2. The user inputs any cheese properties that they want a suggestion for, e.g. 'hard german'
3. The program takes and processes data from the predefined urls(listet below) and any extra urls given as a string
   * List of Spanish Cheeses Wikipedia
   * List of Italian Cheeses Wikipedia
   * List of Greek Protected Designations of Origin Cheeses Wikipedia
   * List of German Cheeses Wikipedia
   * List of Dutch Cheeses Wikipedia
   * Serbian Cheeses Wikipedia
5. The getPageContent and extractDiscreteCheeseData methods handle data retrieval and processing
6. The findBestMatch and countMatchingWords methods determine the best cheese suggestion based on the user input
7. The code outputs any specific cheese or suggestions that match the given properties in the urls

### String methods include:
* .trim()
  * used to ensure extra spaces don't interfere with proecesses
* .toLowerCase()
  * default case to compare strings as
* .indexOf()
  * for locating the index of semicolons separating the URLs, as well as parsing the HTML
* .substring()
  * extracted each individual URL and word from the cheesedata, as well as taking what is needed from the HTML of the webpages
* .replaceAll()
  * uses regular expressions to remove all the HTML tags that would otherwise needed to be imported and removed one by one using .replace() 
* .replace()
  * replaces HTML entities and cleans up the output 
* .contains()
  * determines if a string contains an HTML tag, crucial for removing unwnted paragraphs, and to determine if a word matches an input
* .equals()
  * checks if the user inputs quit, or if a string is empty
* .charAt()
  * used to iterate through strings and skip spaces
* .length()
  * determines the length of words for setting iteration boundaries 
* .lastIndexOf()
  * locates the HTML tags that were crucial to keep the given wiki pages' formatting somewhat consistent  

## Call graph 
(format credit - Deepseek AI)

main 

├── addUrl 

│ └── (calls URI.create, URL.openConnection, 
HttpURLConnection.setRequestProperty, etc.) 

├── getPageContent 

│ └── (calls URI.toURL, URLConnection.setRequestProperty, 
BufferedReader.readLine, etc.) 

├── extractDiscreteCheeseData 

│ └── (calls String.substring, String.replace, String.replaceAll, etc.)  

├── findBestMatch 

│ └── countMatchingWords 

└── countMatchingWords




