package store.controller;

import store.model.Order;
import store.service.OrderService;
import store.service.ReceiptService;
import store.view.OutputView;

import java.util.List;

public class ReceiptController {
    private final OrderService orderService;
    private final ReceiptService receiptService;
    private final OutputView outputView;

    public ReceiptController(OrderService orderService, ReceiptService receiptService, OutputView outputView) {
        this.orderService = orderService;
        this.receiptService = receiptService;
        this.outputView = outputView;
    }

    public void makeReceipt() {
        receiptService.calculate();
        List<Order> buyOrders = orderService.getTotalOrder();
        List<Order> promotionOrders = orderService.getFreeOrderByPromotion();
        int[] totalPriceInfos = getTotalPriceInfo(buyOrders);

        printTotalPriceInfo(buyOrders, promotionOrders, totalPriceInfos[0], totalPriceInfos[1], totalPriceInfos[2],
                totalPriceInfos[3], totalPriceInfos[4]);
    }

    private void printTotalPriceInfo(List<Order> buyOrders, List<Order> promotionOrders, int totalQuantity, int totalOrderPrice
            , int totalPromotionPrice, int totalDiscountPrice, int totalPurchasePrice) {
        outputView.printTotalOrderInfo(buyOrders);
        outputView.printTotalPromotionInfo(promotionOrders);
        outputView.printTotalPriceInfo(totalQuantity, totalOrderPrice, totalPromotionPrice, totalDiscountPrice,
                totalPurchasePrice);
    }

    private int[] getTotalPriceInfo(List<Order> buyOrders) {
        int totalQuantity = buyOrders.stream().mapToInt(Order::getQuantity).sum();
        int totalOrderPrice = receiptService.getTotalOrderPrice();
        int totalPromotionPrice = receiptService.getTotalPromotionPrice();
        int totalDiscountPrice = receiptService.getTotalDiscountPrice();
        int totalPurchasePrice = receiptService.getTotalPurchasePrice();

        return new int[]{totalQuantity, totalOrderPrice, totalPromotionPrice, totalDiscountPrice, totalPurchasePrice};
    }
}
