package store.service;

import store.model.MemberShipDiscount;
import store.model.Order;

import java.util.List;

public class MemberShipDiscountService {
    private final MemberShipDiscount memberShipDiscount;

    public MemberShipDiscountService(MemberShipDiscount memberShipDiscount) {
        this.memberShipDiscount = memberShipDiscount;
    }

    public void setDiscountPrice(int discountPrice) {
        memberShipDiscount.setMemberShipDiscountPrice(discountPrice);
    }

    public void calculateDiscountPrice(List<Order> totalOrder, List<Order> freeOrder) {
        int totalPrice = totalOrder.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        int freePrice = freeOrder.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        int discountPrice = (int) ((totalPrice - freePrice) * 0.3);
        discountPrice = Math.min(discountPrice, 8000);

        setDiscountPrice(discountPrice);
    }
}
