package ru.halimov;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.halimov.parsersFormat.FormatParserJSON;
import ru.halimov.parsersFormat.FormatParserSCV;
import ru.halimov.springConfig.FormatParserAppConfig;

public class FormatParserMain {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(FormatParserAppConfig.class);

        FormatParserSCV formatParserSCV = annotationConfigApplicationContext.getBean(FormatParserSCV.class);
        formatParserSCV.setPathToSCVFile("C:\\Users\\bert1\\Desktop\\test\\csvFile.csv");
        formatParserSCV.parse();

        FormatParserJSON formatParserJSON = annotationConfigApplicationContext.getBean(FormatParserJSON.class);
        formatParserJSON.setPathToJSONFile("C:\\Users\\bert1\\Desktop\\test\\jsonFile.json");
        formatParserJSON.parse();

        annotationConfigApplicationContext.close();
    }
}
