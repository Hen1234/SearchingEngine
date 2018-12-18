package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Ranker {


    public Ranker() {
    }

    public void getQueryDocFromSearcher(QueryDoc currentQueryDoc){

        //iterator for the QueryTermsInTheQueryDoc
        Iterator it = currentQueryDoc.getQueryTermsInDocsAndQuery().entrySet().iterator();
        while (it.hasNext()) {
            //Terms nextTerm = (Terms) it.next();
            //text.append(nextTerm.getValue());
            Map.Entry pair = (Map.Entry) it.next();
            QueryTerm currentQueryTerm = (QueryTerm) pair.getKey();


        }

    }


}
