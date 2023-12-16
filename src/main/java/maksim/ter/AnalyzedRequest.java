package maksim.ter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalyzedRequest {

    private final long idResource;
    private final List<Integer> positions;

    public AnalyzedRequest(long idResource) {
        this.idResource = idResource;
        this.positions = new ArrayList<>();
    }

    public void addPosition(int position){
        positions.add(position);
    }

    public int getNextPosition(int position){
        int ind = Collections.binarySearch(positions, position + 1);
        ind  = ind < 0 ? -(ind + 1) : ind;
        if (ind >= positions.size()){
            return Integer.MAX_VALUE;
        }
        return positions.get(ind);
    }

    public boolean greaterCountRequest(AnalyzedRequest request, int position){
        return this.getNextPosition(position) < request.getNextPosition(position);
    }

    public long getIdResource() {
        return idResource;
    }
}
