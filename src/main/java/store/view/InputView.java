package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String getOrders() {
        System.out.println("\n" + "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public String getAnswerToPromotionInfo(String productName) {
        System.out.println("\n" + "현재 " + productName + "은(는) " + "1개를 무료로 더 받을 수 있습니다. " +
                "추가하시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public String getAnswerToPromotionIsOutOfStock(String productName, int quantity) {
        System.out.println("\n" + "현재 " + productName + " " + quantity + "개는 " + "프로모션 할인이 적용되지 않습니다. " +
                "그래도 구매하시겠습니까? (Y/N)");
        return Console.readLine();
    }
}
