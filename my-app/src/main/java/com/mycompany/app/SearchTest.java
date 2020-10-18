package com.mycompany.app;

import java.io.IOException;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

public class SearchTest {
  public static void main(String[] args) throws IOException, ParseException {
    int scoring = 0; // Vector Space Model = 0; BM25 = 1
    String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";
    StandardAnalyzer analyzer = new StandardAnalyzer();
    for (int i =0; i < args.length;i++) {
      System.out.print(args[i]);
      if ("-scoring".equals(args[i])) {
        System.out.print("setting scoring to" + args[i+1]);
        scoring = Integer.parseInt(args[i+1]);
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
    
    Scanner scan = new Scanner(System.in);
    while(true) {
      System.out.print("> Please enter a query term:");

        // This method reads the number provided using keyboard
      String term = scan.nextLine();
      System.out.println("...searching for term: " + term);
      QueryParser parser = new QueryParser("Title", analyzer);
      Query query = parser.parse(term);
      TopDocs results = searcher.search(query, 1400);
      System.out.println("Hits for " + term + ": "+ results.totalHits);
      System.out.println("RESULTS: " + results.scoreDocs);
        // Closing Scanner after the use
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