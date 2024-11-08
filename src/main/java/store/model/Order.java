package store.model;

public class Order {
    private final String name;
    private final int quantity;
    private final int totalPrice;

    public Order(String name, int quantity, int totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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
}
