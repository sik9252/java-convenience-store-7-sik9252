package store.repository;

import store.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private final List<Order> totalOrder = new ArrayList<>();
    private final List<Order> freeOrderByPromotion = new ArrayList<>();

    @Override
    public void saveTotalOrder(Order order) {
        totalOrder.add(order);
    }

    @Override
    public void saveFreeOrderByPromotion(Order order) {
        freeOrderByPromotion.add(order);
    }

    @Override
    public List<Order> getTotalOrder() {
        return totalOrder;
    }

    @Override
    public List<Order> getFreeOrderByPromotion() {
        return freeOrderByPromotion;
    }
}
