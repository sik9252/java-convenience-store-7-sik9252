package store.view;

import store.controller.ProductController;
import store.controller.ReceiptController;
import store.model.Order;

import java.util.List;

public class OutputView {
    private final ProductController productController;

    public OutputView(ProductController productController) {
        this.productController = productController;
    }

    public void printWelcome() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProducts() {
        String productDisplay = productController.convertProductFormatToPrint();
        System.out.println(productDisplay);
    }

    public void printTotalOrderInfo(List<Order> buyOrders) {
        System.out.println("\n==============W 편의점================");
        System.out.printf("%-13s\t%5s\t%7s\n", "상품명", "수량", "금액");

        for (Order order : buyOrders) {
            System.out.printf("%-13s\t%5d\t%,10d\n", order.getName(), order.getQuantity(), order.getTotalPrice());
        }
    }

    public void printTotalPromotionInfo(List<Order> promotionOrders) {
        System.out.println("=============증    정===============");
        for (Order order : promotionOrders) {
            System.out.printf("%-13s\t%5s\n", order.getName(), order.getQuantity());
        }
    }

    public void printTotalPriceInfo(int totalQuantity, int totalOrderPrice, int totalPromotionPrice,
                                    int totalDiscountPrice,
                                    int totalPurchasePrice) {
        System.out.println("====================================");
        System.out.printf("%-13s\t%5s\t%,10d\n", "총구매액", totalQuantity, totalOrderPrice);
        System.out.printf("%-13s\t%18s\n", "행사할인", String.format("-%,d", totalPromotionPrice));
        System.out.printf("%-13s\t%18s\n", "멤버십할인", String.format("-%,d", totalDiscountPrice));
        System.out.printf("%-13s\t%,18d\n", "내실돈", totalPurchasePrice);
    }
}
