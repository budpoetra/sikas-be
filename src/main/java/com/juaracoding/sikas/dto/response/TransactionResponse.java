package com.juaracoding.sikas.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Integer id;
    private Integer userId;
    private String transactionNumber;
    private LocalDateTime createdDate;
    private BigDecimal totalPriceTransaction;
    private List<TransactionDetailResponse> transactionDetails;
}
