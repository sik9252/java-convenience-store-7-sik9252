package store.controller;

import store.exception.CustomException;
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
        checkProductAvailableToBuy(input);
        createOrdersFromUserInput(input);
    }

    private void checkProductAvailableToBuy(String input) {
        String[] userOrder = StringUtils.splitStringWithComma(input);

        for (String order : userOrder) {
            String[] processedInfo = order.replace("[", "").replace("]", "").
                    split("-");
            String productName = processedInfo[0];
            int requestQuantity = Integer.parseInt(processedInfo[1]);

            checkProduct(productName, requestQuantity);
        }
    }

    private void createOrdersFromUserInput(String input) {
        String[] userOrder = StringUtils.splitStringWithComma(input);

        for (String order : userOrder) {
            String[] processedInfo = order.replace("[", "").replace("]", "").
                    split("-");
            String productName = processedInfo[0];
            int price = productService.getProductPrice(productName);
            int requestQuantity = Integer.parseInt(processedInfo[1]);

            createOrder(productName, price, requestQuantity);
        }
    }

    private void createOrder(String productName, int price, int requestQuantity) {
        if (promotionService.getPromotionInfo(productName) == null) {
            orderService.createTotalOrder(productName, price, requestQuantity);
            return;
        }

        createOrderWhenPromotionIsExist(productName, price, requestQuantity);
    }

    private void createOrderWhenPromotionIsExist(String productName, int price, int requestQuantity) {
        int buy = promotionService.getPromotionInfo(productName)[0];
        int get = promotionService.getPromotionInfo(productName)[1];
        int diff = (((requestQuantity / (buy + get)) + 1) * (buy + get)) - requestQuantity;
        int freeQuantity = requestQuantity / (buy + get);
        int availableQuantityCanBuy = productService.getProductQuantity(productName);

        if (availableQuantityCanBuy < requestQuantity + freeQuantity) {
            handleInsufficientStock(productName, price, requestQuantity, buy, get, availableQuantityCanBuy);
            return;
        }

        if (requestQuantity == 2 && buy + get == 3 || requestQuantity == 1 && buy + get == 2) {
            freeQuantity = 1;
        }

        createOrderWhenPromotionIsOnePlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
        createOrderWhenPromotionIsTwoPlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
    }

    private void handleInsufficientStock(String productName, int price, int requestQuantity,
                                         int buy, int get, int availableQuantityCanBuy) {
        int canBuy = calculateCanBuyQuantity(requestQuantity, buy, get, availableQuantityCanBuy);
        int exceedQuantity = requestQuantity - canBuy;

        while (true) {
            try {
                tryInputByExceed(productName, price, requestQuantity, exceedQuantity, canBuy, buy, get);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int calculateCanBuyQuantity(int requestQuantity, int buy, int get, int availableQuantityCanBuy) {
        int calc = ((requestQuantity / (buy + get))) * (buy + get);

        if (calc < availableQuantityCanBuy) {
            return calc;
        }

        return (((availableQuantityCanBuy / (buy + get))) * (buy + get));
    }

    private void tryInputByExceed(String productName, int price, int requestQuantity,
                                  int exceedQuantity, int canBuy, int buy, int get) {
        String input = inputView.getAnswerToPromotionIsOutOfStock(productName, exceedQuantity);

        orderService.checkIsValidAnswerToPromotionInfo(input);
        orderService.createOrderWhenAnswerIsYes(input, productName, price, requestQuantity,
                canBuy / (buy + get));
        orderService.createOrderWhenAnswerIsNo(input, productName, price, requestQuantity,
                canBuy / (buy + get), buy);
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
            orderService.createTotalOrder(productName, price, requestQuantity);
            orderService.createFreeOrderByPromotion(productName, price, freeQuantity);
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
        orderService.checkIsValidAnswerToPromotionInfo(input);

        if (buy == 2 && requestQuantity == 2 || buy == 1 && requestQuantity == 1) {
            orderService.createOrderWhenAnswerIsYes(input, productName, price, requestQuantity + 1,
                    freeQuantity);

        }

        if (buy == 2 && requestQuantity != 2 || buy == 1 && requestQuantity != 1) {
            orderService.createOrderWhenAnswerIsYes(input, productName, price, requestQuantity + 1,
                    freeQuantity + 1);

        }

        orderService.createOrderWhenAnswerIsNo(input, productName, price, requestQuantity, freeQuantity, buy);
    }

    private void createOrderWhenDiffIsTwo(String productName, int price, int requestQuantity, int freeQuantity,
                                          int buy) {
        orderService.createTotalOrder(productName, price, requestQuantity);
        if (requestQuantity != buy) {
            orderService.createFreeOrderByPromotion(productName, price, freeQuantity);
        }
    }

    private void checkProduct(String productName, int requestQuantity) {
        productService.checkProductExist(productName);
        productService.checkQuantityAvailableToBuy(productName, requestQuantity);
    }

    private void checkInputIsEmpty(String input) {
        if (input.isEmpty()) {
            throw new CustomException(INVALID_INPUT.getMessage() + "\n");
        }
    }

    private void checkInputHasValidFormat(String input) {
        if (!input.matches(PATTERN)) {
            throw new CustomException(INVALID_PURCHASE_FORMAT.getMessage() + "\n");
        }
    }
}
