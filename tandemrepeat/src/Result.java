import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Result {

    private Map<String, HashSet<TROutput>> results = new HashMap<String, HashSet<TROutput>>();
    private long executionTime = 0;
    private String source = "";
    public Result(String s) {
        source = s;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void addResult(TROutput triplet) {
        String sub = source.substring(triplet.idx, triplet.idx + triplet.length);
        if (!results.containsKey(sub)) {
            HashSet<TROutput> val = new HashSet<TROutput>();
            val.add(triplet);
            results.put(sub, val);
        } else {
            results.get(sub).add(triplet);
        }
    }

    public Map<String, HashSet<TROutput>> getResults() {
        return results;
    }

    public void printResults() {
        for (Map.Entry<String, HashSet<TROutput>> e : results.entrySet()) {
            StringBuffer opt = new StringBuffer();
            opt.append("[");
            for (TROutput t : e.getValue()) {
                opt.append(" (" + t.idx + "," + t.length + "," + t.repeats + "),");
            }
            opt.deleteCharAt(opt.length() - 1);
            opt.append("]");
            System.out.println(e.getKey() + " =>  " + opt);
        }

        System.out.println("Execution time: " + this.executionTime + " ms");
    }
}
