package store.controller;

import store.service.OrderService;
import store.view.InputView;


public class OrderController {
    private final InputView inputView;
    private final OrderService orderService;

    public OrderController(InputView inputView, OrderService orderService) {
        this.inputView = inputView;
        this.orderService = orderService;
    }

    public void order() {
        String input = inputView.getOrders();
        orderService.create(input);
    }
}
