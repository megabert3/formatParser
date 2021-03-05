package ru.halimov.parser.parsersFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.formatForParse.FormatFromTestTask;

import java.io.*;

/**
 * Данный класс отвечает за парсинг данных из файлов с расширением JSON
 */
@Component
public class FormatParserJSON implements FormatParser {

    private FormatFromTestTask formatFromTestTask;

    @Override
    public void parse(File path) {

        String fileName = path.getName();

        try (BufferedReader fileStream = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path)))) {

            int lineCounter = 0;
            String line;

            while ((line = fileStream.readLine()) != null) {
                lineCounter++;

                //{“orderId”:2,”amount”:1.23,”currency”:”USD”,”comment”:”оплата заказа”}
                String[] splitLine = line.split(",");

                if (splitLine.length < 4) {
                    formatFromTestTask.setErrorResult(lineCounter, fileName,
                            "ERROR: недостаточно данных для парсинга");

                } else if (splitLine.length > 4) {
                    formatFromTestTask.setErrorResult(lineCounter, fileName,
                            "ERROR: находится больше данных чем необходимо");

                } else {
                    try {
                        formatFromTestTask.setId(Integer.parseInt(splitLine[0].split(":")[1]));
                        formatFromTestTask.setAmount(Double.parseDouble(splitLine[1].split(":")[1]));

                        String commentInQuotes  = splitLine[3].trim().split(":")[1];
                        formatFromTestTask.setComment(commentInQuotes.substring(1, commentInQuotes.length() - 1));

                        formatFromTestTask.setFileName(fileName);
                        formatFromTestTask.setLine(lineCounter);
                        formatFromTestTask.setResult("OK");

                    }catch (NumberFormatException e) {
                        formatFromTestTask.setErrorResult(lineCounter, fileName,
                                "ERROR: не удалось привести значение к необходимому типу данных");
                    }
                }

                System.out.println(formatFromTestTask.getParseLine());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Autowired
    public void setFormatFromTestTask(FormatFromTestTask formatFromTestTask) {
        this.formatFromTestTask = formatFromTestTask;
    }
}
