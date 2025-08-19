package com.example.EECToronto.Payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {
    public String handlePaymentService(Long amount, String currency) throws StripeException {
        Stripe.apiKey = "sk_test_51QgKtWLPVb9tqR1BYbZ535nHM9FxYpWYpSlLprhhsr2WIj2bw3aIBcJzoRkB7YqYKhlaRKJwVKGOSkNVpukvGot2009ZBtVt1p";
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .setReceiptEmail("eyosiasdesta10@gmail.com")
                        .addPaymentMethodType("card")
                .build();
        PaymentIntent paymentIntenet = PaymentIntent.create((params));
        System.out.println("the Payment Intent is: " + paymentIntenet.getClientSecret() + " : " + paymentIntenet.getAmount());
        return paymentIntenet.toJson();
    }
}
