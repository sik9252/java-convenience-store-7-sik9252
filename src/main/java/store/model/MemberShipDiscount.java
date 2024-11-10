package store.model;

public class MemberShipDiscount {
    private int discountPrice;

    public MemberShipDiscount() {
        this.discountPrice = 0;
    }

    public int getMemberShipDiscountPrice() {
        return discountPrice;
    }

    public void setMemberShipDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }
}
