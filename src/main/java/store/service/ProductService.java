package store.service;

import store.exception.CustomException;
import store.implement.ProductRepositoryImpl;
import store.model.Product;
import store.utils.FileUtils;
import store.utils.StringUtils;

import java.util.List;

import static store.exception.ErrorMessage.EXCEEDED_STOCK_QUANTITY;
import static store.exception.ErrorMessage.PRODUCT_NOT_EXIST;

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

    public int getProductPrice(String productName) {
        return productRepository.getProductPrice(productName);
    }

    public int getProductQuantity(String productName) {
        return productRepository.getProductQuantity(productName);
    }

    public String getProductPromotion(String productName) {
        return productRepository.getProductPromotion(productName);
    }

    public void decreaseProductQuantity(String productName, int quantity) {
        productRepository.decreaseProductQuantity(productName, quantity);
    }

    public void checkProductExist(String productName) {
        if (!productRepository.isProductExist(productName)) {
            throw new CustomException(PRODUCT_NOT_EXIST.getMessage() + "\n");
        }
    }

    public void checkQuantityAvailableToBuy(String productName, int quantityToBuy) {
        if (!productRepository.isQuantityAvailable(productName, quantityToBuy)) {
            throw new CustomException(EXCEEDED_STOCK_QUANTITY.getMessage() + "\n");
        }
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
