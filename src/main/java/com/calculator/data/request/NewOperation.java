package com.calculator.data.request;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.data.entity.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
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
                if (this.getNumber1().isNaN() || this.getNumber2().isNaN()) {
                    throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Missing number");
                }
                break;
            }
            case SQUAREROOT: {
                if (this.getNumber1().isNaN()) {
                    throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Missing Number");
                }
                break;
            }
            default:
                this.setNumber1(number1);
                this.setNumber2(number2);
                this.setType(type);
        }
    }

}
