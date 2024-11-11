package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.CustomException;
import store.exception.ErrorMessage;
import store.model.Order;
import store.repository.OrderRepositoryImpl;
import store.repository.ProductRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        ProductService productService = new ProductService(productRepository);
        orderService = new OrderService(orderRepository, productService);
    }

    @Test
    @DisplayName("전체 주문 목록을 반환한다.")
    void getTotalOrder() {
        orderService.saveTotalOrder(new Order("콜라", 1000, 3));
        orderService.saveTotalOrder(new Order("사이다", 2000, 3));

        List<Order> orders = orderService.getTotalOrder();
        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("프로모션으로 무료 제공된 주문 목록을 반환한다.")
    void getFreeOrderByPromotion() {
        orderService.saveFreeOrderByPromotion(new Order("콜라", 1000, 1));
        orderService.saveFreeOrderByPromotion(new Order("사이다", 2000, 1));

        List<Order> promoOrders = orderService.getFreeOrderByPromotion();
        assertThat(promoOrders).hasSize(2);
    }

    @Test
    @DisplayName("저장소에 들어온 주문을 저장한다.")
    void saveTotalOrder() {
        Order order = new Order("콜라", 1000, 1);
        orderService.saveTotalOrder(order);

        List<Order> orders = orderService.getTotalOrder();
        assertThat(orders).contains(order);
    }

    @Test
    @DisplayName("저장소에 들어온 프로모션(무료 제공된 상품) 주문을 저장한다.")
    void saveFreeOrderByPromotion() {
        Order promotionOrder = new Order("콜라", 1000, 1);
        orderService.saveFreeOrderByPromotion(promotionOrder);

        List<Order> promotionOrders = orderService.getFreeOrderByPromotion();
        assertThat(promotionOrders).contains(promotionOrder);
    }

    @Test
    @DisplayName("안내 메시지에 유효한 답변이 아닌 경우 예외를 던진다.")
    void invalidAnswerThrowsException() {
        assertThatThrownBy(() -> orderService.checkIsValidAnswerToPromotionInfo("X"))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    @DisplayName("상품을 덜 가져온 경우 메시지에 Y를 입력한 경우 총 주문 및 프로모션(무료) 주문을 생성한다.")
    void createOrderWhenAnswerIsYes() {
        orderService.createOrderWhenAnswerIsYes("Y", "콜라", 1000, 3, 1);

        List<Order> orders = orderService.getTotalOrder();
        List<Order> freeOrders = orderService.getFreeOrderByPromotion();

        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().getName()).isEqualTo("콜라");
        assertThat(orders.getFirst().getQuantity()).isEqualTo(3);

        assertThat(freeOrders).hasSize(1);
        assertThat(freeOrders.getFirst().getName()).isEqualTo("콜라");
        assertThat(freeOrders.getFirst().getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품을 덜 가져온 경우 안내 메시지에 N을 입력한 경우 총 주문만 생성하고 조건에 맞을 경우 무료 주문을 생성한다.")
    void createOrderWhenAnswerIsNo() {
        orderService.createOrderWhenAnswerIsNo("N", "콜라", 2000, 2, 1, 2);

        List<Order> orders = orderService.getTotalOrder();
        List<Order> freeOrders = orderService.getFreeOrderByPromotion();

        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().getName()).isEqualTo("콜라");
        assertThat(orders.getFirst().getQuantity()).isEqualTo(2);

        assertThat(freeOrders).hasSize(0);
    }
}
