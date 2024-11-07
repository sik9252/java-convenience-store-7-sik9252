package store.factory;

import store.implement.PromotionRepositoryImpl;
import store.service.PromotionService;

public class PromotionFactory {
    public static PromotionService createPromotionService() {
        PromotionRepositoryImpl promotionRepository = new PromotionRepositoryImpl();

        return new PromotionService(promotionRepository);
    }
}
