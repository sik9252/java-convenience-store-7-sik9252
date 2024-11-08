package store.implement;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    public boolean isProductExist(String name) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(name));
    }

    public boolean isQuantityAvailable(String name, int quantityToBuy) {
        int totalQuantity = products.stream()
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getQuantity)
                .sum();

        return totalQuantity >= quantityToBuy;
    }

    public int getProductPrice(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .map(Product::getPrice)
                .findFirst()
                .orElse(0);
    }

    public int getProductQuantity(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .map(Product::getQuantity)
                .findFirst()
                .orElse(0);
    }

    public String getProductPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .map(Product::getPromotion)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public void decreaseProductQuantity(String productName, int quantityToDecrease) {
        for (Product product : products) {
            if (!product.getName().equals(productName)) {
                continue;
            }

            int currentQuantity = product.getQuantity();
            int newQuantity = currentQuantity - quantityToDecrease;

            if (newQuantity >= 0) {
                product.setQuantity(newQuantity);
                break;
            }

            product.setQuantity(0);
            quantityToDecrease -= currentQuantity;
        }
    }
}
