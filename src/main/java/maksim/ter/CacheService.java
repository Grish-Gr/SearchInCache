package maksim.ter;

import java.util.HashMap;
import java.util.List;

public class CacheService {

    private final int limitSizeCacheStorage;
    private final HashMap<Long,AnalyzedRequest> analyzedRequests;
    private final HashMap<Long, Object> cacheStorage = new HashMap<>();

    public CacheService(List<Long> identifiers, int limitSizeCacheStorage) {
        this.limitSizeCacheStorage = limitSizeCacheStorage;
        this.analyzedRequests = new HashMap<>();
        for (int position = 0; position < identifiers.size(); position++){
            long currentID = identifiers.get(position);
            if (!analyzedRequests.containsKey(currentID)){
                analyzedRequests.put(currentID, new AnalyzedRequest(currentID));
            }
            analyzedRequests.get(currentID).addPosition(position);
        }
    }

    public boolean checkResourceById(long id){
        return cacheStorage.containsKey(id);
    }

    public Object getResourceById(long id){
        return cacheStorage.get(id);
    }

    public void analyzePutResourceInCache(int position, long idRequest, Object res){
        if (cacheStorage.size() < limitSizeCacheStorage){
            cacheStorage.put(idRequest, res);
            return;
        }
        AnalyzedRequest request = analyzedRequests.get(idRequest);
        AnalyzedRequest replaceRequest = null;
        for (long idInCache: cacheStorage.keySet()){
            AnalyzedRequest analyzedRequestInCache = analyzedRequests.get(idInCache);
            if (replaceRequest != null){
                if (replaceRequest.greaterCountRequest(analyzedRequestInCache, position)){
                    replaceRequest = analyzedRequestInCache;
                }
                continue;
            }
            if (request.greaterCountRequest(analyzedRequestInCache, position)){
                replaceRequest = analyzedRequestInCache;
            }
        }

        if (replaceRequest != null){
            cacheStorage.remove(replaceRequest.getIdResource());
            cacheStorage.put(idRequest, res);
        }
    }
}
