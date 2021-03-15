package com.example.payment.service;

import com.example.payment.dto.PaymentDto;
import com.example.payment.request.PaymentRequest;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> list();
    PaymentDto detail(Integer id);
    Boolean create(PaymentRequest paymentRequest);
    Boolean update(PaymentRequest paymentRequest);
    Boolean delete(Integer id);

}
