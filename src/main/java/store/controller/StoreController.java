package store.controller;

import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final OrderController orderController;
    private final ProductService productService;
    private final PromotionService promotionService;

    public StoreController() {
        productService = ProductFactory.createProductService();
        promotionService = PromotionFactory.createPromotionService();
        ProductController productController = new ProductController(productService);
        orderController = OrderFactory.createOrderController();
        outputView = new OutputView(productController);
        inputView = new InputView();
    }

    public void run() {
        productService.save("products.md");
        promotionService.save("promotions.md");
        outputView.printWelcome();
        outputView.printProducts();
        orderController.order();
    }
}
