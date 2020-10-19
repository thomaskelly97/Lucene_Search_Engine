package com.mycompany.app;

import java.io.IOException;

import java.nio.file.Paths;

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
      System.out.print(args[i]);
      if ("-scoring".equals(args[i])) {
        System.out.print("setting scoring to" + args[i+1]);
        scoring = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-queryPath".equals(args[i])) {
        queryPath = args[i+1];
        i++;
      }
    }
    
  
    System.out.println("SCORING: " + scoring);
    Directory directory = FSDirectory.open(Paths.get(outputDir));
    
    // Index reader reads from the index at 'directory'
    IndexReader reader = DirectoryReader.open(directory);
    // Searcher takes the index reader 
    IndexSearcher searcher = new IndexSearcher(reader);
    if ((scoring) == 0) {
      searcher.setSimilarity(new ClassicSimilarity());
      System.out.println("Using VSM scoring.");
    }
    if ((scoring) == 1) {
      searcher.setSimilarity(new BM25Similarity());
      System.out.println("Using BM25 scoring.");
    }
    
    // read in the query file 
    BufferedReader queryFile = null;
    String queries = "";
    if (queryPath != "") {
      queryFile = Files.newBufferedReader(Paths.get(queryPath), StandardCharsets.UTF_8);
    }
    Scanner scan = new Scanner(System.in);
    String term = "";
    String nextLine = "";
    int abstractNumber = 0;
    while(true) {
      if (queryPath == "") { // we need to ask user for a term
        System.out.print("> Please enter a query term:");
        term = scan.nextLine();
        System.out.println("...searching for term: " + term);
      } else { // we are using the queries from a given file
        // set term to each line read from the .qry file here
        // so each 
        term = queryFile.readLine();
        System.out.println("Reading in line: " + term);
        if (term == null || term.length() == 0) {
          break;
        }
        // need to parse out .I, .W and get the term to search
        if (term.substring(0,2).equals(".I")) {
          term = queryFile.readLine();
          term = queryFile.readLine();
          nextLine = "";
          while (!term.substring(0,2).equals(".I")) {
            nextLine = nextLine + term;
            term = queryFile.readLine();
            System.out.println(">>> BIG CUNT <<< " + term);
            if (term == null) break;
          }
        }
      }

      // This method reads the number provided using keyboard
      MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Title", "Author", "Bibliography", "Content"}, analyzer);
      Query query = parser.parse(QueryParser.escape(nextLine.trim()));

      TopDocs results = searcher.search(query, 1000);
      
      int numHits = Integer.parseInt(results.totalHits.toString().replaceAll("[\\D]", ""));
      System.out.println("Number of Hits " + numHits);
      
      
      ScoreDoc[] hits = results.scoreDocs;
      for (int j = 0; j < numHits; j++) {
        System.out.println("Score for hit: " + j + "-" + hits[j].score);
        Document doc = searcher.doc(hits[j].doc);
        String path = doc.get("path");
        System.out.println("path: " + path);
        if (path != null) {
			    System.out.println(" 0 " + path.replace(".I ","") + " " +(j+1)+ " " + hits[j].score);		  
		    } 
      }
    }
      // scan.close();
    // Query parser parses the query 

    // Use the parser to parse a query 
    // Store result of using the query to search 

    // query = parser.parse("critical shear");
    // results = searcher.search(query, 5);
    // System.out.println("Hits for superhero: " + results.totalHits);

    // query = parser.parse("air flow in a separating laminar boundary layer");
    // results = searcher.search(query, 5);
    // System.out.println("Hits for buckling of transverse stiffened plates under shear: " + results.totalHits);
  }
}