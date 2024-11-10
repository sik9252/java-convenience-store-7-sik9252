package store.repository;

import store.model.Promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionRepositoryImpl implements PromotionRepository {
    private final List<Promotion> promotions = new ArrayList<>();

    @Override
    public void savePromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public int[] getPromotionBenefit(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .map(promotion -> new int[]{promotion.getBuy(), promotion.getGet()})
                .orElse(null);
    }

    public String getStartDate(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .map(Promotion::getStartDate)
                .orElse("");
    }

    public String getEndDate(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .map(Promotion::getEndDate)
                .orElse("");
    }
}
