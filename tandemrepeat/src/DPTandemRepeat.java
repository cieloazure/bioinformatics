import java.util.Arrays;
import java.util.Collections;

public class DPTandemRepeat {
    char [] sequence;
    char pointers [][];
    int matrix [][];
    public DPTandemRepeat(char [] sequence){
        this.sequence = sequence;
        this.pointers = new char [sequence.length][sequence.length];
        this.matrix = new int [sequence.length][sequence.length];
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

            }
            else if (this.pointers[i][j] == 'l'){
                seq_1 = this.sequence[j]+seq_1;
                seq_2 = "-"+ seq_2 ;
                j=j-1;
            }

        }
        System.out.println(seq_1);
        System.out.println(seq_2);
    }
    public static void main(String[] args){
        String seq = "aactaat";

        char [] seq_char = new char[seq.length()+1];
        char [] seq_char_temp = seq.toCharArray();
        seq_char[0] = '1';
        for (int i =1; i<seq_char.length;i++){
            seq_char[i] = seq_char_temp[i-1];
        }
        DPTandemRepeat dptr = new DPTandemRepeat(seq_char);
        dptr.buildMatrix();
        for(int i = 0; i < seq_char.length; i++) {
            for (int j = 0; j < seq_char.length; j++) {
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
        for(int i = 0; i < dptr.sequence.length; i++) {
            for (int j = i; j < dptr.sequence.length; j++) {
                if(dptr.matrix[i][j]>0){
                    dptr.buildTandemRepeats(i,j);
                    System.out.println("**************");
                }

            }

        }

    }


}
