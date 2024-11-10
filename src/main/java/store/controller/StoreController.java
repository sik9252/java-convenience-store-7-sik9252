package store.controller;

import store.exception.CustomException;
import store.repository.OrderRepositoryImpl;
import store.repository.ProductRepositoryImpl;
import store.repository.PromotionRepositoryImpl;
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
    private final MemberShipDiscount memberShipDiscount;
    private final Receipt receipt;

    private OrderRepositoryImpl orderRepository;

    private final ProductService productService;
    private final PromotionService promotionService;
    private OrderService orderService;
    private final MemberShipDiscountService memberShipDiscountService;
    private ReceiptService receiptService;

    private final ProductController productController;
    private final PromotionController promotionController;
    private OrderController orderController;
    private MemberShipDiscountController memberShipDiscountController;
    private ReceiptController receiptController;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController() {
        memberShipDiscount = new MemberShipDiscount();
        receipt = new Receipt(0, 0, 0, 0);

        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        PromotionRepositoryImpl promotionRepository = new PromotionRepositoryImpl();

        productService = new ProductService(productRepository);
        promotionService = new PromotionService(promotionRepository, productService);
        memberShipDiscountService = new MemberShipDiscountService(memberShipDiscount);
        receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);

        productController = new ProductController(productService);
        promotionController = new PromotionController(promotionService);

        inputView = new InputView();
        outputView = new OutputView(productController);
    }

    public void run() {
        saveStoreInfo();
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

    private void saveStoreInfo() {
        productController.saveProducts();
        promotionController.savePromotions();
    }

    private boolean continueToBuy() {
        while (true) {
            try {
                String input = inputView.getAnswerToContinue();
                checkYorNIsEntered(input);
                return continueWhenAnswerIsYes(input);
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void checkYorNIsEntered(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }

    private void init() {
        initOrder();
        initDiscountAndReceipt();
        outputView.printWelcome();
        outputView.printProducts();
        orderController.order();
        memberShipDiscountController.discountWithMemberShip();
        receiptController.makeReceipt();
    }

    private void initOrder() {
        orderRepository = new OrderRepositoryImpl();
        orderService = new OrderService(orderRepository, productService);
        orderController = new OrderController(inputView, orderService, productService, promotionService);
    }

    private void initDiscountAndReceipt() {
        memberShipDiscountController = new MemberShipDiscountController(orderService, memberShipDiscountService,
                inputView);
        receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);
        receiptController = new ReceiptController(orderService, receiptService, outputView);
    }

    private boolean continueWhenAnswerIsYes(String input) {
        return input.equals("Y");
    }
}
