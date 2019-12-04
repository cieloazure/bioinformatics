import java.util.*;

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
                opt.append(" (").append(t.idx).append(",").append(t.length).append(",").append(t.repeats).append("),");
            }
            opt.deleteCharAt(opt.length() - 1);
            opt.append("  ]");
            System.out.println(e.getKey() + " =>  " + opt);
        }

        System.out.println("Execution time: " + this.executionTime + " ms");
    }

    public boolean equalTRs(Result other) {

        if(other.results.size() != this.results.size()) return false;

        for(String key: this.results.keySet()) {
            if(!other.results.containsKey(key)) return false;
        }
        return true;
    }
//    TODO: Merge results in case of suffix tree when we have tandem repeats of length 2
//    public void merge() {
//        class Interval {
//            int startIdx;
//            int endIdx;
//            int length;
//            int repeats;
//            int primitiveLength;
//        }
//
//        for(String key: this.results.keySet()) {
//            Set<TROutput> val1 = this.results.get(key);
//        }
//    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Result)) return false;

        Result other = (Result)obj;
        if(other.results.size() != this.results.size()) return false;

        for(String key: this.results.keySet()) {
            if(!other.results.containsKey(key)) return false;

            Set<TROutput> val1 = this.results.get(key);
            Set<TROutput> val2 = other.results.get(key);

            if(val1.size() != val2.size()) return false;

            if(!val1.containsAll(val2)) return false;
        }

        return true;
    }


}
