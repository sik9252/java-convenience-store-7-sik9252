package store.factory;

import store.controller.OrderController;
import store.implement.OrderRepositoryImpl;
import store.service.OrderService;
import store.view.InputView;

public class OrderFactory {
    public static OrderController createOrderController() {
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        OrderService orderService = new OrderService(orderRepository);
        InputView inputView = new InputView();

        return new OrderController(inputView, orderService);
    }
}
