package li.yifei4.data.sequence;

import java.io.IOException;
import java.util.HashMap;

public class SequencePool {
    private static HashMap<String,Sequence> sequences= new HashMap<String,Sequence>();
    public static Sequence get(String seqName){
        return sequences.get(seqName);
    }
    public static void add (Sequence seq){
        sequences.put(seq.getName(),seq);
    }
    public static void Flush() throws IOException {
        for (Sequence value : sequences.values()) {
            value.Flush();
        }
    }
}
