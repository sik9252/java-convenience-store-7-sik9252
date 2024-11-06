package store.service;

import store.implement.ProductRepositoryImpl;
import store.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static store.exception.ErrorMessage.FAILED_READ_FILE;

public class ProductService {
    private final ProductRepositoryImpl productRepositoryImpl;

    public ProductService(ProductRepositoryImpl productRepositoryImpl) {
        this.productRepositoryImpl = productRepositoryImpl;
    }

    public void createProductsFromResource(String filename) {
        List<String> lines = readFileFromLines(filename);
        lines.stream().skip(1).forEach(this::addCreatedProduct);
    }

    public List<Product> getProducts() {
        return productRepositoryImpl.getAllProducts();
    }

    private List<String> readFileFromLines(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(ProductService.class.getClassLoader().getResourceAsStream(filename))))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(FAILED_READ_FILE.getMessage());
        }

        return lines;
    }

    private void addCreatedProduct(String line) {
        Product product = createProduct(line);
        addProductToRepository(product);
    }

    private Product createProduct(String line) {
        String[] info = line.split(",");

        String name = info[0];
        int price = Integer.parseInt(info[1]);
        int quantity = Integer.parseInt(info[2]);
        String promotion = null;

        if (!info[3].trim().equals("null")) {
            promotion = info[3];
        }

        return new Product(name, price, quantity, promotion);
    }

    private void addProductToRepository(Product product) {
        if (product != null) {
            productRepositoryImpl.addProduct(product);
        }
    }
}
