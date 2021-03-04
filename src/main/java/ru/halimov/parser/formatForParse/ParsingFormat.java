package ru.halimov.parser.formatForParse;

/**
 * Данный интерфейс отвечает за получение распарсенной строки от объектов формата парсинга
 */
public interface ParsingFormat {
    /**
     * @return строка необходимого формата
     */
    String getParseLine();
}
