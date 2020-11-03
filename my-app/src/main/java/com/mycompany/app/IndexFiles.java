package com.mycompany.app;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class IndexFiles {
 public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer = new CustomAnalyzer(); // now using custom analyzer

        // input file = files to index
        String inputFilePath = "../cran/cran.all.1400";
        File file = new File(inputFilePath);
        // outputDir where the index will be stored
        String outputDir = "../index";

        Directory directory = FSDirectory.open(Paths.get(outputDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); // create or append works on either first time or to append to existing index
        IndexWriter iwriter = new IndexWriter(directory, config);  
        
        String currentLine = ""; // line from buffer 
        String fieldName = "";
        Document document = null;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            currentLine = br.readLine();
            while(currentLine != null){
                System.out.print("-> Indexing document: " + currentLine + "- ");
                document = new Document();
                if (currentLine.matches("(.*)(.I)(.*)")) {
                    Field pathField = new StringField("path", currentLine, Field.Store.YES);
                    document.add(pathField);
                    currentLine = br.readLine();
                    while (!currentLine.matches("(.*)(.I)(.*)")) {
                        if (currentLine.matches("(.*)(.T)(.*)")) {
                            fieldName = "Title";
                            currentLine = br.readLine();
                            System.out.println(currentLine);
                        }
                        else if (currentLine.matches("(.*)(.A)(.*)")) {
                            fieldName = "Author";
                            currentLine = br.readLine();
                        }
                        else if (currentLine.matches("(.*)(.B)(.*)")) {
                            fieldName = "B_Field";
                            currentLine = br.readLine();
                        }
                        else if (currentLine.matches("(.*)(.W)(.*)")) {
                            fieldName = "Content";
                            currentLine = br.readLine();
                        }
                        document.add(new TextField(fieldName, currentLine, Field.Store.YES));
                        currentLine = br.readLine();
                        if (currentLine == null) break;
                    }
                }
                iwriter.addDocument(document);
            }
            iwriter.close();
            System.out.println("==> Finished indexing documents.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        directory.close();
    }
}