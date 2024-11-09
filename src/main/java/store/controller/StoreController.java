package store.controller;

import store.implement.OrderRepositoryImpl;
import store.implement.ProductRepositoryImpl;
import store.implement.PromotionRepositoryImpl;
import store.model.Discount;
import store.model.Receipt;
import store.service.DiscountService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final ProductController productController;
    private final PromotionController promotionController;
    private final OrderController orderController;

    private final OutputView outputView;

    public StoreController() {
        Receipt receipt = new Receipt(0, 0, 0, 0);
        Discount discount = new Discount(0);
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        PromotionRepositoryImpl promotionRepository = new PromotionRepositoryImpl();

        InputView inputView = new InputView();

        ProductService productService = new ProductService(productRepository);
        PromotionService promotionService = new PromotionService(promotionRepository);
        OrderService orderService = new OrderService(orderRepository);
        DiscountService disCountService = new DiscountService(discount);
        ReceiptService receiptService = new ReceiptService(orderRepository, discount, receipt);

        productController = new ProductController(productService);
        promotionController = new PromotionController(promotionService);
        orderController = new OrderController(inputView, orderService, productService, promotionService,
                disCountService, receiptService);

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
