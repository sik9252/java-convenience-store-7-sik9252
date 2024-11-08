package store.repository;

import store.model.Order;

import java.util.List;

public interface OrderRepository {
    void addBuyOrder(Order order);

    void addPromotionOrder(Order order);

    List<Order> getBuyOrders();

    List<Order> getPromotionOrders();
}
