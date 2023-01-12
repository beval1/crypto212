package com.crypto212.marketengine.web;

import com.crypto212.marketengine.service.OrderService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{currencyPair}")
    public ResponseEntity<ResponseDTO> createOrder(@RequestHeader("X-User-Id") Long userId,
                                                   @PathVariable("currencyPair") String currencyPair,
                                                   @RequestParam("actionType") String actionType,
                                                   @RequestParam("amount") String amount){
        orderService.createOrder(userId, currencyPair, actionType, amount);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO
                        .builder()
                        .message("Order created successfully!")
                        .content(null)
                        .build());
    }
}
