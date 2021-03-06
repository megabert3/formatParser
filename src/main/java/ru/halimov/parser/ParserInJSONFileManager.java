package ru.halimov.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.halimov.parser.parsersFormat.FormatParserJSON;
import ru.halimov.parser.parsersFormat.FormatParserCSV;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Данный класс является менеджером по парсингу файлов. В зависимости от расширения полученного на вход файла
 * делегирует работу необходимому объекту
 */
@Component
public class ParserInJSONFileManager {

    private final BlockingQueue<String> filesPathQueue = new LinkedBlockingQueue<>();

    //Мьютекс говорящий о том, что очередь путей пуста и первый входной путь будет взят в обработку
    private boolean canParseImmediately  = true;

    private FormatParserJSON formatParserJSON;

    private FormatParserCSV formatParserCSV;

    public void parse(String pathToFile) {

        if (canParseImmediately) {

            File file = new File(pathToFile);

            if (!file.isFile()) {
                System.out.println("Указанный путь не является файлом: " + pathToFile);
                return;
            }

            String extension = getExtension(pathToFile).toLowerCase(Locale.ROOT);

            switch (extension) {
                case "csv":
                    formatParserCSV.parse(file);
                    canParseImmediately = false;
                    break;

                case "json":
                    formatParserJSON.parse(file);
                    canParseImmediately = false;
                    break;

                default:
                    System.out.println("Формат файла не поддерживается");
            }

        } else {
            filesPathQueue.add(pathToFile);
        }
    }

    /**
     * Передаёт в управление новую ссылку для парсинга
     */
    public synchronized void parseNextFile() {
        String path = filesPathQueue.poll();
        canParseImmediately = true;

        if (path != null) {
            parse(path);
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
    public void setFormatParserSCV(FormatParserCSV formatParserCSV) {
        this.formatParserCSV = formatParserCSV;
    }
}
