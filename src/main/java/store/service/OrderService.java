package store.service;

import store.implement.OrderRepositoryImpl;
import store.model.Order;

import java.util.List;

public class OrderService {
    private final OrderRepositoryImpl orderRepository;

    public OrderService(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getBuyOrders() {
        return orderRepository.getBuyOrders();
    }

    public List<Order> getPromotionOrders() {
        return orderRepository.getPromotionOrders();
    }

    public void saveBuyOrderToRepository(Order order) {
        if (order != null) {
            orderRepository.addBuyOrder(order);
        }
    }

    public void savePromotionOrderToRepository(Order order) {
        if (order != null) {
            orderRepository.addPromotionOrder(order);
        }
    }
}
