package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Product;
import store.model.Promotion;
import store.repository.ProductRepositoryImpl;
import store.repository.PromotionRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionServiceTest {
    private PromotionRepositoryImpl promotionRepository;
    private ProductRepositoryImpl productRepository;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(productRepository);
        promotionService = new PromotionService(promotionRepository, productService);
    }

    @Test
    @DisplayName("프로모션의 혜택을 조회한다.")
    void getBenefitOfPromotion() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, "2024-01-01", "2024-12-31");
        promotionRepository.savePromotion(promotion);

        int[] benefits = promotionService.getBenefitOfPromotion("탄산2+1");
        assertThat(benefits).containsExactly(2, 1);
    }

    @Test
    @DisplayName("프로모션의 시작 날짜를 조회한다.")
    void getStartDateOfPromotion() {
        Promotion promotion = new Promotion("반짝할인", 2, 1, "2024-11-01", "2024-11-30");
        promotionRepository.savePromotion(promotion);

        String startDate = promotionService.getStartDateOfPromotion("반짝할인");
        assertThat(startDate).isEqualTo("2024-11-01");
    }

    @Test
    @DisplayName("프로모션의 종료 날짜를 조회한다.")
    void getEndDateOfPromotion() {
        Promotion promotion = new Promotion("반짝할인", 2, 1, "2024-11-01", "2024-11-30");
        promotionRepository.savePromotion(promotion);

        String endDate = promotionService.getEndDateOfPromotion("반짝할인");
        assertThat(endDate).isEqualTo("2024-11-30");
    }

    @Test
    @DisplayName("제품에 적용 가능한 프로모션 정보를 조회한다.")
    void getPromotionInfoForProduct() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, "2024-01-01", "2024-12-31");
        promotionRepository.savePromotion(promotion);

        Product cola = new Product("콜라", 1000, 5, "탄산2+1");
        productRepository.saveProduct(cola);

        int[] benefits = promotionService.getPromotionInfo("콜라");
        assertThat(benefits).containsExactly(2, 1);
    }
}
