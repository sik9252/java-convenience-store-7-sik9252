package store.service;

import store.implement.OrderRepositoryImpl;
import store.model.Discount;
import store.model.Order;
import store.model.Receipt;

public class ReceiptService {
    private final Receipt receipt;
    private final OrderRepositoryImpl orderRepository;
    private final Discount discount;

    public ReceiptService(OrderRepositoryImpl orderRepository, Discount discount, Receipt receipt) {
        this.orderRepository = orderRepository;
        this.discount = discount;
        this.receipt = receipt;
    }

    public int getTotalOrderPrice() {
        return receipt.getTotalOrderPrice();
    }

    public int getTotalPromotionPrice() {
        return receipt.getTotalPromotionPrice();
    }

    public int getTotalDiscountPrice() {
        return receipt.getTotalDiscountPrice();
    }

    public int getTotalPurchasePrice() {
        return receipt.getTotalPurchasePrice();
    }

    public void calcTotalOrderPrice() {
        int totalOrderPrice = orderRepository.getBuyOrders().stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        receipt.setTotalOrderPrice(totalOrderPrice);
    }

    public void calcTotalPromotionPrice() {
        int totalPromotionPrice = orderRepository.getPromotionOrders().stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        receipt.setTotalPromotionPrice(totalPromotionPrice);
    }

    public void calcTotalDiscountPrice() {
        receipt.setTotalDiscountPrice(discount.getDiscountPrice());
    }

    public void calcTotalPurchasePrice() {
        int totalOrderPrice = receipt.getTotalOrderPrice();
        int totalPromotionPrice = receipt.getTotalPromotionPrice();
        int totalDiscountPrice = discount.getDiscountPrice();

        receipt.setTotalPurchasePrice(totalOrderPrice - totalPromotionPrice - totalDiscountPrice);
    }
}
