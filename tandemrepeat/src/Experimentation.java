public class Experimentation {
    static int seqLenLL = 20;
    static int seqLenUL = 100;
    static int rLenLL = 3;
    static int rLenUL = 10;
    static int rInstancesLL = 2;
    static int rInstancesUL = 10;

    private static TRStringBuilder trsb = new TRStringBuilder();

    public static void main(String[] args) {
        Experimentation.varyingTotalSequenceLength();
        Experimentation.varyingSeqLength();
        Experimentation.varyingSeqInstances();
    }

    private static void varyingTotalSequenceLength() {
        System.out.println(" ********  VARYING TOTAL SEQUENCE LENGTH  ******** ");
        printSeparator('-', 100);
        for(int seqLen = seqLenLL; seqLen <= seqLenUL; seqLen += 10) {
            String source = trsb.build(rLenLL, rInstancesLL, seqLen);
            Result r1 = SuffixTreeDemo.run(source);
            Result r2 = DPTandemRepeatDemo.run(source);
            printParams(rLenLL, rInstancesLL, seqLen);
            r1.printResults();
            printSeparator('-', 100);
            r2.printResults();
            printSeparator('=', 100);
        }
    }

    private static void varyingSeqLength() {
        System.out.println(" ********  VARYING SEQUENCE LENGTH  ******** ");
        printSeparator('-', 100);
        for(int seqLen = rLenLL; seqLen <= rLenUL; seqLen++) {
            String source = trsb.build(seqLen, rInstancesLL, seqLenUL);
            Result r1 = SuffixTreeDemo.run(source);
            Result r2 = DPTandemRepeatDemo.run(source);
            printParams(seqLen, rInstancesLL, seqLenUL);
            printSeparator('-', 100);
            r1.printResults();
            printSeparator('-', 100);
            r2.printResults();
            printSeparator('=', 100);
        }
    }

    private static void varyingSeqInstances() {
        System.out.println(" ********  VARYING SEQUENCE Instances ******** ");
        printSeparator('-', 100);
        for(int seqInst = rInstancesLL; seqInst <= rInstancesUL; seqInst++) {
            String source = trsb.build(rLenLL, seqInst, seqLenUL);
            Result r1 = SuffixTreeDemo.run(source);
            Result r2 = DPTandemRepeatDemo.run(source);
            printParams(rLenLL, seqInst, seqLenUL);
            printSeparator('-', 100);
            r1.printResults();
            printSeparator('-', 100);
            r2.printResults();
            printSeparator('=', 100);
        }
    }

    private static void printParams(int rLen, int rInstances, int totalSeq) {
        System.out.println("[Parameters]: Sequence Length:" + rLen + " | Number of Sequences:" + rInstances + " | Total length of sequences:" + totalSeq);
    }

    private static void printSeparator(char c, int x) {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < x; i++) {
            s.append(c);
        }
        System.out.println(s.toString());
    }
}
