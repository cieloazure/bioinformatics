import java.util.Random;

public class TRStringBuilder {
    private static final String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String build(int rLength, int rInstances, int totalLength) { //, float errorRate) {
        Random r = new Random();
        int totalTRLen = rLength * rInstances;
        if (totalTRLen > totalLength) {
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
        int trIdx = r.nextInt(totalLength);
        s.replace(trIdx, trIdx + totalTRLen, knownTR);

        return s.toString();
    }

    private static String sequenceGenerator(int rLength, int rInstances) {
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
