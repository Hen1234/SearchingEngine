package Model;

import java.util.ArrayList;
import java.util.List;

public class QueryTerm {
    ArrayList<String> docs;
    ArrayList<Integer> timesInDocs;
    int appearanceInQuery;

    public QueryTerm() {
        docs = new ArrayList<String>();
        timesInDocs = new ArrayList<Integer>();
        appearanceInQuery =0;
    }
}

