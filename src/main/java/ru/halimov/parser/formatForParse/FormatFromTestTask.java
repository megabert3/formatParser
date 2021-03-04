package ru.halimov.parser.formatForParse;

import org.springframework.stereotype.Component;

@Component
public class FormatFromTestTask implements ParsingFormat {

    private int id;
    private double amount;
    private String comment = "";
    private String fileName = "";
    private int line;
    private String result;

    @Override
    public String getParseLine() {
        //{“id”:1, ”amount”:100, ”comment”:”оплата заказа”, ”filename”:”orders.csv”, ”line”:1, ”result”:”OK” }
        return "{\"id:\":" + id + "," +
                "\"amount\":" + amount + "," +
                "\"comment\":" + "\"" + comment + "\"" + "," +
                "\"filename\":" + "\"" + fileName + "\"" + "," +
                "\"line\":" + line + "," +
                "\"result\":" + "\"" + result + "\"";
    }

    public void setErrorResult(int line, String fileName, String errorMess) {
        id = 0;
        amount = 0;
        comment = "";
        this.fileName = fileName;
        this.line = line;
        result = errorMess;
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
