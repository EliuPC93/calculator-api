package com.calculator.data.response;

import com.calculator.data.entity.Record;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecordDto {
    private String id;
    private Double amount;
    private String operationType;
    private String operationResponse;
    private Double userBalance;
    private String date;

    public static RecordDto from(Record record) {
        return RecordDto.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .operationType(record.getOperation().getType())
                .operationResponse(record.getOperationResponse())
                .userBalance(record.getUserBalance())
                .date(record.getDate().toString())
                .build();
    }

}
