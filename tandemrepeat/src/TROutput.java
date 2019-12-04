import java.util.HashSet;
import java.util.Set;

class TROutput {
    int idx = -1;
    int length = -1;
    int repeats = -1;

    public TROutput() {
    }

    public TROutput(int idx, int length, int repeats) {
        this.idx = idx;
        this.length = length;
        this.repeats = repeats;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof TROutput)) return false;

        TROutput other = (TROutput)obj;
        return other.idx == this.idx && other.length == this.length && other.repeats == this.repeats;
    }

    @Override
    public int hashCode() {
        int result = (int) (idx ^ (idx >>> 32));
        result = 31 * result + (int) (length ^ (length >>> 32));
        result = 31 * result + (int) (repeats ^ (repeats >>> 32));
        return result;
    }

}
