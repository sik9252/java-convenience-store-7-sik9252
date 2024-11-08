package store.model;

public class Order {
    private final String name;
    private final int quantity;
    private int totalPrice;

    public Order(String name, int price, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = 0;
        getTotalPrice(price, quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    private void getTotalPrice(int price, int buyQuantity) {
        totalPrice = buyQuantity * price;
    }
}
