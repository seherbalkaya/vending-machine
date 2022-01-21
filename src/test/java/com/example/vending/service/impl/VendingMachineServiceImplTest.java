package com.example.vending.service.impl;

import com.example.vending.constant.PaymentTypeDefinition;
import com.example.vending.entity.ProductEntity;
import com.example.vending.entity.ProductPaymentTransactionEntity;
import com.example.vending.exception.MissingPaymentException;
import com.example.vending.exception.NotFoundProductException;
import com.example.vending.exception.NotFoundTransactionException;
import com.example.vending.exception.StockWarningException;
import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.model.ProductPaymentResponse;
import com.example.vending.model.ProductResponse;
import com.example.vending.model.SelectedProductResponse;
import com.example.vending.repository.ProductPaymentTransactionRepository;
import com.example.vending.repository.ProductRepository;
import com.example.vending.service.PayService;
import com.example.vending.service.PayStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class VendingMachineServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductPaymentTransactionRepository productPaymentTransactionRepository;
    @Mock
    private PayStrategy payStrategy;
    @Mock
    private PayService payService;

    @InjectMocks
    private VendingMachineServiceImpl vendingMachineService;

    @Test
    void should_select_product_then_successfully() {
        //given
        String productCode = "a1";

        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode("a1");
        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setPrice(5d);
        productEntity.setName("gofret");
        productEntity.setType("food");
        productEntity.setStock(3);

        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.of(productEntity));
        Mockito.when(productPaymentTransactionRepository.save(Mockito.any(ProductPaymentTransactionEntity.class))).thenReturn(new ProductPaymentTransactionEntity());

        //when
        ProductResponse response = vendingMachineService.selectProduct(productCode);

        //then
        assertEquals("gofret", response.getProductName());
        assertEquals(3, response.getProductCount());
        assertEquals(5d, response.getProductPrice());
        assertEquals("food", response.getProductType());

        Mockito.verify(productRepository, Mockito.times(1)).findByCode(productCode);
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).save(Mockito.any(ProductPaymentTransactionEntity.class));
    }

    @Test
    void should_select_product_then_falling_throw_product_notfound_exception() {
        //given
        String productCode = "a34";
        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.empty());
        //when

        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.selectProduct(productCode));

        //then
        assertEquals(NotFoundProductException.class, e.getClass());
        Mockito.verify(productRepository, Mockito.times(1)).findByCode(productCode);
    }

    @Test
    void should_get_selected_product_info_successfully() {
        //given
        String productCode = "a1";
        String transactionId = UUID.randomUUID().toString();
        Integer productCount = 1;

        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode("a1");
        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setPrice(5d);
        productEntity.setName("gofret");
        productEntity.setType("food");
        productEntity.setStock(3);

        ProductPaymentTransactionEntity productPaymentTransactionEntity = new ProductPaymentTransactionEntity();
        productPaymentTransactionEntity.setId(transactionId);

        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.of(productEntity));
        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.of(productPaymentTransactionEntity));
        Mockito.when(productPaymentTransactionRepository.save(productPaymentTransactionEntity)).thenReturn(new ProductPaymentTransactionEntity());
        //when
        SelectedProductResponse response = vendingMachineService.selectedProductInfo(productCode, transactionId, productCount);
        //then
        assertEquals("a1", response.getProductCode());
        assertEquals(5d, response.getTotalPrice());
        assertEquals(1, response.getSelectedCount());
        assertEquals(transactionId, response.getTransactionId());
    }

    @Test
    void should_selected_product_info_falling_product_notfound_exception() {
        //given
        String productCode = "a1";
        String transactionId = UUID.randomUUID().toString();
        Integer productCount = 1;
        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.empty());
        //when

        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.selectedProductInfo(productCode, transactionId, productCount));

        //then
        assertEquals(NotFoundProductException.class, e.getClass());
        Mockito.verify(productRepository, Mockito.times(1)).findByCode(productCode);
    }

    @Test
    void should_selected_product_info_falling_product_stock_warning_exception() {
        //given
        String productCode = "a1";
        String transactionId = UUID.randomUUID().toString();
        Integer productCount = 5;

        ProductEntity productEntity = new ProductEntity();
        productEntity.setStock(3);

        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.of(productEntity));
        //when

        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.selectedProductInfo(productCode, transactionId, productCount));

        //then
        assertEquals(StockWarningException.class, e.getClass());
        Mockito.verify(productRepository, Mockito.times(1)).findByCode(productCode);
    }

    @Test
    void should_selected_product_info_falling_transaction_notfound_exception() {
        //given
        String productCode = "a1";
        String transactionId = UUID.randomUUID().toString();
        Integer productCount = 1;

        ProductEntity productEntity = new ProductEntity();
        productEntity.setStock(3);

        Mockito.when(productRepository.findByCode(productCode)).thenReturn(Optional.of(productEntity));
        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        //when

        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.selectedProductInfo(productCode, transactionId, productCount));

        //then
        assertEquals(NotFoundTransactionException.class, e.getClass());
        Mockito.verify(productRepository, Mockito.times(1)).findByCode(productCode);
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).findById(transactionId);
    }

    @Test
    void should_product_payment_successfully() {
        //given
        String transactionId = UUID.randomUUID().toString();
        ProductPaymentRequest request = new ProductPaymentRequest();
        request.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());
        request.setPrice(2d);

        ProductPaymentTransactionEntity transactionEntity = new ProductPaymentTransactionEntity();
        transactionEntity.setId(transactionId);
        transactionEntity.setProductCount(1);
        transactionEntity.setTotalPrice(2d);
        transactionEntity.setProductId("1");
        transactionEntity.setProductCode("a1");
        transactionEntity.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());
        transactionEntity.setRefundPrice(0d);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setStock(5);
        productEntity.setPrice(2d);
        productEntity.setName("gofret");

        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));
        Mockito.when(payStrategy.findStrategy(PaymentTypeDefinition.BANKNOTE.name())).thenReturn(payService);
        Mockito.when(payService.pay(request, 5d)).thenReturn(0d);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(productEntity));
        Mockito.when(productRepository.save(productEntity)).thenReturn(productEntity);
        Mockito.when(productPaymentTransactionRepository.save(transactionEntity)).thenReturn(transactionEntity);

        //when
        ProductPaymentResponse response = vendingMachineService.productPayment(transactionId, request);

        //then
        assertEquals(PaymentTypeDefinition.BANKNOTE.name(), response.getPaymentType());
        assertEquals(0d, response.getRefundAmount());
        assertEquals(2d, response.getTotalPrice());
        assertEquals(1, response.getProductCount());
        assertEquals("gofret", response.getProductName());

        Mockito.verify(payStrategy, Mockito.times(1)).findStrategy(PaymentTypeDefinition.BANKNOTE.name());
        Mockito.verify(productRepository, Mockito.times(1)).findById("1");
        Mockito.verify(payService, Mockito.times(1)).pay(request, 2d);
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).findById(transactionId);
        Mockito.verify(productRepository, Mockito.times(1)).save(productEntity);
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).save(transactionEntity);
    }

    @Test
    void should_throw_transaction_not_found_exception_when_product_payment_request() {

        //given
        ProductPaymentRequest request = new ProductPaymentRequest();
        String transactionId = UUID.randomUUID().toString();

        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        //when
        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.productPayment(transactionId, request));

        //then
        assertEquals(NotFoundTransactionException.class, e.getClass());
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).findById(transactionId);
    }

    @Test
    void should_throw_missing_payment_exception_when_product_payment_request() {

        //given
        ProductPaymentRequest request = new ProductPaymentRequest();
        request.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());
        request.setPrice(2d);

        String transactionId = UUID.randomUUID().toString();

        ProductPaymentTransactionEntity transactionEntity = new ProductPaymentTransactionEntity();
        transactionEntity.setId(transactionId);
        transactionEntity.setTotalPrice(5d);

        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));
        //when
        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.productPayment(transactionId, request));

        //then
        assertEquals(MissingPaymentException.class, e.getClass());
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).findById(transactionId);
    }

    @Test
    void should_throw_notfound_product_exception_when_product_payment_request() {
        //given
        ProductPaymentRequest request = new ProductPaymentRequest();
        request.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());
        request.setPrice(5d);

        String transactionId = UUID.randomUUID().toString();

        ProductPaymentTransactionEntity transactionEntity = new ProductPaymentTransactionEntity();
        transactionEntity.setId(transactionId);
        transactionEntity.setTotalPrice(5d);
        transactionEntity.setProductId("2");

        Mockito.when(productPaymentTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));
        Mockito.when(payStrategy.findStrategy(PaymentTypeDefinition.BANKNOTE.name())).thenReturn(payService);
        Mockito.when(payService.pay(request, 5d)).thenReturn(0d);
        Mockito.when(productRepository.findById(transactionEntity.getProductId())).thenReturn(Optional.empty());

        //when
        RuntimeException e = assertThrows(RuntimeException.class, () -> vendingMachineService.productPayment(transactionId, request));

        //then
        assertEquals(NotFoundProductException.class, e.getClass());
        Mockito.verify(productPaymentTransactionRepository, Mockito.times(1)).findById(transactionId);
        Mockito.verify(payStrategy, Mockito.times(1)).findStrategy(PaymentTypeDefinition.BANKNOTE.name());
        Mockito.verify(payService, Mockito.times(1)).pay(request, 5d);
        Mockito.verify(productRepository, Mockito.times(1)).findById("2");
    }
}