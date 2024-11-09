package store.model;

public class Order {
    private final String name;
    private final int quantity;
    private final int totalPrice;

    public Order(String name, int price, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = getTotalPrice(price, quantity);
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

    private int getTotalPrice(int price, int buyQuantity) {
        return buyQuantity * price;
    }
}
