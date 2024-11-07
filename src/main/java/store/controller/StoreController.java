package store.controller;

import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    ProductController productController;
    OrderController orderController;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController() {
        productController = ProductFactory.createProductController();
        orderController = OrderFactory.createOrderController();
        outputView = new OutputView(productController);
        inputView = new InputView();
    }

    public void run() {
        productController.createProductsFromResource("products.md");
        outputView.printWelcome();
        outputView.printProducts();
        orderController.order();
    }
}
