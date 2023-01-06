package com.crypto212.customer;

import com.crypto212.amqp.RabbitMQMessageProducer;
import com.crypto212.clients.fraud.FraudCheckResponse;
import com.crypto212.clients.fraud.FraudClient;
import com.crypto212.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final FraudClient fraudClient;

    public CustomerService(CustomerRepository customerRepository, RabbitMQMessageProducer rabbitMQMessageProducer,
                           FraudClient fraudClient) {
        this.customerRepository = customerRepository;
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
        this.fraudClient = fraudClient;
    }


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to Amigoscode...",
                        customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }
}
