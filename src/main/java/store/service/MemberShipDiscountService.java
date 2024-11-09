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
        memberShipDiscount.setDiscountPrice(discountPrice);
    }

    public void calculateDiscountPrice(List<Order> list) {
        int totalPrice = list.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        int discountPrice = (int) (totalPrice * 0.3);
        discountPrice = Math.min(discountPrice, 8000);

        setDiscountPrice(discountPrice);
    }
}
