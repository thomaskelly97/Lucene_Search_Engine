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

public class LuceneTest {
 
 public static void main(String[] args) throws IOException, ParseException {
 //New index
 StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
 String inputFilePath = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/cran/cran.all.1400";
 String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";
 File file = new File(inputFilePath);
 System.out.println("FILE:" + file);
 
 Directory directory = FSDirectory.open(Paths.get(outputDir));
 System.out.println("DIR O:" + directory);
 
 IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
 config.setOpenMode(OpenMode.CREATE); // Create a new index 
 
 //Create a writer
 IndexWriter writer = new IndexWriter(directory, config);
 
 Document document = new Document ();
 // Looks like we're reading in the file and essentially parsing it into documents
 try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
     document.add(new TextField("content", br));
     writer.addDocument(document);
     writer.close();
 } catch (IOException e) {
     e.printStackTrace();
 }
  
 //Now let's try to search for Hello
 IndexReader reader = DirectoryReader.open(directory);
 IndexSearcher searcher = new IndexSearcher (reader);
 QueryParser parser = new QueryParser ("content", standardAnalyzer);
 Query query = parser.parse("has anyone investigated the shear buckling of stiffened plates");
 TopDocs results = searcher.search(query, 5);
 System.out.println("Hits for has anyone investigated the shear buckling of stiffened plates -->" + results.totalHits);
 
 //case insensitive search
 query = parser.parse("analytical study");
 results = searcher.search(query, 5);
 System.out.println("Hits for analytical study -->" + results.totalHits);
 
 //search for a value not indexed
 query = parser.parse("siehfseifuhesfiuh");
 results = searcher.search(query, 5);
 System.out.println("Hits for Hi there -->" + results.totalHits);
 }
}