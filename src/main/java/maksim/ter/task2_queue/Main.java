package maksim.ter.task2_queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


public class Main {

    private static ArrayDeque<String> queue1 = new ArrayDeque<>();
    private static ArrayDeque<String> queue2 = new ArrayDeque<>();
    private static ArrayDeque<String> queue3 = new ArrayDeque<>();

    public static void main(String[] args) {
        int countGetRequestInPhysicMemory = 0;
        Path path = Paths.get("input.txt");
        try {
            List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<String> identifiers = inputData.stream().skip(1).toList();
            String[] paramRequest = inputData.get(0).split(" ");
            if (paramRequest.length != 2){
                throw new IllegalArgumentException();
            }

            int countRequests = Integer.parseInt(paramRequest[1]);
            final int sizeMemory = 10;
            if (countRequests > identifiers.size()){
                throw new IllegalArgumentException();
            }

            int currentSizeMemory = 0;
            for (int position = 0; position < countRequests; position++){
                String currentIdRequest = identifiers.get(position);

                if (currentIdRequest.charAt(0) == '1') {
                    if (queue1.contains(currentIdRequest)) {
                        continue;
                    }

                    if (currentSizeMemory >= sizeMemory) {
                        queue1.removeFirst();
                        currentSizeMemory--;
                    }

                    currentSizeMemory++;
                    countGetRequestInPhysicMemory++;
                    queue1.addLast(currentIdRequest);

                } else if (currentIdRequest.charAt(0) == '2') {
                    if (queue2.contains(currentIdRequest)) {
                        continue;
                    }

                    if (currentSizeMemory >= sizeMemory) {
                        queue2.removeFirst();
                        currentSizeMemory--;
                    }

                    currentSizeMemory++;
                    countGetRequestInPhysicMemory++;
                    queue2.addLast(currentIdRequest);

                } else {
                    if (queue3.contains(currentIdRequest)) {
                        continue;
                    }

                    if (currentSizeMemory >= sizeMemory) {
                        queue3.removeFirst();
                        currentSizeMemory--;
                    }

                    currentSizeMemory++;
                    countGetRequestInPhysicMemory++;
                    queue3.addLast(currentIdRequest);
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