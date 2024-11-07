package store.service;

import store.implement.PromotionRepositoryImpl;
import store.model.Promotion;
import store.utils.FileUtils;
import store.utils.StringUtils;

import java.util.List;

public class PromotionService {
    private final PromotionRepositoryImpl promotionRepository;

    public PromotionService(PromotionRepositoryImpl promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void save(String filename) {
        List<String> lines = FileUtils.readFile(filename);
        lines.stream().skip(1).forEach(this::create);
    }

    public List<Promotion> get() {
        return promotionRepository.getPromotions();
    }

    private void create(String line) {
        Promotion promotion = process(line);
        saveToRepository(promotion);
    }

    private Promotion process(String line) {
        String[] info = StringUtils.splitStringWithComma(line);

        String name = info[0];
        int buy = Integer.parseInt(info[1]);
        int get = Integer.parseInt(info[2]);
        String startDate = info[3];
        String endDate = info[4];

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private void saveToRepository(Promotion promotion) {
        if (promotion != null) {
            promotionRepository.addPromotion(promotion);
        }
    }
}
