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
 //New index
 StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
 String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";
 System.out.println("FILE:" + file);
 
 Directory directory = FSDirectory.open(Paths.get(outputDir));
 DirectoryReader ireader = DirectoryReader.open(directory);
IndexSearcher isearcher = new IndexSearcher(ireader);
BooleanQuery.Builder query = new BooleanQuery.Builder();
Query term1 = new TermQuery(new Term("content","university"));
Query term2 = new TermQuery(new Term("content","dublin"));
Query term3 = new TermQuery(new Term("content","college"));
query.add(new BooleanClause(term1,BooleanClause.Occur.SHOULD)); //OR
 

  ScoreDoc[] hits = isearcher.search(query, TOP).scoreDocs;
  System.out.println("Documents: " + hits.length);
  for (int i = 0; i < hits.length; i++) {
      Document hitDoc = isearcher.doc(hits[i].doc);
      System.out.println(i + ") " + hitDoc.get("path") + " " + 	hits[i].score);
  }
  ireader.close();
  directory.close();
  
 
//  IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
//  config.setOpenMode(OpenMode.CREATE); // Create a new index 
 
//  //Create a writer
//  IndexWriter writer = new IndexWriter(directory, config);
 
//  //Now let's try to search for Hello
//  IndexReader reader = DirectoryReader.open(directory);
//  IndexSearcher searcher = new IndexSearcher (reader);
//  QueryParser parser = new QueryParser ("content", standardAnalyzer);
//  Query query = parser.parse("Peter Parker");
//  TopDocs results = searcher.search(query, 5);
//  System.out.println("Hits for Peter Parker -->" + results.totalHits);
 
//  //case insensitive search
//  query = parser.parse("superhero");
//  results = searcher.search(query, 5);
//  System.out.println("Hits for superhero -->" + results.totalHits);
 
//  //search for a value not indexed
//  query = parser.parse("name");
//  results = searcher.search(query, 5);
//  System.out.println("Hits for name -->" + results.totalHits);
 }
}