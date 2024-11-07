package store.service;

import store.implement.OrderRepositoryImpl;
import store.model.Order;

import java.util.List;

public class OrderService {
    private final OrderRepositoryImpl orderRepository;

    public OrderService(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public void createOrder(String input) {
        String[] orders = input.split(",");

        for (String item : orders) {
            String[] orderInfo = item.replace("[", "").replace("]", "").
                    split("-");
            String name = orderInfo[0];
            int quantity = Integer.parseInt(orderInfo[1]);

            Order order = new Order(name, quantity);
            addOrderToRepository(order);
        }
    }

    private void addOrderToRepository(Order order) {
        if (order != null) {
            orderRepository.addOrder(order);
        }
    }

}
