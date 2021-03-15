package com.example.payment.entity;

import com.example.payment.dto.ProductDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "product_id")
    Integer productId;

    @Column(name = "product_name")
    String productName;

    @Column(name="stock")
    Integer stock;

    @Column(name = "total_ammount")
    Integer totalAmmount;


}
