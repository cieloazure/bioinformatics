public class Experimentation {
    int seqLenLL = 20;
    int seqLenUL = 100;
    int rLenLL = 3;
    int rLenUL = 100;
    int rInstancesLL = 2;
    int rInstancesUL = 10;

    public void varyingTotalSequenceLength() {
        for(int seqLen = seqLenLL; seqLen < seqLenUL; seqLen++) {
            String source = TRStringBuilder.build(rLenLL, rInstancesLL, seqLen);
            Result r1 = SuffixTreeDemo.run(source);
            Result r2 = DPTandemRepeatDemo.run(source);
        }
    }
}
