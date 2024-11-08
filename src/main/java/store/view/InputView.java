package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String getOrders() {
        System.out.println("\n" + "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public String getAnswerToPromotionInfo(String productName, int freeQuantity) {
        System.out.println("\n" + "현재 " + productName + "은(는) " + freeQuantity + "개를 무료로 더 받을 수 있습니다. " +
                "추가하시겠습니까? (Y/N)");
        return Console.readLine();
    }
}
