package store.implement;

import store.model.Order;
import store.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> promotionOrders = new ArrayList<>();
    private List<Order> notPromotionOrders = new ArrayList<>();

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

    public List<Order> getNotPromotionProduct() {
        removePromotionOrders();
        return notPromotionOrders;
    }

    private void removePromotionOrders() {
        List<Order> list = new ArrayList<>(buyOrders);

        list.removeIf(buyOrder -> promotionOrders.stream().anyMatch(promotionOrder ->
                promotionOrder.getName().equals(buyOrder.getName())));

        notPromotionOrders = list;
    }
}
