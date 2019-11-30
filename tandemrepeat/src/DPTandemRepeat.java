import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class DPTandemRepeat {
    char [] sequence;
    char pointers [][];
    int matrix [][];
    long executionTime;
    HashMap <String, ArrayList<Pair>> repeats;

    private DPTandemRepeat(String sequence){
        this.setSequence(sequence);
        this.pointers = new char [this.sequence.length][this.sequence.length];
        this.matrix = new int [this.sequence.length][this.sequence.length];
        this.repeats = new <String, ArrayList<Pair>>HashMap();
        this.executionTime = 0;
    }
    private static class Triplets{
        int strtIndex;
        int length;
        int numRepeats;
        String seq;
        public Triplets(int strt, int len, int nr, String seq){
            this.seq = seq;
            this.strtIndex=strt;
            this.length=len;
            this.numRepeats = nr;
        }
    }
    private void setSequence(String sequence) {
        char [] seq_char = new char[sequence.length()+1];
        char [] seq_char_temp = sequence.toCharArray();
        seq_char[0] = '1';
        for (int i =1; i<seq_char.length;i++){
            seq_char[i] = seq_char_temp[i-1];
        }
        this.sequence = seq_char;
    }

    public void buildMatrix(){


        for(int i = 0; i < this.sequence.length; i++){
            for (int j =i; j<this.sequence.length; j++){
                if (i==0 || i==j){
                    this.matrix[i][j] = 0;
                    this.pointers[i][j] = 's';
                }
                else{
                    this.matrix[i][j] = -1;
                }
            }
        }

        for(int i = 1; i < this.sequence.length; i++) {
            for (int j = i; j < this.sequence.length; j++) {
                if (i==j){
                    continue;
                }
                int temp [] = {0,0,0,0};
                temp[0] = this.matrix[i-1][j-1] + this.compare(this.sequence[i] , this.sequence[j]);
                temp[1] = this.matrix[i-1][j] + this.compare(this.sequence[i], '-');
                temp[2] = this.matrix[i][j-1] + this.compare('-', this.sequence[i]);
                int [] max = this.get_max(temp);
                this.matrix[i][j] = max[0];
                if (max[0] == 0){
                    this.pointers[i][j] = 's';
                }
                else{
                    if (max[1] ==  0){
                        this.pointers[i][j] = 'd';
                    }
                    else if (max[1] ==  1){
                        this.pointers[i][j] = 'u';
                    }
                    else if (max[1] ==  2){
                        this.pointers[i][j] = 'l';
                    }
                }

            }

        }



    }
    public int compare (char a, char b){
        if (a==b){
            return 1;
        }
        else{
            return -1;
        }
    }
    public int [] get_max(int [] input){
        int op [] = {input[0],0};
        for(int i =0; i<input.length; i++){
            if (input[i] > op[0]){
                op[0] = input[i];
                op[1] = i;
            }
        }
        return op;
    }
    public void buildTandemRepeats(int i, int j){

        String seq_1= "";
        String seq_2= "";
        int i_str = i;
        int j_str = j;
        while(this.pointers[i][j] != 's'){
            if (this.sequence[j]!= this.sequence[i]){
                    break;
            }
            if(this.pointers[i][j] == 'd'){
                seq_1 = this.sequence[j]+seq_1;
                seq_2 = this.sequence[i]+seq_2;
                i=i-1;
                j=j-1;
            }
            else{
                break;
            }

        }
        if (!seq_1.equals(seq_2)){
            return;
        }

        ArrayList<Pair> itemsList = this.repeats.get(seq_1);

        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<Pair>();

            if (j_str == i){
                Pair seq_1_p= new Pair(j, i_str);
                itemsList.add(seq_1_p);
            }
            else if (i_str==j) {
                Pair seq_1_p = new Pair(i, j_str);
                itemsList.add(seq_1_p);
            }
            else{
                Pair seq_1_p = new Pair(i, i_str );
                Pair seq_2_p = new Pair(j,j_str);
                itemsList.add(seq_1_p);
                itemsList.add(seq_2_p);
            }

            this.repeats.put(seq_1, itemsList);

        } else {
            Pair<Integer, Integer> seq_1_p = new Pair(i, i_str);
            Pair <Integer, Integer> seq_2_p = new Pair(j, j_str);
            Boolean seq_1_add= true;
            Boolean seq_2_add= true;
            for(int k = 0; k < itemsList.size(); k++)
            {
                Pair <Integer, Integer>temp =  itemsList.get(k);
                if (temp.getKey()-seq_1_p.getValue() ==0){
                    Pair new_pair = new Pair(seq_1_p.getKey(), temp.getValue());
                    if(!itemsList.contains(new_pair)) {
                        itemsList.add(new_pair);
                    }

                    itemsList.remove(temp);
                    seq_1_add = false;
                }
                else if(seq_1_p.getKey()-temp.getValue() ==0){
                    Pair new_pair = new Pair(temp.getKey(), seq_1_p.getValue());
                    if(!itemsList.contains(new_pair)) {
                        itemsList.add(new_pair);
                    }
                    itemsList.remove(temp);
                    seq_1_add = false;
                }

            }

            for(int k = 0; k < itemsList.size(); k++)
            {
                Pair <Integer, Integer>temp =  itemsList.get(k);
                if (temp.getKey()-seq_2_p.getValue()==0){
                    Pair new_pair = new Pair(seq_2_p.getKey(), temp.getValue());
                    if(!itemsList.contains(new_pair)) {
                        itemsList.add(new_pair);
                    }
                    itemsList.remove(temp);
                    seq_2_add = false;
                }
                else if(seq_2_p.getKey()-temp.getValue() ==0){
                    Pair new_pair = new Pair(temp.getKey(), seq_2_p.getValue());
                    if(!itemsList.contains(new_pair)) {
                        itemsList.add(new_pair);
                    }
                    itemsList.remove(temp);
                    seq_2_add = false;
                }
            }
            if (seq_1_add){
                if(!itemsList.contains(seq_1_p)) {
                    itemsList.add(seq_1_p);
                }
            }
            if (seq_2_add){
                if(!itemsList.contains(seq_2_p)){
                    itemsList.add(seq_2_p);
                }
            }

        }
//        System.out.println(this.repeats);
//        System.out.println(seq_1+": Starting at "+(i+1)+ " ending at "+i_str);
//        System.out.println(seq_2+": Starting at "+(j+1)+ " ending at "+j_str);
//        System.out.println("**************");
    }
    public static void main(String[] args){

//        String seq = "AACTAAT";
        String seq = "AEAEAKAKAEAEAKAK";

        long startTime = System.currentTimeMillis();
        DPTandemRepeat dptr = new DPTandemRepeat(seq);
        dptr.buildMatrix();


        for(int i = 1; i < dptr.sequence.length; i++) {
            for (int j = i; j < dptr.sequence.length; j++) {
                if(dptr.matrix[i][j]>0 && dptr.sequence[i]==dptr.sequence[j]){
                    dptr.buildTandemRepeats(i,j);

                }

            }

        }
        for(int i = 0; i < dptr.sequence.length; i++) {
            for (int j = 0; j < dptr.sequence.length; j++) {
                System.out.print(dptr.matrix[i][j]+"\t");
                }
                System.out.println();

            }
        for(int i = 0; i < dptr.sequence.length; i++) {
            for (int j = 0; j < dptr.sequence.length; j++) {
                System.out.print(dptr.pointers[i][j]+"\t");
            }
            System.out.println();

        }

        ArrayList <String>keys_to_remove = new ArrayList();
        System.out.println(dptr.repeats);
        for (String sequence: dptr.repeats.keySet()){
            ArrayList<Pair> itemsList = dptr.repeats.get(sequence);
            ArrayList<Pair> itemsList_copy = new ArrayList<>();
            itemsList_copy.addAll(itemsList);
            for(int k = 0; k < itemsList_copy.size(); k++) {
                Pair<Integer, Integer> temp = itemsList_copy.get(k);
                if (sequence.length() == (temp.getValue() - temp.getKey())) {
                    itemsList.remove(temp);
                }
            }
            if (itemsList.size()==0){
                keys_to_remove.add(sequence);
            }
        }

        for(int k = 0; k < keys_to_remove.size(); k++) {
            dptr.repeats.remove(keys_to_remove.get(k));
        }
        List <Triplets> op = new ArrayList<>();
        for (String sequence: dptr.repeats.keySet()){
            ArrayList<Pair> itemsList = dptr.repeats.get(sequence);
            for(int k = 0; k < itemsList.size(); k++){
                Pair<Integer, Integer> temp = itemsList.get(k);
                int len_t = temp.getValue()-temp.getKey();
                int nr = len_t/sequence.length();
                Triplets t = new  Triplets(temp.getKey(),len_t,nr, sequence);
                op.add(t);
                System.out.println(sequence+": "+"Start index: "+temp.getKey()+"\tLength: "+len_t+"\tNumber of repeats: "+nr);

            }

        }
        System.out.println(dptr.repeats);
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        dptr.executionTime = timeElapsed;
        System.out.println("Execution time "+ dptr.executionTime+" milliseconds" );
//        System.out.println(dptr.repeats);
//        System.out.println(op);
    }

}
