package com.calculator.data.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class RecordsReponse {
    private List<RecordDto> records;
}
