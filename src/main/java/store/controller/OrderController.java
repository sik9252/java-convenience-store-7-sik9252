package store.controller;

import store.exception.CustomException;
import store.model.Order;
import store.service.MemberShipDiscountService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.ReceiptService;
import store.utils.StringUtils;
import store.view.InputView;

import java.util.List;
import java.util.Objects;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.INVALID_PURCHASE_FORMAT;


public class OrderController {
    private static final String PATTERN = "\\[([가-힣A-Za-z]+)-([1-9][0-9]*)](,\\[([가-힣A-Za-z]+)-([1-9][0-9]*)])*";

    private final InputView inputView;
    private final OrderService orderService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final MemberShipDiscountService memberShipDiscountService;
    private final ReceiptService receiptService;

    public OrderController(
            InputView inputView,
            OrderService orderService,
            ProductService productService,
            PromotionService promotionService,
            MemberShipDiscountService memberShipDiscountService,
            ReceiptService receiptService) {
        this.inputView = inputView;
        this.orderService = orderService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.memberShipDiscountService = memberShipDiscountService;
        this.receiptService = receiptService;
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
        discountWithMemberShip();
        makeReceipt();
    }

    private void makeReceipt() {
        receiptService.calcTotalOrderPrice();
        receiptService.calcTotalPromotionPrice();
        receiptService.calcTotalDiscountPrice();
        receiptService.calcTotalPurchasePrice();

        List<Order> buyOrders = orderService.getBuyOrders();
        List<Order> promotionOrders = orderService.getPromotionOrders();

        int totalOrderPrice = receiptService.getTotalOrderPrice();
        int totalPromotionPrice = receiptService.getTotalPromotionPrice();
        int totalDiscountPrice = receiptService.getTotalDiscountPrice();
        int totalPurchasePrice = receiptService.getTotalPurchasePrice();

        System.out.println("\n==============W 편의점================");
        System.out.printf("%-13s\t%5s\t%7s\n", "상품명", "수량", "금액");

        for (Order order : buyOrders) {
            System.out.printf("%-13s\t%5d\t%,10d\n", order.getName(), order.getQuantity(), order.getTotalPrice());
        }

        System.out.println("=============증    정===============");
        for (Order order : promotionOrders) {
            System.out.printf("%-13s\t%5s\n", order.getName(), order.getQuantity());
        }

        System.out.println("====================================");
        System.out.printf("%-13s\t%5s\t%,10d\n", "총구매액", buyOrders.stream().mapToInt(Order::getQuantity).sum(),
                totalOrderPrice);
        System.out.printf("%-13s\t%18s\n", "행사할인", String.format("-%,d", totalPromotionPrice));
        System.out.printf("%-13s\t%18s\n", "멤버십할인", String.format("-%,d", totalDiscountPrice));
        System.out.printf("%-13s\t%,18d\n", "내실돈", totalPurchasePrice);
    }

    private void discountWithMemberShip() {
        while (true) {
            try {
                String input = inputView.getAnswerToMemberShipDiscount();
                checkIsValidAnswerToPromotionInfo(input);
                calcDiscountWhenAnswerIsYes(input);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void calcDiscountWhenAnswerIsYes(String input) {
        if (input.equals("Y")) {
            List<Order> list = orderService.getNotPromotionProduct();
            memberShipDiscountService.calculateDiscountPrice(list);
        }
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
        int possibleQuantityToBuyPromotion = productService.getProductQuantity(productName);

        if (possibleQuantityToBuyPromotion < requestQuantity + freeQuantity) {
            int canBuy = 0;
            int calc = ((requestQuantity / (buy + get))) * (buy + get);

            if (calc < possibleQuantityToBuyPromotion) {
                canBuy = calc;
            }

            if (calc >= possibleQuantityToBuyPromotion) {
                canBuy = (((possibleQuantityToBuyPromotion / (buy + get))) * (buy + get));
            }

            int exceedQuantity = requestQuantity - canBuy;

            while (true) {
                try {
                    tryInputByExceed(productName, price, requestQuantity, exceedQuantity, canBuy, buy, get);
                    break;
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            }

            return;
        }

        if (requestQuantity == 2 && buy + get == 3 || requestQuantity == 1 && buy + get == 2) {
            freeQuantity = 1;
        }

        createOrderWhenPromotionIsOnePlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
        createOrderWhenPromotionIsTwoPlusOne(productName, price, requestQuantity, diff, freeQuantity, buy, get);
    }

    private void tryInputByExceed(String productName, int price, int requestQuantity,
                                  int exceedQuantity, int canBuy, int buy, int get) {
        String input = inputView.getAnswerToPromotionIsOutOfStock(productName, exceedQuantity);

        checkIsValidAnswerToPromotionInfo(input);
        createOrderWhenAnswerIsYes(input, productName, price, requestQuantity, canBuy / (buy + get));
        createOrderWhenAnswerIsNo(input, productName, price, requestQuantity, canBuy / (buy + get), buy);
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
        createOrderWhenAnswerIsYes(input, productName, price, requestQuantity + 1,
                freeQuantity + 1);
        createOrderWhenAnswerIsNo(input, productName, price, requestQuantity, freeQuantity, buy);
    }

    private void createOrderWhenAnswerIsYes(String input, String productName, int price, int requestQuantity,
                                            int freeQuantity) {
        if (input.equals("Y")) {
            createBuyOrder(productName, price, requestQuantity);
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
            throw new CustomException(INVALID_INPUT.getMessage() + "\n");
        }
    }

    private void checkInputHasValidFormat(String input) {
        if (!input.matches(PATTERN)) {
            throw new CustomException(INVALID_PURCHASE_FORMAT.getMessage() + "\n");
        }
    }

    private void checkIsValidAnswerToPromotionInfo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }

    public void createBuyOrder(String name, int price, int buyQuantity) {
        Order buyOrder = new Order(name, price, buyQuantity);
        productService.decreaseProductQuantity(name, buyQuantity);
        orderService.saveBuyOrderToRepository(buyOrder);
    }

    public void createPromotionOrder(String name, int price, int freeQuantity) {
        if (freeQuantity > 0) {
            Order promotionOrder = new Order(name, price, freeQuantity);
            orderService.savePromotionOrderToRepository(promotionOrder);
        }
    }
}
