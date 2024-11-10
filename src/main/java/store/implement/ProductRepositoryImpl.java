package store.implement;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @Override
    public void saveProduct(Product product) {
        products.add(product);
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    public boolean checkIsProductExistInStore(String name) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(name));
    }

    public boolean checkIsQuantityAvailableToBuy(String name, int quantityToBuy) {
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
            if (isMatchingProduct(product, productName)) {
                quantityToDecrease = processQuantityDecrease(product, quantityToDecrease);
                if (quantityToDecrease == 0) break;
            }
        }
    }

    private boolean isMatchingProduct(Product product, String productName) {
        return product.getName().equals(productName);
    }

    private int processQuantityDecrease(Product product, int quantityToDecrease) {
        int currentQuantity = product.getQuantity();
        int newQuantity = calculateNewQuantity(currentQuantity, quantityToDecrease);
        updateProductQuantity(product, newQuantity);
        return calculateRemainingQuantityToDecrease(quantityToDecrease, currentQuantity);
    }

    private int calculateNewQuantity(int currentQuantity, int quantityToDecrease) {
        return currentQuantity - quantityToDecrease;
    }

    private void updateProductQuantity(Product product, int newQuantity) {
        product.setQuantity(Math.max(newQuantity, 0));
    }

    private int calculateRemainingQuantityToDecrease(int quantityToDecrease, int currentQuantity) {
        return Math.max(quantityToDecrease - currentQuantity, 0);
    }

}
