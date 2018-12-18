package Model;

import java.util.ArrayList;
import java.util.List;

public class QueryTerm {
    String value;
    ArrayList<QueryDoc> DocsContainTerm;
    /*ArrayList<String> docs;
    ArrayList<Integer> timesInDocs;*/
    int appearanceInQuery;

    public QueryTerm(String value) {
        /*docs = new ArrayList<String>();
        timesInDocs = new ArrayList<Integer>();*/
        appearanceInQuery =0;
        this.value = value;
    }
}

