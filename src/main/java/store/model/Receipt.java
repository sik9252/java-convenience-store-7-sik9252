package store.model;

public class Receipt {
    private int totalOrderPrice;
    private int totalPromotionPrice;
    private int totalDiscountPrice;
    private int totalPurchasePrice;


    public Receipt() {
        this.totalOrderPrice = 0;
        this.totalPromotionPrice = 0;
        this.totalDiscountPrice = 0;
        this.totalPurchasePrice = 0;
    }

    public int getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public int getTotalPromotionPrice() {
        return totalPromotionPrice;
    }

    public int getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public int getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void setTotalOrderPrice(int totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public void setTotalPromotionPrice(int totalPromotionPrice) {
        this.totalPromotionPrice = totalPromotionPrice;
    }

    public void setTotalDiscountPrice(int totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public void setTotalPurchasePrice(int totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }
}
