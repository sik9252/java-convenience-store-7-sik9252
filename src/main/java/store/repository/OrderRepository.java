package store.repository;

import store.model.Order;

import java.util.List;

public interface OrderRepository {
    void saveTotalOrder(Order order);

    void saveFreeOrderByPromotion(Order order);

    List<Order> getTotalOrder();

    List<Order> getFreeOrderByPromotion();
}
