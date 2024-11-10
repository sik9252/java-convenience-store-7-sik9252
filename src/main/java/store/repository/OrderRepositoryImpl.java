package store.repository;

import store.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private final List<Order> totalOrder = new ArrayList<>();
    private final List<Order> freeOrderByPromotion = new ArrayList<>();
    private List<Order> notPromotionOrders = new ArrayList<>();

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

    public List<Order> getNotPromotionProduct() {
        removePromotionOrders();
        return notPromotionOrders;
    }

    private void removePromotionOrders() {
        List<Order> list = new ArrayList<>(totalOrder);

        list.removeIf(buyOrder -> freeOrderByPromotion.stream()
                .anyMatch(promotionOrder -> promotionOrder.getName().equals(buyOrder.getName())));

        notPromotionOrders = list;
    }
}
