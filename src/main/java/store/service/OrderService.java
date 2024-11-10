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

    public List<Order> getTotalOrder() {
        return orderRepository.getTotalOrder();
    }

    public List<Order> getFreeOrderByPromotion() {
        return orderRepository.getFreeOrderByPromotion();
    }

    public void saveTotalOrder(Order order) {
        if (order != null) {
            orderRepository.saveTotalOrder(order);
        }
    }

    public void saveFreeOrderByPromotion(Order order) {
        if (order != null) {
            orderRepository.saveFreeOrderByPromotion(order);
        }
    }

    public List<Order> getNotPromotionProduct() {
        return orderRepository.getNotPromotionProduct();
    }

    public void createTotalOrder(String name, int price, int buyQuantity) {
        Order buyOrder = new Order(name, price, buyQuantity);
        productService.decreaseProductQuantity(name, buyQuantity);
        saveTotalOrder(buyOrder);
    }

    public void createFreeOrderByPromotion(String name, int price, int freeQuantity) {
        if (freeQuantity > 0) {
            Order promotionOrder = new Order(name, price, freeQuantity);
            saveFreeOrderByPromotion(promotionOrder);
        }
    }

    public void createOrderWhenAnswerIsYes(String input, String productName, int price, int requestQuantity,
                                            int freeQuantity) {
        if (input.equals("Y")) {
            createTotalOrder(productName, price, requestQuantity);
            createFreeOrderByPromotion(productName, price, freeQuantity);
        }
    }

    public void createOrderWhenAnswerIsNo(String input, String productName, int price, int requestQuantity,
                                           int freeQuantity, int buy) {
        if (input.equals("N")) {
            createTotalOrder(productName, price, requestQuantity);
            if (requestQuantity != buy) {
                createFreeOrderByPromotion(productName, price, freeQuantity);
            }
        }
    }

    public void checkIsValidAnswerToPromotionInfo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new CustomException(INVALID_INPUT.getMessage());
        }
    }
}
