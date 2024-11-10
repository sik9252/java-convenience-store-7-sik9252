package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.MemberShipDiscount;
import store.model.Order;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberShipDiscountServiceTest {
    private MemberShipDiscountService discountService;
    private MemberShipDiscount memberShipDiscount;

    @BeforeEach
    void setUp() {
        memberShipDiscount = new MemberShipDiscount();
        discountService = new MemberShipDiscountService(memberShipDiscount);
    }

    @Test
    @DisplayName("멤버십 회원은 (프로모션 미적용) 금액의 30%를 할인한다.")
    void calculateDiscountPrice() {
        Order order1 = new Order("콜라", 15000, 1);
        Order order2 = new Order("사이다", 10000, 1);
        List<Order> orders = Arrays.asList(order1, order2);

        discountService.calculateDiscountPrice(orders);

        assertThat(memberShipDiscount.getMemberShipDiscountPrice()).isEqualTo(7500);
    }

    @Test
    @DisplayName("할인가는 8000을 초과하지 않는다.")
    void maxDiscountIs8000() {
        Order order1 = new Order("콜라", 20000, 1);
        Order order2 = new Order("사이다", 20000, 1);
        Order order3 = new Order("초코바", 15000, 1);
        List<Order> orders = Arrays.asList(order1, order2, order3);

        discountService.calculateDiscountPrice(orders);

        assertThat(memberShipDiscount.getMemberShipDiscountPrice()).isEqualTo(8000);
    }
}
