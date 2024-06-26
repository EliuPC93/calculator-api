package com.calculator.data.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@ToString
public class OperationResponseDto {

    private String result;

}
