package org.springdoc.api;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springdoc.model.Order;
import org.springdoc.repository.OrderRepository;
import org.springdoc.repository.PetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoreApiDelegateImpl implements StoreApiDelegate {

    private final OrderRepository orderRepository;

	@SuppressWarnings("unused")
	private final PetRepository petRepository;

    private final NativeWebRequest request;

    public StoreApiDelegateImpl(OrderRepository orderRepository, PetRepository petRepository, NativeWebRequest request) {
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
        this.request = request;
    }

    @PostConstruct
    void initOrders() {
        orderRepository.save(createOrder(1, 1, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(2, 1, Order.StatusEnum.DELIVERED));
        orderRepository.save(createOrder(3, 2, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(4, 2, Order.StatusEnum.DELIVERED));
        orderRepository.save(createOrder(5, 3, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(6, 3, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(7, 3, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(8, 3, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(9, 3, Order.StatusEnum.PLACED));
        orderRepository.save(createOrder(10, 3, Order.StatusEnum.PLACED));
    }


    @Override
    public ResponseEntity<Void> deleteOrder(String orderId) {
        Order order = orderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        orderRepository.delete(order);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Map<String, Integer>> getInventory() {
        ApiUtil.checkApiKey(request);
		return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Override
    public ResponseEntity<Order> placeOrder(Order order) {
        return ResponseEntity.ok(orderRepository.save(order));
    }

    private static Order createOrder(long id, long petId, Order.StatusEnum status) {
        return new Order()
                .id(id)
                .petId(petId)
                .quantity(2)
				.shipDate(new Date())
                .status(status);
    }
}
