package store.service;

import store.implement.OrderRepositoryImpl;
import store.model.Order;
import store.utils.StringUtils;

import java.util.List;

public class OrderService {
    private final OrderRepositoryImpl orderRepository;

    public OrderService(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> get() {
        return orderRepository.getOrders();
    }

    public void create(String input) {
        String[] orderInfo = StringUtils.splitStringWithComma(input);

        for (String info : orderInfo) {
            String[] processedInfo = info.replace("[", "").replace("]", "").
                    split("-");
            String name = processedInfo[0];
            int quantity = Integer.parseInt(processedInfo[1]);

            Order order = new Order(name, quantity);
            saveToRepository(order);
        }
    }

    private void saveToRepository(Order order) {
        if (order != null) {
            orderRepository.addOrder(order);
        }
    }

}
