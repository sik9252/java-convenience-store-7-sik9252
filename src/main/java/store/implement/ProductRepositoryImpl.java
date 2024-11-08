package store.implement;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

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

    public String getProductPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .map(Product::getPromotion)
                .findFirst()
                .orElse(null);
    }
}
