package store.controller;

import store.exception.CustomException;
import store.implement.OrderRepositoryImpl;
import store.implement.ProductRepositoryImpl;
import store.implement.PromotionRepositoryImpl;
import store.model.MemberShipDiscount;
import store.model.Receipt;
import store.service.MemberShipDiscountService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;

import static store.exception.ErrorMessage.INVALID_INPUT;

public class StoreController {
    private MemberShipDiscount memberShipDiscount;
    private Receipt receipt;

    private OrderRepositoryImpl orderRepository;

    private ProductService productService;
    private PromotionService promotionService;
    private OrderService orderService;
    private MemberShipDiscountService memberShipDiscountService;
    private ReceiptService receiptService;

    private final ProductController productController;
    private final PromotionController promotionController;
    private OrderController orderController;
    private MemberShipDiscountController memberShipDiscountController;
    private ReceiptController receiptController;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController() {
        memberShipDiscount = new MemberShipDiscount(0);
        receipt = new Receipt(0, 0, 0, 0);

        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        //orderRepository = new OrderRepositoryImpl();
        PromotionRepositoryImpl promotionRepository = new PromotionRepositoryImpl();

        productService = new ProductService(productRepository);
        promotionService = new PromotionService(promotionRepository, productService);
        //orderService = new OrderService(orderRepository, productService);
        memberShipDiscountService = new MemberShipDiscountService(memberShipDiscount);
        receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);

        productController = new ProductController(productService);
        promotionController = new PromotionController(promotionService);
        //orderController = new OrderController(inputView, orderService, productService, promotionService);
        //memberShipDiscountController = new MemberShipDiscountController(orderService, memberShipDiscountService,
        // inputView);

        inputView = new InputView();
        outputView = new OutputView(productController);
        //receiptController = new ReceiptController(orderService, receiptService, outputView);
    }

    public void run() {
        productController.saveProducts();
        promotionController.savePromotion();
        while (true) {
            try {
                init();
                if (!continueToBuy()) {
                    break;
                }
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean continueToBuy() {
        while (true) {
            try {
                String input = inputView.getAnswerToContinue();
                checkIsValidAnswerToPromotionInfo(input);

                if (input.equals("Y")) {
                    return true;
                } else if (input.equals("N")) {
                    return false;
                }
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void init() {
        orderRepository = new OrderRepositoryImpl();
        orderService = new OrderService(orderRepository, productService);
        orderController = new OrderController(inputView, orderService, productService, promotionService);
        memberShipDiscountController = new MemberShipDiscountController(orderService, memberShipDiscountService,
                inputView);
        receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);
        receiptController = new ReceiptController(orderService, receiptService, outputView);

        outputView.printWelcome();
        outputView.printProducts();
        orderController.order();
        memberShipDiscountController.discountWithMemberShip();
        receiptController.makeReceipt();
    }

    public void checkIsValidAnswerToPromotionInfo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }
}
