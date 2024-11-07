package store.controller;

import store.exception.CustomException;
import store.service.OrderService;
import store.view.InputView;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.INVALID_PURCHASE_FORMAT;


public class OrderController {
    private static final String PATTERN = "\\[([가-힣A-Za-z]+)-([1-9][0-9]*)](,\\[([가-힣A-Za-z]+)-([1-9][0-9]*)])*";

    private final InputView inputView;
    private final OrderService orderService;

    public OrderController(InputView inputView, OrderService orderService) {
        this.inputView = inputView;
        this.orderService = orderService;
    }

    public void order() {
        while (true) {
            try {
                String input = inputView.getOrders();
                checkInputIsEmpty(input);
                checkInputHasValidFormat(input);
                orderService.create(input);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private void checkInputIsEmpty(String input) {
        if (input.isEmpty()) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }

    private void checkInputHasValidFormat(String input) {
        if (!input.matches(PATTERN)) {
            throw new CustomException(INVALID_PURCHASE_FORMAT.getMessage());
        }
    }
}
