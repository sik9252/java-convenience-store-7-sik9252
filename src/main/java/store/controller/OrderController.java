package store.controller;

import store.exception.CustomException;
import store.model.Order;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.utils.StringUtils;
import store.view.InputView;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.INVALID_PURCHASE_FORMAT;


public class OrderController {
    private static final String PATTERN = "\\[([가-힣A-Za-z]+)-([1-9][0-9]*)](,\\[([가-힣A-Za-z]+)-([1-9][0-9]*)])*";

    private final InputView inputView;
    private final OrderService orderService;
    private final ProductService productService;
    private final PromotionService promotionService;

    public OrderController(
            InputView inputView,
            OrderService orderService,
            ProductService productService,
            PromotionService promotionService) {
        this.inputView = inputView;
        this.orderService = orderService;
        this.productService = productService;
        this.promotionService = promotionService;
    }

    public void order() {
        while (true) {
            try {
                String input = inputView.getOrders();
                checkInputIsEmpty(input);
                checkInputHasValidFormat(input);
                createEntireOrder(input);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createEntireOrder(String input) {
        String[] orderInfo = StringUtils.splitStringWithComma(input);

        for (String info : orderInfo) {
            String[] processedInfo = info.replace("[", "").replace("]", "").
                    split("-");
            String productName = processedInfo[0];
            int price = productService.getProductPrice(productName);
            int buyQuantity = Integer.parseInt(processedInfo[1]);

            productService.checkProductExist(productName);
            productService.checkQuantityAvailableToBuy(productName, buyQuantity);
            int freeQuantity = getFreeQuantity(productName, buyQuantity);

            createBuyOrder(productName, price, buyQuantity + freeQuantity);
        }
    }

    private int getFreeQuantity(String productName, int buyQuantity) {
        String promotionName = productService.getProductPromotion(productName);

        if (promotionName != null) {
            int[] benefit = promotionService.getBenefitOfPromotion(promotionName);
            int buy = benefit[0];
            int get = benefit[1];

            return compareQuantityToGetFree(productName, buyQuantity, buy, get);
        }

        return 0;
    }

    public int compareQuantityToGetFree(String productName, int buyQuantity, int buy, int get) {
        int promotionSet = buyQuantity / buy;
        int remainder = Math.abs((buy + get) * promotionSet - buyQuantity);

        if (remainder > 0) {
            while (true) {
                try {
                    String input = inputView.getAnswerToPromotionInfo(productName, remainder);
                    checkIsValidAnswerToPromotionInfo(input);

                    if (input.equals("Y")) {
                        createPromotionOrder(productName, remainder);
                    }

                    if (input.equals("N")) {
                        return 0;
                    }

                    break;
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return remainder;
    }

    private void checkInputIsEmpty(String input) {
        if (input.isEmpty()) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }

    private void checkInputHasValidFormat(String input) {
        if (!input.matches(PATTERN)) {
            throw new CustomException(INVALID_PURCHASE_FORMAT.getMessage());
        }
    }

    private void checkIsValidAnswerToPromotionInfo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }

    public void createBuyOrder(String name, int price, int buyQuantity) {
        Order buyOrder = new Order(name, price, buyQuantity);
        orderService.saveBuyOrderToRepository(buyOrder);
    }

    public void createPromotionOrder(String name, int freeQuantity) {
        if (freeQuantity > 0) {
            Order promotionOrder = new Order(name, freeQuantity);
            orderService.savePromotionOrderToRepository(promotionOrder);
        }
    }
}
