# Lucene Search Engine - Assignment 1 - Info Retrieval 

Code for indexing, searching and other related functionality is stored under the my-app directory. 

+ IndexFiles.java 
This file contains the code to read the cranfield collection, and subsequently index it based on multiple fields. The abstract contains 
fields of .I = index number, .T = title, .A = author, .B = bibliography and .W = contents. 

+ SearchFiles.java 
The search functionality is housed in this file. It enables entry of a query term, along with an option specifying which scoring
technique. Use the -scoring option to specify this:
- scoring = 0 is used to specify the Vector Space Model 
- scoring = 1 is used to specify the BM25 model 

Command to run files java -cp target/my-app-1.0-SNAPSHOT.jar:lucene-8.6.2/core/lucene-core-8.6.2.jar:lucene-8.6.2/analysis/common/lucene-analyzers-common-8.6.2.jar:lucene-8.6.2/queryparser/lucene-queryparser-8.6.2.jar com.mycompany.app.SearchTest
