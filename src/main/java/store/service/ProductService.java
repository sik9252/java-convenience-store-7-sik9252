package store.service;

import store.exception.CustomException;
import store.repository.ProductRepositoryImpl;
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

        for (int i = 1; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            String nextLine = (i + 1 < lines.size()) ? lines.get(i + 1) : null;

            create(currentLine, nextLine);
        }
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
        if (!productRepository.checkIsProductExistInStore(productName)) {
            throw new CustomException(PRODUCT_NOT_EXIST.getMessage() + "\n");
        }
    }

    public void checkQuantityAvailableToBuy(String productName, int quantityToBuy) {
        if (!productRepository.checkIsQuantityAvailableToBuy(productName, quantityToBuy)) {
            throw new CustomException(EXCEEDED_STOCK_QUANTITY.getMessage() + "\n");
        }
    }

    private void create(String line, String nextLine) {
        Product product = process(line, nextLine);
        saveToRepository(product);
    }

    private Product process(String line, String nextLine) {
        String[] productInfo = StringUtils.splitStringWithComma(line);
        String name = productInfo[0];
        int price = Integer.parseInt(productInfo[1]);
        int quantity = Integer.parseInt(productInfo[2]);
        String promotion = null;

        if (!productInfo[3].trim().equals("null")) {
            promotion = productInfo[3];

            if (nextLine != null) {
                String[] nextProductInfo = StringUtils.splitStringWithComma(nextLine);
                String nextName = nextProductInfo[0];

                if (!name.equals(nextName)) {
                    Product product1 = new Product(name, price, quantity, promotion);
                    Product product2 = new Product(name, price, 0, null);
                    saveToRepository(product1);
                    saveToRepository(product2);
                    return null;
                }
            }
        }

        return new Product(name, price, quantity, promotion);
    }

    private void saveToRepository(Product product) {
        if (product != null) {
            productRepository.saveProduct(product);
        }
    }
}
