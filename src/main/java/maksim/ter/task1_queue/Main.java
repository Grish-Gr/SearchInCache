package maksim.ter.task1_queue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.collections4.queue.CircularFifoQueue;


public class Main {

    private static CircularFifoQueue<Long> queue = new CircularFifoQueue<>(10);

    public static void main(String[] args) {
        int countGetRequestInPhysicMemory = 0;
        Path path = Paths.get("input.txt");
        try {
            List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<Long> identifiers = inputData.stream().skip(1).map(Long::parseLong).toList();
            String[] paramRequest = inputData.get(0).split(" ");
            if (paramRequest.length != 2){
                throw new IllegalArgumentException();
            }

            int countRequests = Integer.parseInt(paramRequest[1]);
            if (countRequests > identifiers.size()){
                throw new IllegalArgumentException();
            }

            for (int position = 0; position < countRequests; position++){
                long currentIdRequest = identifiers.get(position);

                if (!queue.contains(currentIdRequest)) {
                    countGetRequestInPhysicMemory++;
                    queue.add(currentIdRequest);
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
                writer.write(String.valueOf(countGetRequestInPhysicMemory));
                writer.flush();
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }
    }
}