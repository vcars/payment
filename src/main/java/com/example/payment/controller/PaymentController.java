package com.example.payment.controller;

import com.example.payment.dto.PaymentDto;
import com.example.payment.request.PaymentRequest;
import com.example.payment.response.Response;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/payment/v1")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private Response response;

    @GetMapping(value = "/list")
    public ResponseEntity<Response> list(){
        List<PaymentDto> result = this.paymentService.list();
        response = new Response(result,"Get list payment",true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<Response> detail(@PathVariable Integer id){
        PaymentDto result = this.paymentService.detail(id);
        response = new Response(result,"Get detail payment",true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Response> create(@RequestBody PaymentRequest request){
        Boolean result = this.paymentService.create(request);
        response = new Response(result,"Payment submitted succesfully",true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody PaymentRequest request){
        Boolean result = this.paymentService.update(request);
        response = new Response(result,"Payment updated succesfully",true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id){
        Boolean result = this.paymentService.delete(id);
        response = new Response(result,"Payment deleted succesfully",true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
