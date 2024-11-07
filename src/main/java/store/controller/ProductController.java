package store.controller;

import store.model.Product;
import store.service.ProductService;

import java.util.List;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void createProductsFromResource(String filename) {
        productService.save(filename);
    }

    public String convertProductFormatToPrint() {
        List<Product> products = productService.get();
        StringBuilder result = new StringBuilder();

        for (Product product : products) {
            String quantityInfo = getQuantityInfo(product);
            String promotionInfo = getPromotionInfo(product);

            result.append(String.format("- %s %,d원 %s%s%n", product.getName(), product.getPrice(),
                    quantityInfo, promotionInfo));
        }

        return result.toString();
    }

    private String getQuantityInfo(Product product) {
        if (product.getQuantity() > 0) {
            return product.getQuantity() + "개";
        }

        return "재고 없음";
    }

    private String getPromotionInfo(Product product) {
        if (product.getPromotion() != null) {
            return " " + product.getPromotion();
        }

        return "";
    }
}
