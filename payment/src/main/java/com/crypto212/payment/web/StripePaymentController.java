package com.crypto212.payment.web;

import com.crypto212.payment.service.StripePaymentService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment/stripe")
public class StripePaymentController {
    private final StripePaymentService stripePaymentService;

    public StripePaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/webhook")
    public ResponseEntity stripeWebHook(@RequestBody String requestBody,
                                        @RequestHeader("Stripe-Signature") String signature) {
        stripePaymentService.processEvent(requestBody, signature);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<ResponseDTO> createSession(@RequestHeader("X-User-Id") Long userId,
                                                     @RequestParam("amount") String amount) {
        String sessionUrl = stripePaymentService.createCheckoutSession(userId, amount);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO
                        .builder()
                        .content(sessionUrl)
                        .message("Create checkout session successfully!")
                        .build());
    }

}
