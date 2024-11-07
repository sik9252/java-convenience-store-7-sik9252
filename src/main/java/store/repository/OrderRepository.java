package store.repository;

import store.model.Order;

import java.util.List;

public interface OrderRepository {
    void addOrder(Order order);

    List<Order> getOrders();
}
