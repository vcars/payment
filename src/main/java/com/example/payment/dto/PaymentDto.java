package com.example.payment.dto;


import lombok.Data;

@Data
public class PaymentDto {

    Integer productId;

    String productName;

    Integer stock;

    Integer totalAmmount;

    ProductDto productDto;

}
