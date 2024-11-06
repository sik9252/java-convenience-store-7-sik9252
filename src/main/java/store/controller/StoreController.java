package store.controller;

import store.factory.ProductFactory;
import store.view.OutputView;

public class StoreController {
    ProductController productController = ProductFactory.createProductController();

    private final OutputView outputView;

    public StoreController() {
        outputView = new OutputView(productController);
    }

    public void run() {
        productController.createProductsFromResource("products.md");
        outputView.printWelcome();
        outputView.printProducts();
    }
}
