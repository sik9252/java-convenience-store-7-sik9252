package store.controller;

import store.service.PromotionService;

public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public void savePromotions() {
        promotionService.save("promotions.md");
    }
}
