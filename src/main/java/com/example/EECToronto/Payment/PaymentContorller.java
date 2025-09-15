package com.example.EECToronto.Payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("https://geecvancouver.vercel.app")
@RequestMapping(path = "api/payment")
public class PaymentContorller {

    private final PaymentService paymentService;
    @Autowired
    public PaymentContorller(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping
    public ResponseEntity<Object> handlePayment(@RequestBody Payment payment) throws StripeException {
        return ResponseEntity.ok(paymentService.handlePaymentService(payment.getAmount(), payment.getCurrency()));
    }
}
