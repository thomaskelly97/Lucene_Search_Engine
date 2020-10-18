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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class SearchTest {
  public static void main(String[] args) throws IOException, ParseException {
      System.out.println("-> Start");
      String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";
      StandardAnalyzer analyzer = new StandardAnalyzer();
      Directory directory = FSDirectory.open(Paths.get(outputDir));
      
      // Index reader reads from the index at 'directory'
      IndexReader reader = DirectoryReader.open(directory);
      // Searcher takes the index reader 
      IndexSearcher searcher = new IndexSearcher(reader);
      // Query parser parses the query 
      QueryParser parser = new QueryParser("content", analyzer);

      // Use the parser to parse a query 
      Query query = parser.parse("Hello");
      // Store result of using the query to search 
      TopDocs results = searcher.search(query, 5);
      System.out.println("Hits for hello: "+ results.totalHits);

      query = parser.parse("superhero");
      results = searcher.search(query, 5);
      System.out.println("Hits for superhero: " + results.totalHits);

      query = parser.parse("buckling of transverse stiffened plates under shear");
      results = searcher.search(query, 5);
      System.out.println("Hits for buckling of transverse stiffened plates under shear: " + results.totalHits);
  }
}