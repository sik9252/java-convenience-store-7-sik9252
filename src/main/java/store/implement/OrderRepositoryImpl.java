package store.implement;

import store.model.Order;
import store.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> promotionOrders = new ArrayList<>();

    @Override
    public void addBuyOrder(Order order) {
        buyOrders.add(order);
    }

    @Override
    public void addPromotionOrder(Order order) {
        promotionOrders.add(order);
    }

    @Override
    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    @Override
    public List<Order> getPromotionOrders() {
        return promotionOrders;
    }
}
