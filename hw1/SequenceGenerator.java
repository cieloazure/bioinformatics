import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class SequenceGenerator{
    private int aCount;
    private int cCount;
    private int gCount;
    private int tCount;
    private int seqLen;
    private int numSeq;
    private float mutateProbability;
    private int aIndex;
    private int cIndex;
    private int gIndex;
    private int tIndex;
    private String outputFileName;
    private SequenceGenerator(int a_count,
                             int c_count,
                             int g_count,
                             int t_count,
                             int seq_len,
                             int num_seq,
                             float mut_prob,
                              String outputFileName) {
        this.aCount = a_count;
        this.cCount = c_count;
        this.gCount = g_count;
        this.tCount = t_count;
        this.seqLen = seq_len;
        this.numSeq = num_seq;
        this.mutateProbability = mut_prob;
        this.aIndex = 0;
        this.cIndex = 0;
        this.gIndex = 0;
        this.tIndex = 0;
        this.setIndices();
        this.outputFileName = outputFileName;


    }
    private void setIndices(){
        float sumCount = this.aCount + this.cCount + this.gCount + this.tCount;
        this.aIndex = (int)((this.aCount /sumCount) * 100);
        this.cIndex = (int)((this.cCount /sumCount) * 100);
        this.cIndex = this.cIndex + this.aIndex;
        this.gIndex = (int)((this.gCount /sumCount) * 100);
        this.gIndex = this.cIndex + this.gIndex;
        this.tIndex = (int)((this.tCount /sumCount) * 100);
        this.tIndex = this.tIndex + this.gIndex;
    }
    public static void main(String[] args){

        int seq_len = Integer.parseInt(args[0]);
        int a_count = Integer.parseInt(args[1]);
        int c_count = Integer.parseInt(args[2]);
        int g_count = Integer.parseInt(args[3]);
        int t_count = Integer.parseInt(args[4]);
        int num_seq = Integer.parseInt(args[5]);
        float mut_prob = Float.parseFloat(args[6]);

        String outputFileName = args[7];


        SequenceGenerator sg = new SequenceGenerator(a_count, c_count, g_count, t_count,
                                                    seq_len, num_seq, mut_prob, outputFileName);

        // Generate the first sequence
        String seq1 = sg.get_first_seq();

        // Generate each of the rest of the sequences based on sequence 1
        StringBuilder seqBuilder = new StringBuilder();
        for (int i = 1; i< sg.numSeq; i++){
            String newSeq = sg.getNewSequence(sg.getNewSequence(seq1));
            newSeq = sg.convertToFastaFormat(newSeq, i) + "\n";
            seqBuilder.append(newSeq);
        }

        // Convert first sequence to FASTA format and prepend to other sequences
        seq1 = sg.convertToFastaFormat(seq1, 0);
        String fullSeq = seq1 + seqBuilder.toString();

        // Write the full sequence to the outputfile
        sg.writeSeqToOutputFile(fullSeq);
    }

    /***
     * This method returns the first sequence of the k sequences
     * @return The first of the k sequences.
     */
    private String get_first_seq(){

        Random r = new Random();
        StringBuilder seqBuilder = new StringBuilder();
        for(int i = 0;i< this.seqLen; i++){
            int ind = r.nextInt(100);
            seqBuilder.append(this.getDNAMolecule(ind));
        }

        return seqBuilder.toString();
    }

    /***
     * Based on the first sequence we generate more sequences
     * @param firstSeq
     * @return New sequence
     */
    private String getNewSequence(String firstSeq){
        StringBuilder seqBuilder = new StringBuilder();
        char [] seedSeq = firstSeq.toCharArray();
        double decision;
        int coinToss;
        Random rn = new Random();
        for (int i = 0; i< seedSeq.length; i++){
            decision = Math.random();

            if (decision <= this.mutateProbability){
                //  Mutate or Delete
                coinToss = rn.nextInt(2);
                if (coinToss==1){
                    //  Mutate
                    int ind = rn.nextInt(100);
                    seqBuilder.append(this.getDNAMolecule(ind));
                }
                // else we are supposed to delete the current molecule
            }
            else{
                // We neither mutate nor delete in this case
                seqBuilder.append(seedSeq[i]);
            }
        }
        return seqBuilder.toString();
    }

    /***
     * Convert the given sequence to FASTA format
     * @param sequence
     * @return
     */
    private String convertToFastaFormat(String sequence, int index){
        String seq = "> Sequence_number_" + index+ "| length "+sequence.length()+"\n" ;
        StringBuilder sb = new StringBuilder(seq);
        char seqArr [] = sequence.toCharArray();
        for(int i =0; i<seqArr.length; i++){
            sb.append(seqArr[i]);
            if ((i+1)%80 == 0){
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    /***
     * Write sequence to output file
     * @param sequence
     */
    private void writeSeqToOutputFile(String sequence){
        try {
            PrintWriter out = new PrintWriter(this.outputFileName);
            out.write(sequence);
            out.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Couldn't write to file");
        }



    }

    /***
     * Get the DNA molecule based on the index
     * @param index
     * @return
     */
    private char getDNAMolecule(int index){
        if (index < this.aIndex){
            return('a');
        }
        else if (index >= this.aIndex && index < this.cIndex){
            return('c');
        }
        else if (index >= this.cIndex && index < this.gIndex){
            return('g');
        }
        else {
            return('t');
        }
    }
}
