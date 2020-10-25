package com.mycompany.app;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.FileWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import static java.lang.Math.toIntExact;


public class SearchTest {
  public static void main(String[] args) throws IOException, ParseException {
    int scoring = 0; // Vector Space Model = 0; BM25 = 1
    String queryPath = "";
    String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";
    Analyzer analyzer = new CustomAnalyzer();
    for (int i =0; i < args.length;i++) {
      if ("-scoring".equals(args[i])) {
        scoring = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-queryPath".equals(args[i])) {
        queryPath = args[i+1];
        i++;
      }
    }
    
  
    Directory directory = FSDirectory.open(Paths.get(outputDir));
    
    // Index reader reads from the index at 'directory'
    IndexReader reader = DirectoryReader.open(directory);
    // Searcher takes the index reader 
    IndexSearcher searcher = new IndexSearcher(reader);
    if (scoring == 0) {
      searcher.setSimilarity(new ClassicSimilarity());
    }
    if (scoring == 1) {
      searcher.setSimilarity(new BM25Similarity());
    }
    
    // read in the query file 
    BufferedReader queryFile = null;
    String queries = "";
    String term = ""; // or read from file
    if (queryPath != "" && !queryPath.matches("0")) {
      queryFile = Files.newBufferedReader(Paths.get(queryPath), StandardCharsets.UTF_8);
      term = queryFile.readLine(); 
    }

    Boolean userInputMode = false; 
    Scanner scan = new Scanner(System.in); // scan input
    String nextLine = "";
    String queryTerm = "";
    int queryId = 0; // count queries 
    FileWriter fileWriter = new FileWriter("../cran/results_for_trec_eval");
    System.out.println("Processing query...");
    while(true) {
      System.out.println("------------------------\n");

      if (queryPath == "" ||  queryPath.matches("0")) { // we need to ask user for a term
        System.out.print("> Please enter a query term:");
        term = scan.nextLine();
        System.out.println("...searching for term: " + term);
        queryTerm = term.toLowerCase();
        userInputMode = true;
      } else { // we are using the queries from a given file
        // set term to each line read from the .qry file here
        if (term == null) {
          break;
        }
        // need to parse out .I, .W and get the term to search
        if (term.substring(0,2).equals(".I")) {
          queryId++;
          term = queryFile.readLine();
          if (term.equals(".W")) {
            term = queryFile.readLine();
          }
          nextLine = "";
          while (!term.substring(0,2).equals(".I")) {
            nextLine = nextLine + " " + term;
            term = queryFile.readLine();
            if (term == null) break;
          }
        }
        queryTerm = nextLine;   
      }

      // https://stackoverflow.com/questions/5100528/how-to-do-a-multi-field-phrase-search-in-lucene
      MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Title", "Author", "B_Field", "Content"}, analyzer);
      Query query = parser.parse(QueryParser.escape(queryTerm.trim()));

      TopDocs results = searcher.search(query, 50);
      int numHits = Integer.parseInt(results.totalHits.toString().replaceAll("[\\D]", "")); // ugly parsing because we need int from string
      
      results = searcher.search(query, numHits);
      ScoreDoc[] hits = results.scoreDocs;
      
      System.out.println("------ New query " + queryId + " ------\n==> " + queryTerm);
      System.out.println(numHits + " total matching documents.");
      
      for (int j = 0; j < numHits; j++) { // hits.length seems to always report a value = 10
        Document doc = searcher.doc(hits[j].doc);
        String abstractNumber = doc.get("path");
        if (abstractNumber != null) {
          // System.out.println(queryId + " Q0" + abstractNumber.replace(".I","") + " " + hits[j].score + " " + (j+1) + " STANDARD");
          // This string needs to be written to a trec_eval_results file ^^^
          fileWriter.write(queryId + " Q0" + abstractNumber.replace(".I","") + " " + hits[j].score + " " + (j+1) + " STANDARD\n");
		    } 
      }
      if (userInputMode) { // after we process the input mode query, we need to close so that results can be sent to results file
        System.out.println("==> Finished processing query & results sent to '/cran/results_for_trec_eval'");
        fileWriter.close();
        System.exit(1);
      }
    }
    System.out.println("-> Finished processing " + queryId+ " query(s) & results sent to '/cran/results_for_trec_eval'.");
    fileWriter.close();
  }
}