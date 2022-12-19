package br.com.lucas.ecommerce;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String userId, orderId;
    private BigDecimal amount;

    boolean isFraud() {
        return amount.compareTo(new BigDecimal("4500")) >= 0;
    }
}
