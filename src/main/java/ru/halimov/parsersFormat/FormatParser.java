package ru.halimov.parsersFormat;

import ru.halimov.parsersFormat.formatForParse.ParsingFormat;

import java.io.FileNotFoundException;
import java.util.List;

public interface FormatParser {
    void parse(ParsingFormat parsingFormat);
}
