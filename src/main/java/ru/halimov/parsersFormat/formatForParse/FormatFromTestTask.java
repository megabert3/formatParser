package ru.halimov.parsersFormat.formatForParse;

public class FormatFromTestTask implements ParsingFormat {

    private int id;
    private int amount;
    private String comment = "";
    private String fileName = "";
    private String line;
    private String result;

    @Override
    public void setParamsForParsing(String params) {
        //1,100,USD,оплата заказа,имя файла,номер линии
        String[] splitParams = params.split(",");

        //Если данных недостаточно
        if (splitParams.length < 6) {
            setErrorResult(splitParams[splitParams.length - 2],splitParams[splitParams.length - 1],
                    "ERROR: Недостаточно данных для парсинга");
            return;
        }

        try {
            id = Integer.parseInt(splitParams[0]);
            amount = Integer.parseInt(splitParams[1]);
            comment = splitParams[3].trim();
            fileName = splitParams[4];
            line = splitParams[5];
            result = "OK";

        }catch (NumberFormatException e) {
            setErrorResult(splitParams[splitParams.length - 2],splitParams[splitParams.length - 1],
                    "ERROR: Неверный формат строки");
        }
    }

    private void setErrorResult(String line, String fileName, String errorMess) {
        id = 0;
        amount = 0;
        comment = "";
        this.fileName = fileName;
        this.line = line;
        result = errorMess;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
