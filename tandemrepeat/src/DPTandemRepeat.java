import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class DPTandemRepeat {
    char [] sequence;
    char pointers [][];
    int matrix [][];
    HashMap <String, ArrayList<String>> repeats;

    private DPTandemRepeat(String sequence){
        this.setSequence(sequence);
        this.pointers = new char [this.sequence.length][this.sequence.length];
        this.matrix = new int [this.sequence.length][this.sequence.length];
        this.repeats = new <String, ArrayList<String>>HashMap();
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

            if(this.pointers[i][j] == 'd'){
                seq_1 = this.sequence[j]+seq_1;
                seq_2 = this.sequence[i]+seq_2;
                i=i-1;
                j=j-1;
            }
            else if(this.pointers[i][j] == 'u'){
                seq_1 = "-"+seq_1 ;
                seq_2 = this.sequence[i]+ seq_2 ;
                i=i-1;
                return;

            }
            else if (this.pointers[i][j] == 'l'){
                seq_1 = this.sequence[j]+seq_1;
                seq_2 = "-"+ seq_2 ;
                j=j-1;
                return;
            }

        }
        ArrayList<String> itemsList = this.repeats.get(seq_1);

        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<String>();
            itemsList.add((i+1)+":"+i_str);
            itemsList.add((j+1)+":"+j_str);
            this.repeats.put(seq_1, itemsList);
        } else {
            // add if item is not already in list
            String pos = (i+1)+":"+i_str;
            if(!itemsList.contains(pos)) itemsList.add(pos);
            pos = (j+1)+":"+j_str;
            if(!itemsList.contains(pos)) itemsList.add(pos);

        }

//        System.out.println(seq_1+": Starting at "+(i+1)+ " ending at "+i_str);
//        System.out.println(seq_2+": Starting at "+(j+1)+ " ending at "+j_str);
//        System.out.println("**************");
    }
    public static void main(String[] args){
        String seq = "ATTCGATTCGATTCG";

        DPTandemRepeat dptr = new DPTandemRepeat(seq);
        dptr.buildMatrix();

        for(int i = 0; i < dptr.sequence.length; i++) {
            for (int j = 0; j < dptr.sequence.length; j++) {
                System.out.print(dptr.pointers[i][j]+"\t");
            }
            System.out.println();
        }
        for(int i = 0; i < dptr.sequence.length; i++) {
            for (int j = i; j < dptr.sequence.length; j++) {
                if(dptr.matrix[i][j]>0 && dptr.sequence[i]==dptr.sequence[j]){
                    dptr.buildTandemRepeats(i,j);

                }

            }

        }
    }


}
