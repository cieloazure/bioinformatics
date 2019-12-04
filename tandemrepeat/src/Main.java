public class Main {
    public static void main(String[] args) {
        int totalLength  = 60;
        String source = TRStringBuilder.build(3, 4, totalLength);

        if(totalLength  <= 60) {
            for (int i = 0; i < source.length(); i++) {
                System.out.printf("%3d", i);
            }
            System.out.println();
            for (int i = 0; i < source.length(); i++) {
                System.out.printf("%3c", source.charAt(i));
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("** TRs SUFFIX TREE **");
        Result r1 = SuffixTreeDemo.run(source);
        r1.printResults();

        System.out.println();
        System.out.println("** TRs DYNAMIC PROGRAMMING **");
        Result r2 = DPTandemRepeatDemo.run(source);
        r2.printResults();

        System.out.printf("\n\n");
        System.out.println("SUFFIX_TREE == DP? " + (r1.equals(r2) ? "YES" : "NO"));
    }
}
