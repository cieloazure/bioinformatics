import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DPTandemRepeatDemo {

    public static Result run(String seq) {
        long startTime = System.currentTimeMillis();
        Result r = new Result(seq);
        DPTandemRepeat dptr = new DPTandemRepeat(seq);
        dptr.buildMatrix();


        for (int i = 1; i < dptr.sequence.length; i++) {
            for (int j = i; j < dptr.sequence.length; j++) {
                if (dptr.matrix[i][j] > 0 && dptr.sequence[i] == dptr.sequence[j]) {
                    dptr.buildTandemRepeats(i, j);
                }
            }

        }

        // Duplicate removal
        ArrayList<String> keys_to_remove = new ArrayList<>();
        for (String sequence : dptr.repeats.keySet()) {
            ArrayList<Pair<Integer, Integer>> itemsList = dptr.repeats.get(sequence);
            ArrayList<Pair<Integer, Integer>> itemsList_copy = new ArrayList<Pair<Integer, Integer>>();
            itemsList_copy.addAll(itemsList);
            for (int k = 0; k < itemsList_copy.size(); k++) {
                Pair<Integer, Integer> temp = itemsList_copy.get(k);
                if (sequence.length() == (temp.getValue() - temp.getKey())) {
                    itemsList.remove(temp);
                }
            }
            if (itemsList.size() == 0) {
                keys_to_remove.add(sequence);
            }
        }

        for (int k = 0; k < keys_to_remove.size(); k++) {
            dptr.repeats.remove(keys_to_remove.get(k));
        }

        // Construct output
        List<TROutput> op = new ArrayList<>();
        for (String sequence : dptr.repeats.keySet()) {
            ArrayList<Pair<Integer, Integer>> itemsList = dptr.repeats.get(sequence);
            for (int k = 0; k < itemsList.size(); k++) {
                Pair<Integer, Integer> temp = itemsList.get(k);
                int len_t = temp.getValue() - temp.getKey();
                int nr = len_t / sequence.length();
                TROutput t = new TROutput(temp.getKey(), len_t, nr);
                op.add(t);
            }
        }

        for (TROutput tr : op) {
            r.addResult(tr);
        }

        long endTime = System.currentTimeMillis();
        dptr.executionTime = endTime - startTime;
        r.setExecutionTime(endTime - startTime);
        return r;
    }

    private static class DPTandemRepeat {
        char[] sequence;
        char[][] pointers;
        int[][] matrix;
        long executionTime;
        HashMap<String, ArrayList<Pair<Integer, Integer>>> repeats;

        private DPTandemRepeat(String sequence) {
            this.setSequence(sequence);
            this.pointers = new char[this.sequence.length][this.sequence.length];
            this.matrix = new int[this.sequence.length][this.sequence.length];
            this.repeats = new <String, ArrayList<Pair>>HashMap<String, ArrayList<Pair<Integer, Integer>>>();
            this.executionTime = 0;
        }

        private void setSequence(String sequence) {
            char[] seq_char = new char[sequence.length() + 1];
            char[] seq_char_temp = sequence.toCharArray();
            seq_char[0] = '1';
            for (int i = 1; i < seq_char.length; i++) {
                seq_char[i] = seq_char_temp[i - 1];
            }
            this.sequence = seq_char;
        }

        public void buildMatrix() {
            for (int i = 0; i < this.sequence.length; i++) {
                for (int j = i; j < this.sequence.length; j++) {
                    if (i == 0 || i == j) {
                        this.matrix[i][j] = 0;
                        this.pointers[i][j] = 's';
                    } else {
                        this.matrix[i][j] = -1;
                    }
                }
            }

            for (int i = 1; i < this.sequence.length; i++) {
                for (int j = i; j < this.sequence.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    int[] temp = {0, 0, 0, 0};
                    temp[0] = this.matrix[i - 1][j - 1] + this.compare(this.sequence[i], this.sequence[j]);
                    temp[1] = this.matrix[i - 1][j] + this.compare(this.sequence[i], '-');
                    temp[2] = this.matrix[i][j - 1] + this.compare('-', this.sequence[i]);
                    int[] max = this.get_max(temp);
                    this.matrix[i][j] = max[0];
                    if (max[0] == 0) {
                        this.pointers[i][j] = 's';
                    } else {
                        if (max[1] == 0) {
                            this.pointers[i][j] = 'd';
                        } else if (max[1] == 1) {
                            this.pointers[i][j] = 'u';
                        } else if (max[1] == 2) {
                            this.pointers[i][j] = 'l';
                        }
                    }

                }

            }


        }

        public int compare(char a, char b) {
            if (a == b) {
                return 1;
            } else {
                return -1;
            }
        }

        public int[] get_max(int[] input) {
            int[] op = {input[0], 0};
            for (int i = 0; i < input.length; i++) {
                if (input[i] > op[0]) {
                    op[0] = input[i];
                    op[1] = i;
                }
            }
            return op;
        }

        public void buildTandemRepeats(int i, int j) {

            String seq_1 = "";
            String seq_2 = "";
            int i_str = i;
            int j_str = j;
            while (this.pointers[i][j] != 's') {
                if (this.sequence[j] != this.sequence[i]) {
                    break;
                }
                if (this.pointers[i][j] == 'd') {
                    seq_1 = this.sequence[j] + seq_1;
                    seq_2 = this.sequence[i] + seq_2;
                    i = i - 1;
                    j = j - 1;
                } else {
                    break;
                }

            }
            if (!seq_1.equals(seq_2)) {
                return;
            }

            ArrayList<Pair<Integer, Integer>> itemsList = this.repeats.get(seq_1);

            // if list does not exist create it
            if (itemsList == null) {
                itemsList = new ArrayList<>();

                if (j_str == i) {
                    Pair<Integer, Integer> seq_1_p = new Pair<>(j, i_str);
                    itemsList.add(seq_1_p);
                } else if (i_str == j) {
                    Pair<Integer, Integer> seq_1_p = new Pair<>(i, j_str);
                    itemsList.add(seq_1_p);
                } else {
                    Pair<Integer, Integer> seq_1_p = new Pair<>(i, i_str);
                    Pair<Integer, Integer> seq_2_p = new Pair<>(j, j_str);
                    itemsList.add(seq_1_p);
                    itemsList.add(seq_2_p);
                }

                this.repeats.put(seq_1, itemsList);

            } else {
                Pair<Integer, Integer> seq_1_p = new Pair<>(i, i_str);
                Pair<Integer, Integer> seq_2_p = new Pair<>(j, j_str);
                boolean seq_1_add = true;
                boolean seq_2_add = true;
                for (int k = 0; k < itemsList.size(); k++) {
                    Pair<Integer, Integer> temp = itemsList.get(k);
                    if (temp.getKey() - seq_1_p.getValue() == 0) {
                        Pair<Integer, Integer> new_pair = new Pair<>(seq_1_p.getKey(), temp.getValue());
                        if (!itemsList.contains(new_pair)) {
                            itemsList.add(new_pair);
                        }

                        itemsList.remove(temp);
                        seq_1_add = false;
                    } else if (seq_1_p.getKey() - temp.getValue() == 0) {
                        Pair<Integer, Integer> new_pair = new Pair<>(temp.getKey(), seq_1_p.getValue());
                        if (!itemsList.contains(new_pair)) {
                            itemsList.add(new_pair);
                        }
                        itemsList.remove(temp);
                        seq_1_add = false;
                    }

                }

                for (int k = 0; k < itemsList.size(); k++) {
                    Pair<Integer, Integer> temp = itemsList.get(k);
                    if (temp.getKey() - seq_2_p.getValue() == 0) {
                        Pair<Integer, Integer> new_pair = new Pair<>(seq_2_p.getKey(), temp.getValue());
                        if (!itemsList.contains(new_pair)) {
                            itemsList.add(new_pair);
                        }
                        itemsList.remove(temp);
                        seq_2_add = false;
                    } else if (seq_2_p.getKey() - temp.getValue() == 0) {
                        Pair<Integer, Integer> new_pair = new Pair<>(temp.getKey(), seq_2_p.getValue());
                        if (!itemsList.contains(new_pair)) {
                            itemsList.add(new_pair);
                        }
                        itemsList.remove(temp);
                        seq_2_add = false;
                    }
                }
                if (seq_1_add) {
                    if (!itemsList.contains(seq_1_p)) {
                        itemsList.add(seq_1_p);
                    }
                }
                if (seq_2_add) {
                    if (!itemsList.contains(seq_2_p)) {
                        itemsList.add(seq_2_p);
                    }
                }

            }
        }

    }


}
