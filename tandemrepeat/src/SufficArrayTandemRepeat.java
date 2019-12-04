import java.util.*;

public class SufficArrayTandemRepeat {
    TreeMap <String, Integer> suffixArr;
    public SufficArrayTandemRepeat(String pattern){
        setSuffixArr(pattern);
    }

    public void setSuffixArr(String sequence) {
        HashMap<String, Integer> temp = new HashMap<>();
        for (int i =0; i<sequence.length();i++){
            temp.put(sequence.substring(sequence.length()-1-i),i);
        }
        this.suffixArr = new TreeMap<String, Integer>(temp);

    }

    public static void main(String[] args){
        SufficArrayTandemRepeat sa = new SufficArrayTandemRepeat("aakash");
        System.out.println("Sorted Map   : " + sa.suffixArr);
        
    }
}
