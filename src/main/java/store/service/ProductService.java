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

    public void saveProduct(String filename) {
        List<String> lines = FileUtils.readFile(filename);

        for (int i = 1; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            String nextLine = (i + 1 < lines.size()) ? lines.get(i + 1) : null;

            create(currentLine, nextLine);
        }
    }

    public List<Product> getProducts() {
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
        String promotion = getPromotion(productInfo[3]);

        if (isPromotionDifferent(name, promotion, nextLine)) {
            handleAddGeneralProduct(name, price, quantity, promotion);
            return null;
        }

        return new Product(name, price, quantity, promotion);
    }

    private String getPromotion(String promotionInfo) {
        return promotionInfo.trim().equals("null") ? null : promotionInfo;
    }

    private boolean isPromotionDifferent(String name, String promotion, String nextLine) {
        if (promotion == null || nextLine == null) return false;
        String nextName = StringUtils.splitStringWithComma(nextLine)[0];
        return !name.equals(nextName);
    }

    private void handleAddGeneralProduct(String name, int price, int quantity, String promotion) {
        Product productWithPromotion = new Product(name, price, quantity, promotion);
        Product productWithoutPromotion = new Product(name, price, 0, null);
        saveToRepository(productWithPromotion);
        saveToRepository(productWithoutPromotion);
    }

    private void saveToRepository(Product product) {
        if (product != null) {
            productRepository.saveProduct(product);
        }
    }
}
