package com.crypto212.marketengine.web;

import com.crypto212.marketengine.service.OrderService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createOrder(@RequestHeader("X-User-Id") Long userId){
        orderService.createOrder(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO
                        .builder()
                        .message("Order created successfully!")
                        .content(null)
                        .build());
    }
}
