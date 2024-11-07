package store.service;

import store.implement.ProductRepositoryImpl;
import store.model.Product;
import store.utils.FileUtils;
import store.utils.StringUtils;

import java.util.List;

public class ProductService {
    private final ProductRepositoryImpl productRepository;

    public ProductService(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    public void save(String filename) {
        List<String> lines = FileUtils.readFile(filename);
        lines.stream().skip(1).forEach(this::create);
    }

    public List<Product> get() {
        return productRepository.getProducts();
    }

    private void create(String line) {
        Product product = process(line);
        saveToRepository(product);
    }

    private Product process(String line) {
        String[] productInfo = StringUtils.splitStringWithComma(line);

        String name = productInfo[0];
        int price = Integer.parseInt(productInfo[1]);
        int quantity = Integer.parseInt(productInfo[2]);
        String promotion = null;

        if (!productInfo[3].trim().equals("null")) {
            promotion = productInfo[3];
        }

        return new Product(name, price, quantity, promotion);
    }

    private void saveToRepository(Product product) {
        if (product != null) {
            productRepository.addProduct(product);
        }
    }
}
