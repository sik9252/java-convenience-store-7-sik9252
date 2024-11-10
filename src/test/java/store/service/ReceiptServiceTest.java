package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.MemberShipDiscount;
import store.model.Order;
import store.model.Receipt;
import store.repository.OrderRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptServiceTest {
    private ReceiptService receiptService;
    private OrderRepositoryImpl orderRepository;
    private MemberShipDiscount memberShipDiscount;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepositoryImpl();
        memberShipDiscount = new MemberShipDiscount();
        receipt = new Receipt();
        receiptService = new ReceiptService(orderRepository, memberShipDiscount, receipt);
    }

    @Test
    @DisplayName("총 주문 가격을 계산한다.")
    void calculateTotalOrderPrice() {
        orderRepository.saveTotalOrder(new Order("콜라", 1000, 1));
        orderRepository.saveTotalOrder(new Order("사이다", 2000, 3));

        receiptService.calculate();

        assertThat(receipt.getTotalOrderPrice()).isEqualTo(7000);
    }

    @Test
    @DisplayName("프로모션으로 받은 총 혜택(할인) 가격을 계산한다.")
    void calculateTotalPromotionPrice() {
        orderRepository.saveFreeOrderByPromotion(new Order("콜라", 1000, 2));
        orderRepository.saveFreeOrderByPromotion(new Order("사이다", 2000, 2));

        receiptService.calculate();

        assertThat(receipt.getTotalPromotionPrice()).isEqualTo(6000);
    }

    @Test
    @DisplayName("멤버십에 의한 총 할인 가격을 계산한다.")
    void calculateTotalDiscountPrice() {
        memberShipDiscount.setMemberShipDiscountPrice(1500);

        receiptService.calculate();

        assertThat(receipt.getTotalDiscountPrice()).isEqualTo(1500);
    }

    @Test
    @DisplayName("총 주문, 프로모션, 할인, 구매 가격을 올바르게 계산한다.")
    void calculateAllPricesCorrectly() {
        orderRepository.saveTotalOrder(new Order("콜라", 1000, 2));
        orderRepository.saveTotalOrder(new Order("사이다", 2000, 1));
        orderRepository.saveFreeOrderByPromotion(new Order("초코바", 1200, 1));
        memberShipDiscount.setMemberShipDiscountPrice((int) (4000 * 0.3));

        receiptService.calculate();

        assertThat(receipt.getTotalOrderPrice()).isEqualTo(4000);
        assertThat(receipt.getTotalPromotionPrice()).isEqualTo(1200);
        assertThat(receipt.getTotalDiscountPrice()).isEqualTo(1200);
        assertThat(receipt.getTotalPurchasePrice()).isEqualTo(1600);
    }
}
