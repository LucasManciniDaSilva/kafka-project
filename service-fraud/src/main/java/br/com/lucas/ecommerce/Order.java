package br.com.lucas.ecommerce;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

        private String orderId;
        private BigDecimal amount;

        private String email;
    boolean isFraud() {
        return amount.compareTo(new BigDecimal("4500")) >= 0;
    }
}
