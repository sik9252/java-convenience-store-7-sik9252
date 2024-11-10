package store.controller;

import store.exception.CustomException;
import store.model.Order;
import store.service.MemberShipDiscountService;
import store.service.OrderService;
import store.view.InputView;

import java.util.List;

public class MemberShipDiscountController {
    private final OrderService orderService;
    private final MemberShipDiscountService memberShipDiscountService;
    private final InputView inputView;

    public MemberShipDiscountController(OrderService orderService,
                                        MemberShipDiscountService memberShipDiscountService, InputView inputView) {
        this.orderService = orderService;
        this.memberShipDiscountService = memberShipDiscountService;
        this.inputView = inputView;
    }

    public void discountWithMemberShip() {
        while (true) {
            try {
                tryInputByMemberShipDiscount();
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void tryInputByMemberShipDiscount() {
        String input = inputView.getAnswerToMemberShipDiscount();
        orderService.checkIsValidAnswerToPromotionInfo(input);
        calcDiscountWhenAnswerIsYes(input);
        calcDiscountWhenAnswerIsNo(input);
    }

    private void calcDiscountWhenAnswerIsYes(String input) {
        if (input.equals("Y")) {
            List<Order> list = orderService.getNotPromotionProduct();
            memberShipDiscountService.calculateDiscountPrice(list);
        }
    }

    private void calcDiscountWhenAnswerIsNo(String input) {
        if (input.equals("N")) {
            memberShipDiscountService.setDiscountPrice(0);
        }
    }
}
