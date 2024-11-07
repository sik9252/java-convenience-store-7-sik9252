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

    public void createProductsFromResource(String filename) {
        List<String> lines = FileUtils.readFile(filename);
        lines.stream().skip(1).forEach(this::addCreatedProduct);
    }

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    private void addCreatedProduct(String line) {
        Product product = createProduct(line);
        addProductToRepository(product);
    }

    private Product createProduct(String line) {
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

    private void addProductToRepository(Product product) {
        if (product != null) {
            productRepository.addProduct(product);
        }
    }
}
