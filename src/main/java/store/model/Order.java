package store.model;

public class Order {
    private final String name;
    private int buyQuantity;
    private int freeQuantity;
    private int totalPrice;

    public Order(String name, int price, int buyQuantity) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.totalPrice = 0;
        calculateTotalPrice(price, buyQuantity);
    }

    public Order(String name, int freeQuantity) {
        this.name = name;
        this.freeQuantity = freeQuantity;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    private void calculateTotalPrice(int price, int buyQuantity) {
        totalPrice = buyQuantity * price;
    }
}
