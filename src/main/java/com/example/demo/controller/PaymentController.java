package com.example.demo.controller;

import com.example.demo.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("payment")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("create")
    public Map<String,String> createPayment(@RequestBody Map<String,Object> data){
        try{
            Long amount = ((Number) data.get("amount")).longValue();
            String currency = (String) data.get("currency");

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount,currency);
            return Map.of("clientSecret", paymentIntent.getClientSecret());
        }catch (StripeException e){
            return Map.of("error",e.getMessage());
        }
    }
}
