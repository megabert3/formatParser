package ru.halimov.parser.parsersFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.formatForParse.FormatFromTestTask;


import java.io.*;

@Component
public class FormatParserSCV implements FormatParser {

    private String pathToSCVFile;
    private String fileName;

    @Autowired
    private FormatFromTestTask formatFromTestTask;

    @Override
    public void parse() {

        try (BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(pathToSCVFile)))) {

            int lineCounter = 0;
            String line;
            String[] splitLine;

            while ((line = fileStream.readLine()) != null) {
                lineCounter++;

                //1,100,USD,оплата заказа
                splitLine = line.split(",");

                if (splitLine.length < 4) {
                    formatFromTestTask.setErrorResult(lineCounter, fileName,
                            "ERROR: недостаточно данных для парсинга");

                } else if (splitLine.length > 4) {
                    formatFromTestTask.setErrorResult(lineCounter, fileName,
                            "ERROR: находится больше данных чем необходимо");

                } else {
                    try {
                        formatFromTestTask.setId(Integer.parseInt(splitLine[0]));
                        formatFromTestTask.setAmount(Integer.parseInt(splitLine[1]));
                        formatFromTestTask.setComment(splitLine[3].trim());
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

    public void setPathToSCVFile(String pathToSCVFile) {
        this.pathToSCVFile = pathToSCVFile;
        this.fileName = new File(pathToSCVFile).getName();
    }
}