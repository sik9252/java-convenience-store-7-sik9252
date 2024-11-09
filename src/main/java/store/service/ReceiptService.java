package store.service;

import store.implement.OrderRepositoryImpl;
import store.model.MemberShipDiscount;
import store.model.Order;
import store.model.Receipt;

public class ReceiptService {
    private final Receipt receipt;
    private final OrderRepositoryImpl orderRepository;
    private final MemberShipDiscount memberShipDiscount;

    public ReceiptService(OrderRepositoryImpl orderRepository, MemberShipDiscount memberShipDiscount, Receipt receipt) {
        this.orderRepository = orderRepository;
        this.memberShipDiscount = memberShipDiscount;
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

    public void calculate() {
        calcTotalOrderPrice();
        calcTotalPromotionPrice();
        calcTotalDiscountPrice();
        calcTotalPurchasePrice();
    }

    private void calcTotalOrderPrice() {
        int totalOrderPrice = orderRepository.getBuyOrders().stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        receipt.setTotalOrderPrice(totalOrderPrice);
    }

    private void calcTotalPromotionPrice() {
        int totalPromotionPrice = orderRepository.getPromotionOrders().stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        receipt.setTotalPromotionPrice(totalPromotionPrice);
    }

    private void calcTotalDiscountPrice() {
        receipt.setTotalDiscountPrice(memberShipDiscount.getDiscountPrice());
    }

    private void calcTotalPurchasePrice() {
        int totalOrderPrice = receipt.getTotalOrderPrice();
        int totalPromotionPrice = receipt.getTotalPromotionPrice();
        int totalDiscountPrice = memberShipDiscount.getDiscountPrice();

        receipt.setTotalPurchasePrice(totalOrderPrice - totalPromotionPrice - totalDiscountPrice);
    }
}
