package com.juaracoding.sikas.dto.validation;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

//    @NotNull(message = "User ID cannot be null")
//    private Integer userId;

//    @NotNull(message = "Total price cannot be null")
//    private Integer to;

    @NotEmpty(message = "Transaction details cannot be empty")
    private List<TransactionDetailDTO> transactionDetails;

//    @NotNull(message = "Created by cannot be null")
//    private Integer createdBy;
}


// validasi productId, qtyTransaction, price tidak boleh koosong, integer
