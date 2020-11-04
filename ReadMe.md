Lucene Search Engine - Assignment 1 - Info Retrieval 
===
MVN was used to setup the Java Project. After downloading lucene, compile the project at /my-app/ directory with; 
```
cd my-app
mvn package
```

Code for indexing, searching and other related functionality is stored under the my-app directory. 

Indexing Files 
---
+ IndexFiles.java 
This file contains the code to read the cranfield collection, and subsequently index it based on multiple fields. 
After navigating to the my-app directory, the 'index' bash script can be executed to run the command to index the cran.all.1400 file. 
```
./index
```
The index is stored under /index directory. 

Searching
---
+ Handled in SearchTest.java - under `/my-app/src/java/com/company/app/SearchTest.java`.
I also wrote a bash script to execute the search functionality, under /my-app/; `./search`. 
Using the ./search command, two options are available;
+ -s: Set scoring to either 'vsm' or 'bm25' 
+ -q: Set a path to read queries from e.g. /cran/cran.qry. If left blank, defaults to user query input mode. e.g.;

```
./search -s bm25 -q ../cran/reformatted_cran.qry
```
When querying is complete, the query results and scoring is stored in the `/cran/results_for_trec_eval` file.

Evaluation
---
Navigate to the trec_eval directory. The results file and cranqrel are formatted as specified [here](http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system).
```
./trec_eval ../cran/reformatted_cranqrel ../cran/results_for_trec_eval
```


Please Note; 
---
+ The cran.qry file was reformatted as described in the blackboard announcement, and written to **/cran/reformatted_cran.qry**. This file should be used when searching instead of cran.qry.
+ I noticed the cranqrel file was also incorrectly formatted for trec_eval, and reformatted that and stored in **/cran/reformatted_cranqrel**. This file should be used instead of cranqrel for evaluation.
- This is the function of the python scripts in the /cran directory.


Command to run files without bash scripts; `java -cp target/my-app-1.0-SNAPSHOT.jar:lucene-8.6.2/core/lucene-core-8.6.2.jar:lucene-8.6.2/analysis/common/lucene-analyzers-common-8.6.2.jar:lucene-8.6.2/queryparser/lucene-queryparser-8.6.2.jar com.mycompany.app.SearchTest -scoring vsm -queryPath ../cran/reformatted_cran.qry`
