package store.factory;

import store.implement.ProductRepositoryImpl;
import store.service.ProductService;

public class ProductFactory {
    public static ProductService createProductService() {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();

        return new ProductService(productRepository);
    }
}
