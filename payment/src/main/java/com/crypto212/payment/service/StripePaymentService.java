package com.crypto212.payment.service;

import com.crypto212.clients.userwallet.UserWalletClient;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripePaymentService {
    @Value("${app.stripe.webHookSecret}")
    private String webHookSecret;
    @Value("${app.stripe.successUrl}")
    private String successUrl;
    @Value("${app.stripe.cancelUrl}")
    private String cancelUrl;

    private final UserWalletClient userWalletClient;

    public StripePaymentService(UserWalletClient userWalletClient) {
        this.userWalletClient = userWalletClient;
    }

    @SneakyThrows
    public String createCheckoutSession(Long userId, String amount){
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .putMetadata("userId", String.valueOf(userId))
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("USD")
                                                        .setUnitAmount(Long.parseLong(amount)*100)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Deposit")
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    @SneakyThrows
    public void processEvent(String requestBody, String signature) {
//        log.info("Got payload: " + requestBody);
        Event event = Webhook.constructEvent(requestBody, signature, webHookSecret);
        String eventType = event.getType();
        if("checkout.session.completed".equals(eventType)){
            Session session = (Session) event.getData().getObject();
            Map<String, String> metadata = session.getMetadata();
            String userId = metadata.get("userId");
            Long amount = session.getAmountTotal() / 100;
            userWalletClient.addAssets(Long.parseLong(userId), "USDT", String.valueOf(amount));
        }
    }
}
