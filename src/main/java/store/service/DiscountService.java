package store.service;

import store.model.Discount;

public class DiscountService {
    private final Discount discount;

    public DiscountService(Discount discount) {
        this.discount = discount;
    }

    public void setDiscountPrice(int discountPrice) {
        discount.setDiscountPrice(discountPrice);
    }
}
