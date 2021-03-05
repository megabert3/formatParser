package ru.halimov.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.parsersFormat.FormatParserJSON;
import ru.halimov.parser.parsersFormat.FormatParserSCV;

import java.io.File;
import java.util.Locale;

/**
 * Данный класс является менеджером по парсингу файлов. В зависимости от расширения полученного на вход файла
 * делегирует работу необходимому объекту
 */
@Component
public class ParserInJSONFileManager {

    private FormatParserJSON formatParserJSON;
    private FormatParserSCV formatParserSCV;

    public void parse(String pathToFile) {
        File file = new File(pathToFile);

        if (!file.isFile()) {
            System.out.println("Указанный путь не является файлом: " + pathToFile);
            return;
        }

        String extension = getExtension(pathToFile).toLowerCase(Locale.ROOT);

        switch (extension) {
            case "csv" :
                formatParserSCV.parse(file);
                break;

            case "json" :
                formatParserJSON.parse(file);
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

    @Autowired
    public void setFormatParserJSON(FormatParserJSON formatParserJSON) {
        this.formatParserJSON = formatParserJSON;
    }

    @Autowired
    public void setFormatParserSCV(FormatParserSCV formatParserSCV) {
        this.formatParserSCV = formatParserSCV;
    }
}
