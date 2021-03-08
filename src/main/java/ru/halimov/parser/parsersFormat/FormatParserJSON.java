package ru.halimov.parser.parsersFormat;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.ParserInJSONFileManager;
import ru.halimov.parser.formatForParse.InputFormatFromTestTask;
import ru.halimov.parser.formatForParse.OutputJSONFormatFromTestTask;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Данный класс отвечает за парсинг данных из файлов с расширением JSON
 */
@Component
public class FormatParserJSON implements FormatParser {

    private ParserInJSONFileManager parserInJSONFileManager;

    private String fileName;

    //Файл прочитан?
    private boolean fileEnd = false;

    private Thread outputJSONThread;

    //Очередь с объектами подготовленными для парсинга и вывода в концоль
    private final BlockingQueue<OutputJSONFormatFromTestTask> inputJSONFormatFilesQueue
            = new LinkedBlockingQueue<>();

    @Autowired
    public FormatParserJSON(ParserInJSONFileManager parserInJSONFileManager) {
        this.parserInJSONFileManager = parserInJSONFileManager;
    }

    @Override
    public void parse(File path) {
        fileName = path.getName();
        fileEnd = false;

        Thread inputJSONThread = new Thread(new InputJSONParserThread(path));
        inputJSONThread.start();

        outputJSONThread = new Thread(new OutputJSONParserThread());
        outputJSONThread.start();
    }

    /**
     * Поток получения информации из файла.
     * Инициализирует объект с необходимыми данными (согласно формату) и передаёт очереди для дальнейшей обработки
     */
    private class InputJSONParserThread implements Runnable {

        private File path;

        private InputJSONParserThread (File path) {
            this.path = path;
        }

        @Override
        public void run() {
            try (BufferedReader fileStream = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(path), Charset.forName("UTF-8")))) {


                InputFormatFromTestTask inputFormatFromTestTask;
                ObjectMapper mapper = new ObjectMapper();
                int lineCounter = 0;
                String line;

                while ((line = fileStream.readLine()) != null) {
                    lineCounter++;

                    OutputJSONFormatFromTestTask outputJSONFormatFromTestTask = new OutputJSONFormatFromTestTask();

                    try {
                        inputFormatFromTestTask = mapper.readValue(line, InputFormatFromTestTask.class);

                    } catch (JsonGenerationException | JsonMappingException e) {
                        outputJSONFormatFromTestTask.createErrorFormat(fileName, lineCounter, "ERROR: " + e.getMessage());
                        inputJSONFormatFilesQueue.add(outputJSONFormatFromTestTask);
                        continue;
                    }

                    outputJSONFormatFromTestTask = new OutputJSONFormatFromTestTask(
                            inputFormatFromTestTask.getOrderId(),
                            inputFormatFromTestTask.getAmount(),
                            inputFormatFromTestTask.getComment(),
                            fileName,
                            lineCounter,
                            "OK"
                    );

                    inputJSONFormatFilesQueue.add(outputJSONFormatFromTestTask);
                }

                fileEnd = true;
                outputJSONThread.interrupt();

            } catch (IOException e) {
                if (outputJSONThread.isAlive()) {
                    outputJSONThread.interrupt();
                }
            }
        }
    }

    /**
     * Поток получения информации из очереди с подготовленными для парсинга объектами.
     * Парсит объект в JSON формат и выводит в консоль
     */
    private class OutputJSONParserThread implements Runnable {

        @Override
        public void run() {

            ObjectMapper mapper = new ObjectMapper();

            try {
                //До тех пор пока данные в файле для чтения не закончились
                while (!fileEnd) {

                    StringWriter stringWriter = new StringWriter();

                    OutputJSONFormatFromTestTask outputJSONFormatFromTestTask =
                            inputJSONFormatFilesQueue.take();

                    mapper.writeValue(stringWriter, outputJSONFormatFromTestTask);

                    System.out.println(stringWriter);
                }

                //Если файл закончился, но в очереди ещё остались объекты для парсинга (flush)
                readAndGetParseObject(mapper);

            } catch (InterruptedException e) {
                readAndGetParseObject(mapper);
            } catch (IOException ignore) { }
        }

        /**
         * Дочитывает объекты из очереди и парсит их
         * @param mapper
         */
        private void readAndGetParseObject(ObjectMapper mapper) {

            OutputJSONFormatFromTestTask outputJSONFormatFromTestTask;

            while ((outputJSONFormatFromTestTask = inputJSONFormatFilesQueue.poll()) != null) {

                try {
                    StringWriter stringWriter = new StringWriter();
                    mapper.writeValue(stringWriter, outputJSONFormatFromTestTask);
                    System.out.println(stringWriter);
                } catch (IOException ignore) { }
            }

            //Говорю начать парсить следующий файл
            parserInJSONFileManager.parseNextFile();
        }
    }
}
