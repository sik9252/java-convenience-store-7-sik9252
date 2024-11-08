package store.controller;

import store.implement.OrderRepositoryImpl;
import store.implement.ProductRepositoryImpl;
import store.implement.PromotionRepositoryImpl;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final ProductRepositoryImpl productRepository;
    private final PromotionRepositoryImpl promotionRepository;
    private final OrderRepositoryImpl orderRepository;

    private final ProductService productService;
    private final PromotionService promotionService;
    private final OrderService orderService;

    private final ProductController productController;
    private final PromotionController promotionController;
    private final OrderController orderController;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController() {
        productRepository = new ProductRepositoryImpl();
        orderRepository = new OrderRepositoryImpl();
        promotionRepository = new PromotionRepositoryImpl();

        inputView = new InputView();

        productService = new ProductService(productRepository);
        promotionService = new PromotionService(promotionRepository);
        orderService = new OrderService(orderRepository);

        productController = new ProductController(productService);
        promotionController = new PromotionController(promotionService);
        orderController = new OrderController(inputView, orderService, productService, promotionService);

        outputView = new OutputView(productController);
    }

    public void run() {
        productController.saveProducts();
        promotionController.savePromotion();
        outputView.printWelcome();
        outputView.printProducts();
        orderController.order();
    }
}
