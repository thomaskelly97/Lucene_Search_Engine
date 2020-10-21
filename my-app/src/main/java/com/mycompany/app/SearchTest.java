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
    String queryTerm = "";
    
    while(true) {
      System.out.println("------------------------\n");
      if (queryPath == "") { // we need to ask user for a term
        System.out.print("> Please enter a query term:");
        term = scan.nextLine();
        System.out.println("...searching for term: " + term);
        queryTerm = term.toLowerCase();
      } else { // we are using the queries from a given file
        // set term to each line read from the .qry file here
        // so each 
        term = queryFile.readLine();
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
            if (term == null) break;
          }
        }
        queryTerm = nextLine;
      }

      // https://stackoverflow.com/questions/5100528/how-to-do-a-multi-field-phrase-search-in-lucene
      MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Title", "Author", "Bibliography", "Content"}, analyzer);
      Query query = parser.parse(QueryParser.escape(queryTerm.trim()));

      TopDocs results = searcher.search(query, 10);
      ScoreDoc[] hits = results.scoreDocs;
      
      int numHits = Integer.parseInt(results.totalHits.toString().replaceAll("[\\D]", "")); // ugly parsing because we need int from string
      System.out.println("------ Query ------\n==> " + queryTerm);
      System.out.println(numHits + " total matching documents.");

      for (int j = 0; j < hits.length; j++) { // hits.length seems to always report a value = 10
        Document doc = searcher.doc(hits[j].doc);
        String abstractNumber = doc.get("path");
        if (abstractNumber != null) {
			    System.out.println((j+1) + " - " + abstractNumber.replace(".I ","abstract: ") +  " - score: " + hits[j].score);		  
		    } 
      }
    }
      // scan.close();
  }
}