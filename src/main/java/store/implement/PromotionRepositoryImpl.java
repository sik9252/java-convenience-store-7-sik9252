package store.implement;

import store.model.Promotion;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;

public class PromotionRepositoryImpl implements PromotionRepository {
    private final List<Promotion> promotions = new ArrayList<>();

    @Override
    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    @Override
    public List<Promotion> getPromotions() {
        return promotions;
    }
}
