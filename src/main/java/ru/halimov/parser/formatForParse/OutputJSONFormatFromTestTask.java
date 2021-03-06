package ru.halimov.parser.formatForParse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Данный класс отвечает за конкретный формат записи, которую необходимо возвращать
 */
@Component
@Scope("prototype")
@JsonAutoDetect
public class OutputJSONFormatFromTestTask {

    private int id;
    private double amount;
    private String comment;
    private String fileName;
    private int line;
    private String result;

    public OutputJSONFormatFromTestTask() {}

    @JsonIgnore
    public void setAllParameters(int id, double amount, String comment, String fileName, int line, String result) {
        this.id = id;
        this.amount = amount;
        this.comment = comment;
        this.fileName = fileName;
        this.line = line;
        this.result = result;
    }

    @JsonIgnore
    public void createErrorFormat(String filename, int line, String error) {
        id = 0;
        amount = 0;
        comment = "";
        this.fileName = filename;
        this.line = line;
        result = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
