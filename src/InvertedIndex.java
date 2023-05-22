import java.io.*;
import java.util.*;

    public class InvertedIndex {

        public static void main(String[] args) {

            // Create a hashmap to store the index
            HashMap<String, DictEntry> index = new HashMap<String, DictEntry>();

            // Read the text files and build the index
            for (int i = 1; i <= 10; i++) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("file" + i + ".txt"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split("\\s+");
                        for (String word : words) {
                            // Check if the word is already in the index
                            if (index.containsKey(word)) {
                                DictEntry entry = index.get(word);
                                // Increment the term frequency
                                entry.term_freq++;
                                // Check if the current document has already been added to the posting list
                                if (entry.pList != null && entry.pList.docId == i) {
                                    entry.pList.dtf++;
                                } else {
                                    // Add the document to the posting list
                                    Posting newPosting = new Posting();
                                    newPosting.docId = i;
                                    entry.doc_freq++;
                                    newPosting.next = entry.pList;
                                    entry.pList = newPosting;
                                }
                            } else {
                                // Add the word to the index
                                DictEntry entry = new DictEntry();
                                entry.term_freq = 1;
                                Posting newPosting = new Posting();
                                newPosting.docId = i;
                                entry.doc_freq = 1;
                                entry.pList = newPosting;
                                index.put(word, entry);
                            }
                        }
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Read a word and list all files containing the word
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a word to search for: ");
            String searchWord = scanner.nextLine();
            if (index.containsKey(searchWord)) {
                DictEntry entry = index.get(searchWord);
                Posting currentPosting = entry.pList;
                System.out.println("Files containing the word \"" + searchWord + "\":");
                while (currentPosting != null) {
                    System.out.println("file" + currentPosting.docId + ".txt");
                    currentPosting = currentPosting.next;
                }
            } else {
                System.out.println("The word \"" + searchWord + "\" was not found in any of the files.");
            }
        }

        public static class Posting {
            public Posting next = null;
            public int docId;
            public int dtf = 1;
        }

        public static class DictEntry {
            public int doc_freq = 0;
            public int term_freq = 0;
            public Posting pList =null;}

    }

