import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuffixTreeDemo {
    public static void run(String source) {
        if (!source.endsWith("$")) {
            source += '$';
        }
        long startTime = System.currentTimeMillis();
        SuffixTree s = new SuffixTree(source);
//        s.visualize();

        // Get Branching TRs
        List<TROutput> l1 = s.branchingTRs();


        Result r = new Result(source);

        for (TROutput triplet : l1) {
            r.addResult(triplet);
        }

        // Get Non-Branching TRs
        List<TROutput> l2 = s.nonBranchingTRs(l1);
        for (TROutput triplet : l2) {
            r.addResult(triplet);
        }

        r.printResults();

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time:" + timeElapsed + " ms");
    }

    private static class Node {
        String sub = "";                       // a substring of the input string
        List<Integer> ch = new ArrayList<>();  // list of child nodes
        boolean mark = false;
        int idx = -1;
        boolean visited = false;
        List<Integer> dfs = new ArrayList<>();
        int depth = -1;
    }

    private static class SuffixTree {
        private List<Node> nodes = new ArrayList<>();
        private int next = 0;
        private int recent = 0;
        private Map<Integer, Integer> idxToDfs = new HashMap<>();
        private Map<Integer, Integer> dfsToIdx = new HashMap<>();
        private String source = "";

        public SuffixTree(String str) {
            //  Create suffix tree
            source = str;
            nodes.add(new Node());
            for (int i = 0; i < str.length(); ++i) {
                addSuffix(str.substring(i), i);
            }

            // Traverse the tree in dfs -> assign dfs numbers and calculate
            // depth of each node
            next = 0;
            recent = 0;
            dfs();
        }

        private void addSuffix(String suf, int idx) {
            int n = 0;
            int i = 0;
            while (i < suf.length()) {
                char b = suf.charAt(i);
                List<Integer> children = nodes.get(n).ch;
                int x2 = 0;
                int n2;
                while (true) {
                    if (x2 == children.size()) {
                        // no matching child, remainder of suf becomes new node.
                        n2 = nodes.size();
                        Node temp = new Node();
                        temp.sub = suf.substring(i);
                        temp.idx = idx;
                        nodes.add(temp);
                        children.add(n2);
                        return;
                    }
                    n2 = children.get(x2);
                    if (nodes.get(n2).sub.charAt(0) == b) break;
                    x2++;
                }
                // find prefix of remaining suffix in common with child
                String sub2 = nodes.get(n2).sub;
                int j = 0;
                while (j < sub2.length()) {
                    if (suf.charAt(i + j) != sub2.charAt(j)) {
                        // split n2
                        int n3 = n2;
                        // new node for the part in common
                        n2 = nodes.size();
                        Node temp = new Node();
                        temp.sub = sub2.substring(0, j);
                        temp.ch.add(n3);
                        nodes.add(temp);
                        nodes.get(n3).sub = sub2.substring(j);  // old node loses the part in common
                        nodes.get(n).ch.set(x2, n2);
                        nodes.get(n).idx = idx;
                        break;  // continue down the tree
                    }
                    j++;
                }
                i += j;  // advance past part in common
                n = n2;  // continue down the tree
            }
        }

        public void visualize() {
            if (nodes.isEmpty()) {
                System.out.println("<empty>");
                return;
            }
            visualize_f(0, "");
        }

        private void visualize_f(int n, String pre) {
            List<Integer> children = nodes.get(n).ch;
            if (children.isEmpty()) {
                System.out.println("- " + nodes.get(n).sub);
                return;
            }
            System.out.println("┐ " + nodes.get(n).sub);
            for (int i = 0; i < children.size() - 1; i++) {
                Integer c = children.get(i);
                System.out.print(pre + "├─");
                visualize_f(c, pre + "│ ");
            }
            System.out.print(pre + "└─");
            visualize_f(children.get(children.size() - 1), pre + "  ");
        }

        public void dfs() {
            for (int i = 0; i < nodes.size(); i++) {
                if (!nodes.get(i).visited) {
                    dfsVisit(nodes.get(i), 0);
                }
            }
        }

        private void dfsVisit(Node u, int pred_depth) {
            u.dfs.add(next);
            u.depth = pred_depth + u.sub.length();
            if (u.ch.size() == 0) {
                // leaf
                recent = next;
                idxToDfs.put(u.idx, next);
                dfsToIdx.put(next, u.idx);
                next++;
            }

            for (int i = 0; i < u.ch.size(); i++) {
                if (!nodes.get(u.ch.get(i)).visited) {
                    dfsVisit(nodes.get(u.ch.get(i)), pred_depth + u.sub.length());
                }
            }

            u.visited = true;
            u.dfs.add(recent);
        }

        public List<TROutput> branchingTRs() {
            List<TROutput> branchTRs = new ArrayList<>();
            if (nodes.isEmpty()) {
                return branchTRs;
            }
            branchingTRsRec(0, branchTRs);
            return branchTRs;
        }

        private List<TROutput> nonBranchingTRs(List<TROutput> branchingTRs) {
            // Rotations Procedure:
            // Starting with an occurrence (i; wa; 2) of a branching tandem repeat, test if S[i−1] = a.
            // If they are equal, (i − 1; aw; 2) is reported as a non-branching tandem repeat. This
            // process, called the rotation procedure, is continued to the left until an inequality is
            // observed, at which point the procedure stops.
            List<TROutput> nonBranchingTRs = new ArrayList<>();
            for (TROutput tr : branchingTRs) {
                if(tr.length > 2) {
                    int start1 = tr.idx - 1;
                    int start2 = start1 + tr.length + 1;
                    while (start1 >= 0 && source.charAt(start1) == source.charAt(start2)) {
                        TROutput new_tr = new TROutput();
                        new_tr.idx = start1;
                        new_tr.length = tr.length;
                        new_tr.repeats = 2;
                        nonBranchingTRs.add(new_tr);

                        start1--;
                        start2--;
                    }
                }
            }

            return nonBranchingTRs;
        }

        private void branchingTRsRec(int n, List<TROutput> acc) {
            Node v = nodes.get(n);
            List<Integer> children = v.ch;

            // Checking only internal nodes
            if (children.isEmpty()) {
                return;
            }

            // If vertex is already marked don't proceed
            if (v.mark) {
                return;
            }

            if (n > 0) {
                // Optimized Algorithm
                // 1.Select an unmarked internal node(except root) v. Mark v and execute steps 2a and 2b for
                //node v.
                v.mark = true;

                // 2a. Collect the leaf-list, LL'(v), of v.
                // where LL'(v) = LL(v) - LL(v')
                // where v' = child with largest leaf list

                // find v', child with largest leaf list
                Node max = null;
                int maxSize = -1;
                for (int i = 0; i < children.size(); i++) {
                    Node c = nodes.get(children.get(i));
                    int currSize = c.dfs.get(1) - c.dfs.get(0) + 1;
                    if (currSize > maxSize) {
                        max = c;
                        maxSize = currSize;
                    }
                }

                // find LL'(v)
                List<Integer> leafListRedux = new ArrayList<>();
                for (int i = v.dfs.get(0); i <= v.dfs.get(1); i++) {
                    if (i < max.dfs.get(0) || i > max.dfs.get(1)) {
                        leafListRedux.add(dfsToIdx.get(i));
                    }
                }


                // 2b.For each leaf-label i in LL(v), test whether leaf-label j = i + D(v) is in LL(v). If
                //so, test whether S[i] != S[i + 2D(v)]
                for (int i : leafListRedux) {
                    int j = i + v.depth;
                    Integer dfsId = idxToDfs.get(j);
                    if (dfsId != null) {
                        // check whether leaf-label j is in LL(v)
                        if (dfsId >= v.dfs.get(0) && dfsId <= v.dfs.get(1)) {
                            int next_i = i + (2 * v.depth);
                            if (next_i < source.length() && source.charAt(i) != source.charAt(next_i)) {
                                // branching tandem repeat found
                                TROutput o = new TROutput();
                                o.idx = i;
                                o.length = 2 * v.depth;
                                o.repeats = 2;
                                acc.add(o);
                            }
                        }
                    }
                }

                //For each leaf-label j in LL(v), test whether leaf-label i = j − D(v) is in LL(v). If
                //so, test whether S[i] != S[i + 2D(v)]. There is a branching tandem repeat of length
                //2D(v) starting at that position i if and only if both tests return true
                for (int j : leafListRedux) {
                    int i = j - v.depth;
                    Integer dfsId = idxToDfs.get(i);
                    if (dfsId != null) {
                        // check whether leaf-label j is in LL(v)
                        if (dfsId >= v.dfs.get(0) && dfsId <= v.dfs.get(1)) {
                            int next_i = i + (2 * v.depth);
                            if (next_i < source.length() && source.charAt(i) != source.charAt(next_i)) {
                                // branching tandem repeat found
                                TROutput o = new TROutput();
                                o.idx = i;
                                o.length = 2 * v.depth;
                                o.repeats = 2;
                                acc.add(o);
                            }
                        }
                    }
                }
            }


            for (int i = 0; i < children.size(); i++) {
                Integer c = children.get(i);
                branchingTRsRec(c, acc);
            }
        }
    }

}

