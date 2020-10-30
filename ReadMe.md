Lucene Search Engine - Assignment 1 - Info Retrieval 
===
MVN was used to setup the Java Project. After downloading lucene, compile the project with; 
```
mvn package
```

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
The index is stored under /index directory. 
Just to clarify the difference here; 
+ ./index is a bash script under /my-app that runs the `/src/main/java/com/company/app/IndexFiles.java` file, ensuring that the required lucene JARS are added to path. The full command can be viewed in this ./index bash script. 
+ The /index directory is used to store the index of cran field collection. 
Searching
---
+ Handled in SearchFiles.java - under `/my-app/src/java/com/company/app/SearchFiles.java`.
I wrote a bash script to execute the search functionality. 
Using the ./search command, two options are available;
+ -s: Set scoring to either 'vsm' or 'bm25' 
+ -q: Set a path to read queries from e.g. /cran/cran.qry. If left blank, defaults to user query input mode.

```
./search -s bm25 -q ../cran/cran.qry
```
When querying is complete, the query results and scoring is stored in the `/cran/results_for_trec_eval` file.

Evaluation
---
Navigate to the trec_eval directory and as specified. The results file and cranqrel are formatted as specified [here](http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system).
```
./trec_eval ../cran/reformatted_cranqrel ../cran/results_for_trec_eval
```

Note: I reformatted `cranqel` to adhere to the format specified in the trec_eval docs for this file. This is the function of the python script under /cran/reformat_cranqrel.py. 


Command to run files without bash scripts; `java -cp target/my-app-1.0-SNAPSHOT.jar:lucene-8.6.2/core/lucene-core-8.6.2.jar:lucene-8.6.2/analysis/common/lucene-analyzers-common-8.6.2.jar:lucene-8.6.2/queryparser/lucene-queryparser-8.6.2.jar com.mycompany.app.SearchTest`
