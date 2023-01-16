package com.crypto212.marketengine.web;

import com.crypto212.marketengine.service.OrderService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market/order")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{currencyPair}/{actionType}")
    public ResponseEntity<ResponseDTO> createBuyOrder(@RequestHeader("X-User-Id") Long userId,
                                                      @PathVariable("currencyPair") String currencyPair,
                                                      @PathVariable("actionType") String actionType,
                                                      @RequestParam("walletId") Long walletId,
                                                      @RequestParam("amount") String amount) {
        orderService.exchange(walletId, currencyPair, actionType, amount, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO
                        .builder()
                        .message("Order processed successfully!")
                        .content(null)
                        .build());
    }

}
