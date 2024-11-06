package store.view;

import store.controller.ProductController;

public class OutputView {
    private final ProductController productController;

    public OutputView(ProductController productController) {
        this.productController = productController;
    }

    public void printProducts() {
        String productDisplay = productController.convertProductFormatToPrint();
        System.out.println(productDisplay);
    }
}
