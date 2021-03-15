package com.example.payment.adaptor;

import com.example.payment.request.PaymentRequest;
import com.example.payment.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name ="product", contextId = "productContext",url = "localhost:8080/api/product")
public interface ProductAdaptor {

    @GetMapping("/v1/detail/{id}")
    ResponseEntity<Response> detail(@PathVariable("id") Integer id);

    @PostMapping("/v1/buy")
    ResponseEntity<Response> buy(@RequestBody PaymentRequest request);

    @PostMapping("/v1/cancel")
    ResponseEntity<Response> cancel(@RequestBody PaymentRequest request);

}
