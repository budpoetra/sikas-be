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

    @NotEmpty(message = "Transaction details cannot be empty")
    private List<TransactionDetailDTO> transactionDetails;

}
