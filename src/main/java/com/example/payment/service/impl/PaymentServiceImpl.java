package com.example.payment.service.impl;

import com.example.payment.adaptor.ProductAdaptor;
import com.example.payment.dto.PaymentDto;
import com.example.payment.dto.ProductDto;
import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.request.PaymentRequest;
import com.example.payment.response.Response;
import com.example.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    ProductAdaptor productAdapter;

    RestTemplate restTemplate;

    @Value("localhost:8080/api/product/v1")
    String urlProduct;
    @Override
    public List<PaymentDto> list() {
        List<Payment> payments = this.paymentRepository.findAll();
        if(payments.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Data not found");
        return payments.stream().map(this::getFetchToDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto detail(Integer id) {
        Optional<Payment> payment = this.paymentRepository.findById(id);
        AtomicReference<PaymentDto> paymentDto = new AtomicReference<>();
        payment.ifPresentOrElse(data -> paymentDto.set(this.getFetchToDto(data)) ,()->{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
                }
        );
        return paymentDto.get();
    }

    @Override
    @Transactional
    public Boolean create(PaymentRequest request) {
        ProductDto productDto = new ProductDto();
        try {
            log.info("TRY CREATE");
            ResponseEntity<Response> productResponse = this.productAdapter.detail(request.getProductId());
            log.info("PRODUCT RESPOND");
            this.productAdapter.buy(request);
            Response response = productResponse.getBody();
            productDto = new ObjectMapper().convertValue(response.getData(),ProductDto.class);

        }
        catch (Exception fe){
            fe.getMessage();
            fe.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something Went Wrong!");
        }

        Payment newPayment= new Payment();
        newPayment.setProductId(request.getProductId());
        newPayment.setProductName(productDto.getProductName());
        newPayment.setStock(request.getStock());
        newPayment.setTotalAmmount(request.getStock()*productDto.getPrice());
        this.paymentRepository.save(newPayment);
        return true;
    }

    @Override
    public Boolean update(PaymentRequest request) {
        Optional<Payment> existingProduct = this.paymentRepository.findById(request.getId());
        existingProduct.ifPresentOrElse(data->{
            data.setProductId(request.getId());
            data.setTotalAmmount(request.getStock());
                    this.paymentRepository.save(data);
                } ,()->{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
                }
        );
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        Optional<Payment> existingPaymentMeta = this.paymentRepository.findById(id);
        if(existingPaymentMeta.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
        }

        Payment existingPayment = existingPaymentMeta.get();

        try {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(existingPayment.getProductId());
        paymentRequest.setStock(existingPayment.getStock());
        this.productAdapter.cancel(paymentRequest);
        }
        catch (FeignException fe){
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Something went wrong!");
        }
        this.paymentRepository.deleteById(id);

        return true;
    }

    private PaymentDto  getFetchToDto(Payment payment){
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStock(payment.getStock());
        paymentDto.setProductName(payment.getProductName());
        paymentDto.setTotalAmmount(payment.getTotalAmmount());
        return paymentDto;
    }
}
