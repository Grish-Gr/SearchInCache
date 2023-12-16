package maksim.ter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Описание алгоритма:
 * 1) Чтение input.txt файла (запись кол-ва запрсов, лимит хранилища кэша и список id)
 * 2) Создам экземпялр класса CacheService, в нём на основе данных из файла о запросах создаём:
 *    - Хэш таблицу, которая служит кэш хранилищем ограниченом по лимиту (ключ - id, значение - ответ от сервера)
 *    - Хэш таблицу, где ключ id, а значение AnalyzedRequest, в котором содержится список
 *      позиций в файле, когда поступает запрос на распределительный сервер
 * 3) Проходимся по идентификаторам из файла:
 *    - Если id найден в кэше, то возвращаем значение
 *    - Если id нет, то анализируем текущий id с id из кэша, проверяя когда будет сделан
 *      следующий запрос на сервер, если запрос будет по позиции похже, чем текущий id, то
 *      добавялем обращение к распр. серверу и перезаписываем id кэша на текущий
 * 4) Записываем результат (количество запросов к распределительной системе) в файл output.txt
 */
public class Main {

    public static void main(String[] args) {
        int countGetRequestInDistributionService = 0;
        Path path = Paths.get("input.txt");
        try {
            List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<Long> identifiers = inputData.stream().skip(1).map(Long::parseLong).toList();
            String[] paramRequest = inputData.get(0).split(" ");
            if (paramRequest.length != 2){
                throw new IllegalArgumentException();
            }
            int limitStorageCache = Integer.parseInt(paramRequest[0]);
            int countRequests = Integer.parseInt(paramRequest[1]);
            if (countRequests > identifiers.size()){
                throw new IllegalArgumentException();
            }

            CacheService cacheService = new CacheService(identifiers,  limitStorageCache);
            for (int position = 0; position < countRequests; position++){
                long currentIdRequest = identifiers.get(position);
                if (cacheService.checkResourceById(currentIdRequest)){
                    cacheService.getResourceById(currentIdRequest);
                } else {
                    cacheService.analyzePutResourceInCache(position, currentIdRequest, null);
                    countGetRequestInDistributionService++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            System.out.println("Ошибка в чтении файла input.txt. Проверьте правильность ввода данных");
        }
        finally {
            try(FileWriter writer = new FileWriter("output.txt", false)) {
                writer.write(String.valueOf(countGetRequestInDistributionService));
                writer.flush();
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }
    }
}