package ru.halimov.parsersFormat;

import org.springframework.stereotype.Component;
import ru.halimov.parsersFormat.formatForParse.FormatFromTestTask;
import ru.halimov.parsersFormat.formatForParse.ParsingFormat;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class FormatParserSCV implements FormatParser {

    private String pathToSCVFile;
    private String fileName;
    private ParsingFormat parsingFormat;

    public FormatParserSCV(String pathToSCVFile) {
        this.pathToSCVFile = pathToSCVFile;
        this.fileName = new File(pathToSCVFile).getName();
    }

    @Override
    public void parse(ParsingFormat parsingFormat) {

        try (BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(pathToSCVFile)))) {

            int lineCounter = 0;
            String line;
            String[] splitLine;
            List<String> paramList = new ArrayList<>();

            while ((line = fileStream.readLine()) != null) {
                lineCounter++;
                parsingFormat.setParamsForParsing(line + "," + fileName + "," + lineCounter);
                System.out.println(parsingFormat.getParseLine());
            }

        } catch (IOException e) {
        }

    }
}