package store.controller;

import store.exception.CustomException;
import store.model.Order;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.utils.StringUtils;
import store.view.InputView;

import java.util.Objects;

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
                tryOrderInput();
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void tryOrderInput() {
        String input = inputView.getOrders();
        checkInputIsEmpty(input);
        checkInputHasValidFormat(input);
        createOrdersFromUserInput(input);
    }

    private void createOrdersFromUserInput(String input) {
        String[] userOrder = StringUtils.splitStringWithComma(input);

        for (String order : userOrder) {
            String[] processedInfo = order.replace("[", "").replace("]", "").
                    split("-");
            String productName = processedInfo[0];
            int price = productService.getProductPrice(productName);
            int requestQuantity = Integer.parseInt(processedInfo[1]);

            checkProduct(productName, requestQuantity);
            createOrder(productName, price, requestQuantity);
        }
    }

    private void createOrder(String productName, int price, int requestQuantity) {
        if (getPromotionInfo(productName) == null) {
            createBuyOrder(productName, price, requestQuantity);
            return;
        }

        createOrderWhenPromotionIsProgressing(productName, price, requestQuantity);
    }

    private void createOrderWhenPromotionIsProgressing(String productName, int price, int requestQuantity) {
        int buy = Objects.requireNonNull(getPromotionInfo(productName))[0];
        int get = Objects.requireNonNull(getPromotionInfo(productName))[1];
        int diff = (((requestQuantity / (buy + get)) + 1) * (buy + get)) - requestQuantity;
        int freeQuantity = requestQuantity / (buy + get);

        if (requestQuantity == 2 && buy + get == 3 || requestQuantity == 1 && buy + get == 2) {
            freeQuantity = 1;
        }

        createOrderWhenPromotionIsOnePlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
        createOrderWhenPromotionIsTwoPlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
    }

    private void createOrderWhenPromotionIsTwoPlusOne(String productName, int price, int requestQuantity, int diff,
                                                      int freeQuantity, int buy, int get) {
        if (buy == 2) {
            createOrderWhenZeroRemainder(productName, price, requestQuantity, freeQuantity, buy, get);
            if (diff == 1) {
                createOrderWhenDiffIsOne(productName, price, requestQuantity, freeQuantity, buy);
            }
            if (diff == 2) {
                createOrderWhenDiffIsTwo(productName, price, requestQuantity, freeQuantity, buy);
            }
        }
    }


    private void createOrderWhenPromotionIsOnePlusOne(String productName, int price, int requestQuantity, int diff,
                                                      int freeQuantity, int buy, int get) {
        if (buy == 1) {
            createOrderWhenZeroRemainder(productName, price, requestQuantity, freeQuantity, buy, get);
            if (diff == 1) {
                createOrderWhenDiffIsOne(productName, price, requestQuantity, freeQuantity, buy);
            }
        }
    }

    private void createOrderWhenZeroRemainder(String productName, int price, int requestQuantity, int freeQuantity,
                                              int buy, int get) {
        if (requestQuantity % (buy + get) == 0) {
            createBuyOrder(productName, price, requestQuantity);
            createPromotionOrder(productName, price, freeQuantity);
        }
    }

    private void createOrderWhenDiffIsOne(String productName, int price, int requestQuantity, int freeQuantity,
                                          int buy) {
        while (true) {
            try {
                tryInputByPromotion(productName, price, requestQuantity, freeQuantity, buy);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void tryInputByPromotion(String productName, int price, int requestQuantity,
                                     int freeQuantity, int buy) {
        String input = inputView.getAnswerToPromotionInfo(productName);

        checkIsValidAnswerToPromotionInfo(input);
        createOrderWhenAnswerIsYes(input, productName, price, requestQuantity, freeQuantity);
        createOrderWhenAnswerIsNo(input, productName, price, requestQuantity, freeQuantity, buy);
    }

    private void createOrderWhenAnswerIsYes(String input, String productName, int price, int requestQuantity,
                                            int freeQuantity) {
        if (input.equals("Y")) {
            createBuyOrder(productName, price, requestQuantity + freeQuantity);
            createPromotionOrder(productName, price, freeQuantity);
        }
    }

    private void createOrderWhenAnswerIsNo(String input, String productName, int price, int requestQuantity,
                                           int freeQuantity, int buy) {
        if (input.equals("N")) {
            createBuyOrder(productName, price, requestQuantity);
            if (requestQuantity != buy) {
                createPromotionOrder(productName, price, freeQuantity);
            }
        }
    }

    private void createOrderWhenDiffIsTwo(String productName, int price, int requestQuantity, int freeQuantity,
                                          int buy) {
        createBuyOrder(productName, price, requestQuantity);
        if (requestQuantity != buy) {
            createPromotionOrder(productName, price, freeQuantity);
        }
    }

    private int[] getPromotionInfo(String productName) {
        String promotionName = productService.getProductPromotion(productName);
        return promotionService.getPromotionInfo(promotionName);
    }

    private void checkProduct(String productName, int requestQuantity) {
        productService.checkProductExist(productName);
        productService.checkQuantityAvailableToBuy(productName, requestQuantity);
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

    public void createPromotionOrder(String name, int price, int freeQuantity) {
        Order promotionOrder = new Order(name, price, freeQuantity);
        orderService.savePromotionOrderToRepository(promotionOrder);
    }
}
