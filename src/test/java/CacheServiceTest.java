import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class CacheServiceTest {

    @Test
    public void AlgTest(){
        int countGetRequestInDistributionService = 0;
        List<Long> identifiers = List.of(8L, 2L, 6L, 4L, 2L, 8L, 5L, 1L, 2L, 6L, 6L);
        CacheService cacheService = new CacheService(identifiers, 3);

        for (int position = 0; position < identifiers.size(); position++){
            long id = identifiers.get(position);
            if (cacheService.checkResourceById(id)){
                cacheService.getResourceById(id);
            } else {
                cacheService.analyzePutResourceInCache(position, id, null);
                countGetRequestInDistributionService++;
            }
        }

        Assertions.assertEquals(6, countGetRequestInDistributionService);
    }

    @Test
    public void analyzePutResourceInCacheTest(){
        List<Long> identifiers = List.of(1L, 2L, 3L, 2L, 1L, 2L, 3L, 3L, 3L);
        CacheService cacheService = new CacheService(identifiers, 2);

        for (int position = 0; position < identifiers.size(); position++){
            long currentIdRequest = identifiers.get(position);
            if (cacheService.checkResourceById(currentIdRequest)){
                cacheService.getResourceById(currentIdRequest);
            } else {
                cacheService.analyzePutResourceInCache(position, currentIdRequest, null);
            }
        }

        Assertions.assertTrue(cacheService.checkResourceById(3L));
        Assertions.assertTrue(cacheService.checkResourceById(2L));
        Assertions.assertFalse(cacheService.checkResourceById(1L));
    }

    @Test
    public void limitTimeAlgBigFileTest() throws IOException {
        long startTime = new Date().getTime();
        int countGetRequestInDistributionService = 0;
        Path path = Paths.get("src/test/java/test_input_1.txt");
        List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Long> identifiers = inputData.stream().skip(1).map(Long::parseLong).toList();
        int limitStorageCache = Integer.parseInt(inputData.get(0).split(" ")[0]);
        int countRequests = Integer.parseInt(inputData.get(0).split(" ")[1]);
        CacheService cacheService = new CacheService(identifiers, limitStorageCache);

        for (int position = 0; position < countRequests; position++){
            long currentIdRequest = identifiers.get(position);
            if (cacheService.checkResourceById(currentIdRequest)){
                cacheService.getResourceById(currentIdRequest);
            } else {
                cacheService.analyzePutResourceInCache(position, currentIdRequest, null);
                countGetRequestInDistributionService++;
            }
        }

        long endTime = new Date().getTime();
        Assertions.assertTrue(endTime - startTime < 3000);
        Assertions.assertEquals(50311, countGetRequestInDistributionService);
    }

    @Test
    public void limitMemoryAlgBigFileTest() throws IOException {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        int countGetRequestInDistributionService = 0;
        Path path = Paths.get("src/test/java/test_input_1.txt");
        List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Long> identifiers = inputData.stream().skip(1).map(Long::parseLong).toList();
        int limitStorageCache = Integer.parseInt(inputData.get(0).split(" ")[0]);
        int countRequests = Integer.parseInt(inputData.get(0).split(" ")[1]);
        CacheService cacheService = new CacheService(identifiers, limitStorageCache);

        for (int position = 0; position < countRequests; position++){
            long currentIdRequest = identifiers.get(position);
            if (cacheService.checkResourceById(currentIdRequest)){
                cacheService.getResourceById(currentIdRequest);
            } else {
                cacheService.analyzePutResourceInCache(position, currentIdRequest, null);
                countGetRequestInDistributionService++;
            }
        }
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        Assertions.assertTrue(afterUsedMem - beforeUsedMem < 67108864);
        Assertions.assertEquals(50311, countGetRequestInDistributionService);
    }
}
