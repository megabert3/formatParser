package ru.halimov.parsersFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.formatForParse.FormatFromTestTask;

import java.io.*;

@Component
public class FormatParserJSON implements FormatParser {

    @Autowired
    private FormatFromTestTask formatFromTestTask;
    private String pathToJSONFile;
    private String fileName;

    @Override
    public void parse() {
        try (BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(pathToJSONFile)))) {

            int lineCounter = 0;
            String line;
            String[] splitLine;

            while ((line = fileStream.readLine()) != null) {
                lineCounter++;

                //{“orderId”:2,”amount”:1.23,”currency”:”USD”,”comment”:”оплата заказа”}
                splitLine = line.split(",");

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

    public void setPathToJSONFile(String pathToJSONFile) {
        this.pathToJSONFile = pathToJSONFile;
        this.fileName = new File(pathToJSONFile).getName();
    }
}
