package store.service;

import store.exception.CustomException;
import store.implement.OrderRepositoryImpl;
import store.implement.ProductRepositoryImpl;
import store.implement.PromotionRepositoryImpl;
import store.model.Order;
import store.utils.StringUtils;

import java.util.List;

import static store.exception.ErrorMessage.EXCEEDED_STOCK_QUANTITY;
import static store.exception.ErrorMessage.PRODUCT_NOT_EXIST;

public class OrderService {
    private final ProductRepositoryImpl productRepository;
    private final PromotionRepositoryImpl promotionRepository;
    private final OrderRepositoryImpl orderRepository;

    public OrderService(ProductRepositoryImpl productRepository,
                        PromotionRepositoryImpl promotionRepository,
                        OrderRepositoryImpl orderRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.orderRepository = orderRepository;
    }

    public List<Order> get() {
        return orderRepository.getOrders();
    }

    public void create(String input) {
        String[] orderInfo = StringUtils.splitStringWithComma(input);

        for (String info : orderInfo) {
            String[] processedInfo = info.replace("[", "").replace("]", "").
                    split("-");
            String name = processedInfo[0];
            int quantity = Integer.parseInt(processedInfo[1]);

            checkProductExist(name);
            checkQuantityAvailableToBuy(name, quantity);

            Order order = new Order(name, quantity);
            saveToRepository(order);
        }
    }

    private void checkProductExist(String name) {
        if (!productRepository.isProductExist(name)) {
            throw new CustomException(PRODUCT_NOT_EXIST.getMessage());
        }
    }

    private void checkQuantityAvailableToBuy(String name, int quantityToBuy) {
        if (!productRepository.isQuantityAvailable(name, quantityToBuy)) {
            throw new CustomException(EXCEEDED_STOCK_QUANTITY.getMessage());
        }
    }

    private void saveToRepository(Order order) {
        if (order != null) {
            orderRepository.addOrder(order);
        }
    }
}
