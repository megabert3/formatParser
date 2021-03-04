package ru.halimov.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.parsersFormat.FormatParserJSON;
import ru.halimov.parser.parsersFormat.FormatParserSCV;

import java.io.File;

/**
 * Данный класс является менеджером по парсингу файлов. В зависимости от расширения полученного на вход файла
 * делегирует работу необходимому объекту
 */
@Component
public class ParserInJSONFileManager {

    @Autowired
    private FormatParserJSON formatParserJSON;
    @Autowired
    private FormatParserSCV formatParserSCV;

    public void parse(String pathToFile) {
        File file = new File(pathToFile);

        if (!file.isFile()) {
            System.out.println("Указанный путь не является файлом: " + pathToFile);
            return;
        }

        switch (getExtension(pathToFile)) {
            case "csv" :
                formatParserSCV.setPathToSCVFile(pathToFile);
                formatParserSCV.parse();
                break;

            case "json" :
                formatParserJSON.setPathToJSONFile(pathToFile);
                formatParserJSON.parse();
                break;

            default:
                System.out.println("Формат файла не поддерживается");

        }
    }

    /**
     * Возвращает расширение файла
     * @param path - путь к файлу
     * @return - расширение файла
     */
    private String getExtension(String path) {
        String localPath = path.trim();
        int i = localPath.lastIndexOf(".");
        return localPath.substring(i + 1);
    }
}
