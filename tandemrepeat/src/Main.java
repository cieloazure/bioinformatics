public class Main {
    public static void main(String[] args) {
        String source = TRStringBuilder.build(3, 2, 20);
        for (int i = 0; i < source.length(); i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        for (int i = 0; i < source.length(); i++) {
            System.out.printf("%3c", source.charAt(i));
        }

        System.out.println();
        System.out.println();
        System.out.println("** TRs SUFFIX TREE **");
        SuffixTreeDemo.run(source);

        System.out.println();
        System.out.println("** TRs DYNAMIC PROGRAMMING **");
        DPTandemRepeatDemo.run(source);
    }
}
