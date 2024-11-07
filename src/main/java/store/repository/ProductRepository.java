package store.repository;

import store.model.Product;

import java.util.List;

public interface ProductRepository {
    void addProduct(Product product);

    List<Product> getProducts();
}
