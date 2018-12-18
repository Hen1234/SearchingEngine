package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Searcher {

    Ranker ranker;
    String query;
    String queryAfterParse;
    String[] splitedQueryAfterParse;

    HashMap<String, Docs> Documents;
    HashSet<QueryDoc> docRelevantForTheQuery;
    static double avdl;
    static int numOfDocumentsInCorpus;


    public Searcher() {
        //this.query = query;
        try {
            FileInputStream f = new FileInputStream(new File(Indexer.pathDir + "\\" + "DocsAsObject.txt"));
            ObjectInputStream o = new ObjectInputStream(f);
            Documents = (HashMap<String, Docs>) o.readObject();
            o.close();

        } catch (Exception e) {
        }
        docRelevantForTheQuery = new HashSet<QueryDoc>();
        ranker = new Ranker();
        numOfDocumentsInCorpus = Documents.size();

    }

    //public void setQuery(ArrayList<QueryTerm> query) {
//        this.query = query;
//    }


    public void setQuery(String query) {
        this.query = query;
    }

    private void pasreQuery(String query) throws IOException {

        queryAfterParse = ReadFile.p.parser(null, query, ReadFile.toStem, true);
        splitedQueryAfterParse = queryAfterParse.split(" ");

        for (int i = 0; i < splitedQueryAfterParse.length; i++) {
            String curretTermOfQuery = splitedQueryAfterParse[i];

            initQueryTermAndQueryDocs(curretTermOfQuery);


        }




        for(QueryDoc currentQueryDoc: docRelevantForTheQuery ){

            ranker.getQueryDocFromSearcher(currentQueryDoc);

        }

    }


    private void initQueryTermAndQueryDocs(String curretTermOfQuery) {

        QueryTerm currentQueryTerm = null;
        //check if the term exists the dictionary
        //toLowerCase
        if (Indexer.sorted.containsKey(curretTermOfQuery.toLowerCase())) {
            //create a new QueryTerm
            currentQueryTerm = new QueryTerm(curretTermOfQuery.toLowerCase());
        } else {
            //toUpperCase
            if (Indexer.sorted.containsKey(curretTermOfQuery.toUpperCase())) {
                //create a new QueryTerm
                currentQueryTerm = new QueryTerm(curretTermOfQuery.toUpperCase());
            }
        }
        if(currentQueryTerm != null){

            //take the term's pointer from the dictionary
            String pointer = Indexer.sorted.get(curretTermOfQuery.toLowerCase());
            String[] numOfFileAndLineOfTerm = pointer.split(",");
            String fileNum = numOfFileAndLineOfTerm[1];
            String lineNum = numOfFileAndLineOfTerm[2];
            Integer lineNumInt = Integer.parseInt(lineNum);
            String lineFromFile = "";
            try {
                //doc:FBIS3-29#2=27066 ,27079 doc:FBIS3-5232#1=481 DF- 2 TIC- 3
                lineFromFile = Files.readAllLines(Paths.get(Indexer.pathDir + "\\finalposting" + fileNum + ".txt")).get(lineNumInt);
            } catch (Exception e) {
            }

            //ArrayList<String> docs = new ArrayList<>();
            //ArrayList<Integer> amountsPerDoc = new ArrayList<>();
            String docNo = "";
            String tfString = "";

            //update the hashMap of docs and df of the currentQueryTerm
            for (int k = 0; k < lineFromFile.length(); k++) {

                if (lineFromFile.charAt(k) == ':') {
                    k++;

                    //find the doc
                    while (lineFromFile.charAt(k) != '#') {
                        docNo = docNo + lineFromFile.charAt(k);
                        k++;
                    }
                    k++;

                    //find the amountAppearence in the doc
                    while (lineFromFile.charAt(k) != '=') {
                        tfString = tfString + lineFromFile.charAt(k);
                        k++;
                    }

                    int tf = Integer.parseInt(tfString);

                    if (Documents.containsKey(docNo)) {
                        currentQueryTerm.getDocsAndAmount().put(docNo, tf);
                        //add the QueryTerm to the relevant doc
                        Docs docFromOriginalDocs = Documents.get(docNo);
                        QueryDoc newQueryDoc = new QueryDoc(docFromOriginalDocs.getDocNo());
                        newQueryDoc.setLength(docFromOriginalDocs.getDocLength());
                        //add the new QueryDoc to the HashSet of the relevant docs for the query
                        docRelevantForTheQuery.add(newQueryDoc);
                    }


                }
                if (lineFromFile.charAt(k) == 'D' && k + 5 < lineFromFile.length() &&
                        lineFromFile.charAt(k + 1) == 'F' && lineFromFile.charAt(k + 2) == '-' &&
                        lineFromFile.charAt(k + 3) == ' ') {

                    String df = "";
                    int q = 4;
                    while (k + q < lineFromFile.length()) {
                        if (lineFromFile.charAt(k + q) != ' ') {
                            df = df + lineFromFile.charAt(k + q);
                        }
                        q++;

                    }

                    try {
                        Integer dfInt = Integer.parseInt(df);
                        currentQueryTerm.setDf(dfInt);
                    } catch (Exception e) {
                    }

                }


            }

            //update the amount of appearence in the query
            for (int i = 0; i < splitedQueryAfterParse.length; i++) {

                if (splitedQueryAfterParse[i].equals(curretTermOfQuery)) {
                    currentQueryTerm.setAppearanceInQuery(currentQueryTerm.getAppearanceInQuery() + 1);

                }


            }

        }

    }

    private void initAvd() {
        Integer countDocsLength = 0;
        Iterator it = Documents.entrySet().iterator();
        while (it.hasNext()) {
            //Terms nextTerm = (Terms) it.next();
            //text.append(nextTerm.getValue());
            Map.Entry pair = (Map.Entry) it.next();
            countDocsLength = countDocsLength + ((Docs) pair.getValue()).getDocLength();
        }
        avdl = countDocsLength / Documents.size();
    }
}
