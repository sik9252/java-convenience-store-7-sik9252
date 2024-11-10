package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.implement.PromotionRepositoryImpl;
import store.model.Promotion;
import store.utils.FileUtils;
import store.utils.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PromotionService {
    private final PromotionRepositoryImpl promotionRepository;
    private final ProductService productService;

    public PromotionService(PromotionRepositoryImpl promotionRepository, ProductService productService) {
        this.promotionRepository = promotionRepository;
        this.productService = productService;
    }

    public void save(String filename) {
        List<String> lines = FileUtils.readFile(filename);
        lines.stream().skip(1).forEach(this::create);
    }

    public int[] getBenefitOfPromotion(String promotionName) {
        return promotionRepository.getPromotionBenefit(promotionName);
    }

    public String getStartDateOfPromotion(String promotionName) {
        return promotionRepository.getStartDate(promotionName);
    }

    public String getEndDateOfPromotion(String promotionName) {
        return promotionRepository.getEndDate(promotionName);
    }

    public int[] getPromotionInfo(String productName) {
        String promotionName = productService.getProductPromotion(productName);
        return checkPromotionIsAvailable(promotionName);
    }

    private int[] checkPromotionIsAvailable(String promotionName) {
        if (promotionName != null) {
            if (comparePromotionDurationAndToday(promotionName)) {
                return getBenefitOfPromotion(promotionName);
            }
        }

        return null;
    }

    private boolean comparePromotionDurationAndToday(String promotionName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(getStartDateOfPromotion(promotionName), formatter).atStartOfDay();
        LocalDateTime endDate =
                LocalDate.parse(getEndDateOfPromotion(promotionName), formatter).atTime(LocalTime.MAX);
        LocalDateTime today = DateTimes.now();

        return !today.isBefore(startDate) && !today.isAfter(endDate);
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
