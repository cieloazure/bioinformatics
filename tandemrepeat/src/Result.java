import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result {

    private Map<String, ArrayList<TROutput>> results = new HashMap<String, ArrayList<TROutput>>();
    private String source = "";

    public Result(String s) {
        source = s;
    }

    public void addResult(TROutput triplet) {
        String sub = source.substring(triplet.idx, triplet.idx + triplet.length);
        if (!results.containsKey(sub)) {
            ArrayList<TROutput> val = new ArrayList<TROutput>();
            val.add(triplet);
            results.put(sub, val);
        } else {
            results.get(sub).add(triplet);
        }
    }

    public Map<String, ArrayList<TROutput>> getResults() {
        return results;
    }

    public void printResults() {
        for (Map.Entry<String, ArrayList<TROutput>> e : results.entrySet()) {
            StringBuffer opt = new StringBuffer();
            opt.append("[");
            for (TROutput t : e.getValue()) {
                opt.append(" (" + t.idx + "," + t.length + "," + t.repeats + "),");
            }
            opt.deleteCharAt(opt.length() - 1);
            opt.append("]");
            System.out.println(e.getKey() + " =>  " + opt);
        }
    }
}
