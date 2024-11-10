package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.CustomException;
import store.exception.ErrorMessage;
import store.model.Product;
import store.repository.ProductRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest {
    private ProductService productService;
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl();
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void getProducts() {
        Product product1 = new Product("콜라", 1000, 5, "탄산2+1");
        Product product2 = new Product("사이다", 1200, 3, "탄산2+1");
        Product product3 = new Product("비타민워터", 1500, 3, null);

        productRepository.saveProduct(product1);
        productRepository.saveProduct(product2);
        productRepository.saveProduct(product3);

        List<Product> products = productService.getProducts();

        assertThat(products).containsExactlyInAnyOrder(product1, product2, product3);
    }

    @Test
    @DisplayName("존재하지 않는 제품 조회 시 예외가 발생한다.")
    void exceptionWhenProductNotExist() {
        assertThatThrownBy(() -> productService.checkProductExist("참치캔"))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorMessage.PRODUCT_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("구매 가능한 수량이 부족할 경우 예외가 발생한다.")
    void exceptionWhenQuantityIsNotAvailable() {
        productRepository.saveProduct(new Product("콜라", 1000, 5, null));

        assertThatThrownBy(() -> productService.checkQuantityAvailableToBuy("콜라", 10))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorMessage.EXCEEDED_STOCK_QUANTITY.getMessage());
    }

    @Test
    @DisplayName("상품의 가격을 조회한다.")
    void getProductsProductPrice() {
        productRepository.saveProduct(new Product("콜라", 1000, 5, null));

        int price = productService.getProductPrice("콜라");
        assertThat(price).isEqualTo(1000);
    }

    @Test
    @DisplayName("상품의 수량을 조회한다.")
    void getProductsProductQuantity() {
        productRepository.saveProduct(new Product("콜라", 1000, 5, null));

        int quantity = productService.getProductQuantity("콜라");
        assertThat(quantity).isEqualTo(5);
    }

    @Test
    @DisplayName("상품의 프로모션 정보를 조회한다.")
    void getProductsProductPromotion() {
        productRepository.saveProduct(new Product("콜라", 10000, 5, "탄산2+1"));

        String promotion = productService.getProductPromotion("콜라");
        assertThat(promotion).isEqualTo("탄산2+1");
    }

    @Test
    @DisplayName("상품의 수량을 감소시킨다.")
    void decreaseProductQuantitySuccessfully() {
        Product product = new Product("콜라", 1000, 10, null);
        productRepository.saveProduct(product);

        productService.decreaseProductQuantity("콜라", 3);

        int updatedQuantity = productService.getProductQuantity("콜라");
        assertThat(updatedQuantity).isEqualTo(7);
    }
}
