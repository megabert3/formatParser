package ru.halimov;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.halimov.parser.ParserInJSONFileManager;
import ru.halimov.springConfig.FormatParserAppConfig;

public class FormatParserMain {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(FormatParserAppConfig.class);

        ParserInJSONFileManager parserInJSONFileManager =
                annotationConfigApplicationContext.getBean(ParserInJSONFileManager.class);

        for (String pathToFile : args) {
            parserInJSONFileManager.parse(pathToFile);
        }

        /*String csv = "C:\\Users\\bert1\\Desktop\\test\\csvFile.csv";
        String json = "C:\\Users\\bert1\\Desktop\\test\\jsonFile.json";

        parserInJSONFileManager.parse(csv);
        parserInJSONFileManager.parse(json);*/

        annotationConfigApplicationContext.close();
    }
}
