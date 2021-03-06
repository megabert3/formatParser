package ru.halimov.parser.parsersFormat;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.ParserInJSONFileManager;
import ru.halimov.parser.formatForParse.InputFormatFromTestTask;
import ru.halimov.parser.formatForParse.OutputJSONFormatFromTestTask;


import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Данный класс отвечает за парсинг данных из файлов с расширением SCV
 */
@Component
public class FormatParserSCV implements FormatParser {

    private ParserInJSONFileManager parserInJSONFileManager;

    private String fileName;

    //Файл прочитан?
    private boolean fileEnd = false;

    private Thread outputCSVParserThread;

    private final BlockingQueue<OutputJSONFormatFromTestTask> inputJSONFormatFilesQueue
            = new LinkedBlockingQueue<>();

    @Autowired
    public FormatParserSCV(ParserInJSONFileManager parserInJSONFileManager) {
        this.parserInJSONFileManager = parserInJSONFileManager;
    }

    @Override
    public void parse(File path) {
        fileName = path.getName();

        Thread inputCSVParserThread = new Thread(new InputCSVParserThread(path));
        inputCSVParserThread.start();

        outputCSVParserThread = new Thread(new OutputCSVParserThread());
        outputCSVParserThread.start();
    }

    /**
     * Поток получения данных из CSV файла и конвертации его в необходимый формат
     */
    private class InputCSVParserThread implements Runnable {
        private File path;

        private InputCSVParserThread(File path) {
            this.path = path;
        }

        @Override
        public void run() {
            CsvSchema orderLineSchema = CsvSchema.builder()
                    .addColumn("orderId")
                    .addColumn("amount")
                    .addColumn("currency")
                    .addColumn("comment")
                    .build();

            CsvMapper csvMapper = new CsvMapper();

            try {
                MappingIterator<InputFormatFromTestTask> orderLines = csvMapper.readerFor(InputFormatFromTestTask.class)
                        .with(orderLineSchema)
                        .readValues(path);

                InputFormatFromTestTask inputFormatFromTestTask;
                OutputJSONFormatFromTestTask outputJSONFormatFromTestTask;
                int lineCounter = 0;

                while (orderLines.hasNext()) {
                    lineCounter++;

                    try {
                        inputFormatFromTestTask = orderLines.next();
                        outputJSONFormatFromTestTask = new OutputJSONFormatFromTestTask();

                        outputJSONFormatFromTestTask.setAllParameters(
                                inputFormatFromTestTask.getOrderId(),
                                inputFormatFromTestTask.getAmount(),
                                inputFormatFromTestTask.getComment(),
                                fileName,
                                lineCounter,
                                "OK");

                    }catch (RuntimeJsonMappingException e) {
                        outputJSONFormatFromTestTask = new OutputJSONFormatFromTestTask();

                        outputJSONFormatFromTestTask.createErrorFormat(
                                fileName,
                                lineCounter,
                                "ERROR: " + e.getMessage());
                    }

                    inputJSONFormatFilesQueue.add(outputJSONFormatFromTestTask);
                }

                fileEnd = true;
                outputCSVParserThread.interrupt();

            } catch (IOException e) {
                if (outputCSVParserThread.isAlive()) {
                    outputCSVParserThread.interrupt();
                }
            }
        }
    }

    private class OutputCSVParserThread implements Runnable {

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