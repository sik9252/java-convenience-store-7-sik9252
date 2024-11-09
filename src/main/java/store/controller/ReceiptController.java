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
        List<Order> buyOrders = orderService.getBuyOrders();
        List<Order> promotionOrders = orderService.getPromotionOrders();
        int[] totalPriceInfos = getTotalPriceInfos(buyOrders);

        printInfo(buyOrders, promotionOrders, totalPriceInfos[0], totalPriceInfos[1], totalPriceInfos[2],
                totalPriceInfos[3], totalPriceInfos[4]);
    }

    private void printInfo(List<Order> buyOrders, List<Order> promotionOrders, int totalQuantity, int totalOrderPrice
            , int totalPromotionPrice, int totalDiscountPrice, int totalPurchasePrice) {
        outputView.printTotalOrderInfo(buyOrders);
        outputView.printTotalPromotionInfo(promotionOrders);
        outputView.printTotalPriceInfo(totalQuantity, totalOrderPrice, totalPromotionPrice, totalDiscountPrice,
                totalPurchasePrice);
    }

    private int[] getTotalPriceInfos(List<Order> buyOrders) {
        int totalQuantity = buyOrders.stream().mapToInt(Order::getQuantity).sum();
        int totalOrderPrice = receiptService.getTotalOrderPrice();
        int totalPromotionPrice = receiptService.getTotalPromotionPrice();
        int totalDiscountPrice = receiptService.getTotalDiscountPrice();
        int totalPurchasePrice = receiptService.getTotalPurchasePrice();

        return new int[]{totalQuantity, totalOrderPrice, totalPromotionPrice, totalDiscountPrice, totalPurchasePrice};
    }
}
