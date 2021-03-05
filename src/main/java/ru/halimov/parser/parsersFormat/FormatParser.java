package ru.halimov.parser.parsersFormat;

import java.io.File;

/**
 * Данный интерфейс отвечает за реализацию парсинга данных у объектов отвечающих за парсинг файлов разного формата
 */
public interface FormatParser {
    void parse(File path);
}
