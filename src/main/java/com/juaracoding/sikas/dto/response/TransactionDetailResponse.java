package com.juaracoding.sikas.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {

    private Integer id;
    private Integer transactionId;
    private Long productId;
    private Integer qtyTransaction;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private Integer createdBy;
}
