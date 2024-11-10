package store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.MemberShipDiscount;
import store.model.Order;
import store.model.Receipt;
import store.repository.OrderRepositoryImpl;
import store.repository.ProductRepositoryImpl;
import store.service.OrderService;
import store.service.ProductService;
import store.service.ReceiptService;
import store.view.OutputView;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReceiptControllerTest {
    private OrderService orderService;
    private ReceiptController receiptController;


    @BeforeEach
    void setUp() {
        Receipt receipt = new Receipt();
        MemberShipDiscount memberShipDiscount = new MemberShipDiscount();

        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();

        ProductService productService = new ProductService(productRepository);
        ProductController productController = new ProductController(productService);

        orderService = new OrderService(orderRepository, productService);
        ReceiptService receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);

        OutputView outputView = new OutputView(productController);
        receiptController = new ReceiptController(orderService, receiptService, outputView);
    }

    @Test
    @DisplayName("영수증을 생성하여 출력한다.")
    void makeReceipt() {
        Order order1 = new Order("콜라", 1000, 3);
        Order freeOrder1 = new Order("콜라", 1000, 1);
        Order order2 = new Order("초코칩", 1200, 2);
        Order freeOrder2 = new Order("초코칩", 1200, 1);
        Order order3 = new Order("비타민워터", 2000, 1);

        orderService.saveTotalOrder(order1);
        orderService.saveFreeOrderByPromotion(freeOrder1);
        orderService.saveTotalOrder(order2);
        orderService.saveFreeOrderByPromotion(freeOrder2);
        orderService.saveTotalOrder(order3);

        assertThatCode(() -> receiptController.makeReceipt()).doesNotThrowAnyException();
    }
}
