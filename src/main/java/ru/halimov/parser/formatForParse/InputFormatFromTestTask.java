package ru.halimov.parser.formatForParse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Данный класс отвечает за конкретный формат приходящих данных из файла
 */
@Component
@Scope("prototype")
@JsonAutoDetect
public class InputFormatFromTestTask {

    private int orderId;
    private double amount;
    private String currency;
    private String comment;

    public InputFormatFromTestTask(){}

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getComment() {
        return comment;
    }
}
