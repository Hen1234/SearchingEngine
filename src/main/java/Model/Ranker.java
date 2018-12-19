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
            currentQueryDoc.setRank(currentQueryDoc.getRank()+BM25func(currentQueryTerm, currentQueryDoc));


        }

    }

    private double BM25func(QueryTerm currentQueryTerm, QueryDoc currentQueryDoc) {

        int cwq = currentQueryTerm.getAppearanceInQuery();
        int cwd = currentQueryTerm.getDocsAndAmount().get(currentQueryDoc.getDocNO());
        int d = currentQueryDoc.getLength();
        int df = currentQueryTerm.getDf();
        double avdl = Searcher.avdl;
        int M = Searcher.numOfDocumentsInCorpus;

        //k=2, B=0.75
        return Math.log10((M+1)/df)*cwq*((3*cwd)/(cwd+(2*(0.25+(0.75*(d/avdl))))));

    }


}
