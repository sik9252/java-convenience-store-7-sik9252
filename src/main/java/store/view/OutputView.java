package store.view;

import store.controller.ProductController;

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
}
