Lucene Search Engine - Assignment 1 - Info Retrieval 
===

Code for indexing, searching and other related functionality is stored under the my-app directory. 

Indexing Files 
---
+ IndexFiles.java 
This file contains the code to read the cranfield collection, and subsequently index it based on multiple fields. The abstract contains 
fields of .I = index number, .T = title, .A = author, .B = b_field and .W = contents. 
After navigating to the my-app directory, the 'index' bash script can be executed to run the command to index the cran.all.1400 file. 
```
./index
```

Searching
---
+ Handled in SearchFiles.java 

Using the ./search command, two options are available;
+ -s: Set scoring to either 'vsm' or 'bm25' 
+ -q: Set a path to read queries from e.g. /cran/cran.qry. If left blank, defaults to user query input mode.

```
./search -s bm25 -q ../cran/cran.qry
```

Command to run files java -cp target/my-app-1.0-SNAPSHOT.jar:lucene-8.6.2/core/lucene-core-8.6.2.jar:lucene-8.6.2/analysis/common/lucene-analyzers-common-8.6.2.jar:lucene-8.6.2/queryparser/lucene-queryparser-8.6.2.jar com.mycompany.app.SearchTest
