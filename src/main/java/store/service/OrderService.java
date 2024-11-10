package store.service;

import store.exception.CustomException;
import store.repository.OrderRepositoryImpl;
import store.model.Order;

import java.util.List;

import static store.exception.ErrorMessage.INVALID_INPUT;

public class OrderService {
    private final OrderRepositoryImpl orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepositoryImpl orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public List<Order> getBuyOrders() {
        return orderRepository.getTotalOrder();
    }

    public List<Order> getPromotionOrders() {
        return orderRepository.getFreeOrderByPromotion();
    }

    public void saveBuyOrderToRepository(Order order) {
        if (order != null) {
            orderRepository.saveTotalOrder(order);
        }
    }

    public void savePromotionOrderToRepository(Order order) {
        if (order != null) {
            orderRepository.saveFreeOrderByPromotion(order);
        }
    }

    public List<Order> getNotPromotionProduct() {
        return orderRepository.getNotPromotionProduct();
    }

    public void createBuyOrder(String name, int price, int buyQuantity) {
        Order buyOrder = new Order(name, price, buyQuantity);
        productService.decreaseProductQuantity(name, buyQuantity);
        saveBuyOrderToRepository(buyOrder);
    }

    public void createPromotionOrder(String name, int price, int freeQuantity) {
        if (freeQuantity > 0) {
            Order promotionOrder = new Order(name, price, freeQuantity);
            savePromotionOrderToRepository(promotionOrder);
        }
    }

    public void createOrderWhenAnswerIsYes(String input, String productName, int price, int requestQuantity,
                                            int freeQuantity) {
        if (input.equals("Y")) {
            createBuyOrder(productName, price, requestQuantity);
            createPromotionOrder(productName, price, freeQuantity);
        }
    }

    public void createOrderWhenAnswerIsNo(String input, String productName, int price, int requestQuantity,
                                           int freeQuantity, int buy) {
        if (input.equals("N")) {
            createBuyOrder(productName, price, requestQuantity);
            if (requestQuantity != buy) {
                createPromotionOrder(productName, price, freeQuantity);
            }
        }
    }

    public void checkIsValidAnswerToPromotionInfo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }
}
