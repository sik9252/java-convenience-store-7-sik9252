package store.factory;

import store.controller.ProductController;
import store.implement.ProductRepositoryImpl;
import store.service.ProductService;

public class ProductFactory {
    public static ProductController createProductController() {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(productRepository);
        return new ProductController(productService);
    }
}
