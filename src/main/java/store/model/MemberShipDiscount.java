package store.model;

public class MemberShipDiscount {
    private int discountPrice;

    public MemberShipDiscount(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }
}
