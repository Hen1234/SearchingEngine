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

    ArrayList<QueryTerm> query;
    ArrayList<QueryDoc> docsContainsQueryTerms;
    HashMap<String, Docs> Documents;
    double avdl;



    public Searcher() {
        //this.query = query;
        try {
            FileInputStream f = new FileInputStream(new File(Indexer.pathDir + "\\" + "DocsAsObject.txt"));
            ObjectInputStream o = new ObjectInputStream(f);
            Documents = (HashMap<String, Docs>) o.readObject();
            o.close();

        } catch (Exception e) {}
    }

    public void setQuery(ArrayList<QueryTerm> query) {
        this.query = query;
    }

    private void pasreQuery(String query) throws IOException {

        String queryAfterParse = ReadFile.p.parser(null, query, ReadFile.toStem, true);
        String[] splitedQueryAfterParse = queryAfterParse.split(" ");

        for (int i = 0; i <splitedQueryAfterParse.length ; i++) {
            String curretTermOfQuery = splitedQueryAfterParse[i];

            //check if the term exists the dictionary

            //toLowerCase
            if(Indexer.sorted.containsKey(curretTermOfQuery.toLowerCase())){
                String pointer = Indexer.sorted.get(curretTermOfQuery.toLowerCase());
                String[] numOfFileAndLineOfTerm = pointer.split(",");
                String fileNum = numOfFileAndLineOfTerm[1];
                String lineNum = numOfFileAndLineOfTerm[2];
                Integer lineNumInt = Integer.parseInt(lineNum);
                String lineFromFile="";
                try{
                    //doc:FBIS3-29#2=27066 ,27079 doc:FBIS3-5232#1=481 DF- 2 TIC- 3
                    lineFromFile = Files.readAllLines(Paths.get(Indexer.pathDir +"\\finalposting" + fileNum + ".txt")).get(lineNumInt);
                }
                catch(Exception e){}

                ArrayList<String> docs = new ArrayList<>();
                ArrayList<Integer> amountsPerDoc = new ArrayList<>();
                String docNo = "";
                String tfString = "";
                for (int k = 0; i < lineFromFile.length(); k++) {

                    if (lineFromFile.charAt(i) == ':'){
                        i++;
                        while(lineFromFile.charAt(i)!='#'){
                            docNo = docNo+lineFromFile.charAt(i);
                            i++;
                        }
                        i++;
                        while(lineFromFile.charAt(i)!='='){
                            tfString = tfString+lineFromFile.charAt(i);
                            i++;
                        }

                        docs.add(docNo);
                        int tf = Integer.parseInt(tfString);
                        amountsPerDoc.add(tf);




                    }


                }










        }

        }
    }
    private void initAvd(){
        Integer countDocsLength =0;
        Iterator it = Documents.entrySet().iterator();
        while (it.hasNext()) {
            //Terms nextTerm = (Terms) it.next();
            //text.append(nextTerm.getValue());
            Map.Entry pair = (Map.Entry) it.next();
            countDocsLength =countDocsLength + ((Docs)pair.getValue()).getDocLength();
        }
        avdl = countDocsLength / Documents.size();
    }
}
