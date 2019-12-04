import java.util.Random;

public class TRStringBuilder {
    private static final String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String source = "";
    private int repeatStartIdx = -1;
    private int repeatLength = -1;

    public String build(int rLength, int rInstances, int totalLength) { //, float errorRate) {
        Random r = new Random();
        int totalTRLen = rLength * rInstances;
        if (totalTRLen >= totalLength) {
            return "";
        }

        // Generate random string
        StringBuilder s = new StringBuilder();
        while (s.length() < totalLength) {
            int randInt = r.nextInt(26);
            s.append(characterSet.charAt(randInt));
        }

        // Inject known tandem repeat
        String knownTR = sequenceGenerator(rLength, rInstances);
        int trIdx = r.nextInt(totalLength - totalTRLen);
        s.replace(trIdx, trIdx + totalTRLen, knownTR);

        this.source = s.toString();
        this.repeatStartIdx = trIdx;
        this.repeatLength = rLength;
        return s.toString();
    }

//    Place tandem repeats of rLength of rInstances in the string
//    Finding non-overlapping areas of the string
//    Deciding whether such an area exists
//    public static String build(List<Integer> rLengths,List<Integer> rInstances, int totalLength) {
//    }

    private String sequenceGenerator(int rLength, int rInstances) {
        Random r = new Random();
        StringBuilder s = new StringBuilder();
        while (s.length() < rLength) {
            int randInt = r.nextInt(26);
            s.append(characterSet.charAt(randInt));
        }
        int currInstances = 0;
        StringBuilder trs = new StringBuilder();
        while (currInstances < rInstances) {
            trs.append(s);
            currInstances++;
        }

        return trs.toString();
    }

}
