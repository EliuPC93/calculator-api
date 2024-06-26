package com.calculator.data.request;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.data.entity.OperationType;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@ToString
public class NewOperation {
    @NotEmpty(message = "Invalid operation")
    private String type;

    private Double number1;
    private Double number2;

    public NewOperation(String type, Double number1, Double number2) {
        OperationType operationType = OperationType.fromValue(type);
        switch (operationType) {
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION:
            case DIVISION: {
                if (number1 == null || number2 == null) {
                    throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Missing number");
                }
                break;
            }
            case SQUAREROOT: {
                if (number1 == null) {
                    throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Missing Number");
                }
                break;
            }
            default:
                this.setNumber1(number1);
                this.setNumber2(number2);
                this.setType(type);
        }

        this.setNumber1(number1);
        this.setNumber2(number2);
        this.setType(type);
    }

}
