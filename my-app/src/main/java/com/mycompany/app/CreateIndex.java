

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
// INITIAL PRACTICE FILE
public class CreateIndex {
 public static void main(String[] args) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        // To store an index in memory
        // Directory = new RAMDirectory();
        // To store an index on disk
        // Directory = FSDirectory.open("index_path");
        String outputDir = "/Users/user/Documents/College/5th_Year/InfoRet/Lucene_Search_Engine/index";

        Directory directory = FSDirectory.open(Paths.get(outputDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // Index opening mode
        // IndexWriterConfig.OpenMode.CREATE = create a new index
        // IndexWriterConfig.OpenMode.APPEND = open an existing index
        // IndexWriterConfig.OpenMode.CREATE_OR_APPEND = create an index if it does // not exist, otherwise it opens it
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter iwriter = new IndexWriter(directory, config);  
        Document doc = new Document();
        doc.add(new TextField("super_name", "Spider-Man", Field.Store.YES));
        doc.add(new TextField("name", "Peter Parker", Field.Store.YES));
        doc.add(new TextField("category", "superhero", Field.Store.YES));
        iwriter.addDocument(doc);
        iwriter.close();
        directory.close();
    }
}