package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryTerm {

    String value;
    HashMap<Docs, Integer> docsAndAmount;
    //ArrayList<QueryDoc> DocsContainTerm;

    int df;
    /*ArrayList<String> docs;
    ArrayList<Integer> timesInDocs;*/
    int appearanceInQuery; // number of appearance of this term in query

    public QueryTerm(String value) {
        /*docs = new ArrayList<String>();
        timesInDocs = new ArrayList<Integer>();*/
        appearanceInQuery =0;
        this.value = value;
        docsAndAmount = new HashMap<Docs, Integer>();
    }
}

