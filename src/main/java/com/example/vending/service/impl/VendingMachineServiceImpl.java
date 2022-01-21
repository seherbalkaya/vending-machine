package com.example.vending.service.impl;

import com.example.vending.constant.TransactionStatus;
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
import com.example.vending.service.VendingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendingMachineServiceImpl implements VendingMachineService {

    private final ProductRepository productRepository;
    private final ProductPaymentTransactionRepository productPaymentTransactionRepository;
    private final PayStrategy payStrategy;

    @Override
    public ProductResponse selectProduct(String productCode) {
        ProductEntity productEntity = productRepository.findByCode(productCode).orElseThrow(NotFoundProductException::new);

        ProductPaymentTransactionEntity productPaymentTransactionEntity = startPaymentTransaction(productEntity);
        productPaymentTransactionRepository.save(productPaymentTransactionEntity);
        return convertProductResponse(productEntity, productPaymentTransactionEntity);
    }

    @Override
    public SelectedProductResponse selectedProductInfo(String productCode, String transactionId, Integer productCount) {
        ProductEntity productEntity = productRepository.findByCode(productCode).orElseThrow(NotFoundProductException::new);
        if (productEntity.getStock() < productCount) {
            throw new StockWarningException();
        }

        ProductPaymentTransactionEntity productPaymentTransactionEntity = productPaymentTransactionRepository.findById(transactionId).orElseThrow(NotFoundTransactionException::new);
        productPaymentTransactionEntity.setExpireDate(LocalDateTime.now().plusMinutes(3));
        productPaymentTransactionEntity.setProductCount(productCount);
        productPaymentTransactionEntity.setTotalPrice(productEntity.getPrice() * productCount);
        productPaymentTransactionRepository.save(productPaymentTransactionEntity);

        return convertSelectedProductResponse(productEntity, productPaymentTransactionEntity);
    }


    @Override
    public ProductPaymentResponse productPayment(String transactionId, ProductPaymentRequest productPaymentRequest) {
        ProductPaymentTransactionEntity productPaymentTransactionEntity = productPaymentTransactionRepository.findById(transactionId).orElseThrow(NotFoundTransactionException::new);

        if (productPaymentRequest.getPrice() < productPaymentTransactionEntity.getTotalPrice()) {
            throw new MissingPaymentException();
        }

        PayService payService = payStrategy.findStrategy(productPaymentRequest.getPaymentType());
        Double refundAmount = payService.pay(productPaymentRequest, productPaymentTransactionEntity.getTotalPrice());

        ProductEntity productEntity = productRepository.findById(productPaymentTransactionEntity.getProductId()).orElseThrow(NotFoundProductException::new);
        productEntity.setStock(productEntity.getStock() - productPaymentTransactionEntity.getProductCount());
        productRepository.save(productEntity);

        productPaymentTransactionEntity.setStatus(TransactionStatus.COMPLETED.getId());
        productPaymentTransactionEntity.setRefundPrice(refundAmount);
        productPaymentTransactionEntity.setPaymentType(productPaymentRequest.getPaymentType());
        productPaymentTransactionRepository.save(productPaymentTransactionEntity);

        return convertProductPaymentResponse(productPaymentTransactionEntity, productEntity);
    }

    private ProductPaymentResponse convertProductPaymentResponse(ProductPaymentTransactionEntity productPaymentTransactionEntity, ProductEntity productEntity) {
        ProductPaymentResponse productPaymentResponse = new ProductPaymentResponse();
        productPaymentResponse.setPaymentType(productPaymentTransactionEntity.getPaymentType());
        productPaymentResponse.setRefundAmount(productPaymentTransactionEntity.getRefundPrice());
        productPaymentResponse.setTotalPrice(productPaymentTransactionEntity.getTotalPrice());
        productPaymentResponse.setProductCount(productPaymentTransactionEntity.getProductCount());
        productPaymentResponse.setProductName(productEntity.getName());
        return productPaymentResponse;
    }

    private ProductResponse convertProductResponse(ProductEntity productEntity, ProductPaymentTransactionEntity productPaymentTransactionEntity) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductCode(productEntity.getCode());
        productResponse.setProductName(productEntity.getName());
        productResponse.setProductPrice(productEntity.getPrice());
        productResponse.setProductType(productEntity.getType());
        productResponse.setTransactionExpireDate(productPaymentTransactionEntity.getExpireDate());
        productResponse.setTransactionId(productPaymentTransactionEntity.getId());
        productResponse.setProductCount(productEntity.getStock());
        return productResponse;
    }

    private ProductPaymentTransactionEntity startPaymentTransaction(ProductEntity productEntity) {
        ProductPaymentTransactionEntity productPaymentTransactionEntity = new ProductPaymentTransactionEntity();
        productPaymentTransactionEntity.setProductCode(productEntity.getCode());
        productPaymentTransactionEntity.setProductId(productEntity.getId());
        productPaymentTransactionEntity.setId(UUID.randomUUID().toString());
        productPaymentTransactionEntity.setExpireDate(LocalDateTime.now().plusMinutes(3));
        productPaymentTransactionEntity.setStatus(TransactionStatus.STARTED.getId());
        return productPaymentTransactionEntity;
    }

    private SelectedProductResponse convertSelectedProductResponse(ProductEntity productEntity, ProductPaymentTransactionEntity productPaymentTransactionEntity) {
        SelectedProductResponse selectedProductResponse = new SelectedProductResponse();
        selectedProductResponse.setProductCode(productEntity.getCode());
        selectedProductResponse.setTotalPrice(productPaymentTransactionEntity.getTotalPrice());
        selectedProductResponse.setSelectedCount(productPaymentTransactionEntity.getProductCount());
        selectedProductResponse.setTransactionExpireDate(productPaymentTransactionEntity.getExpireDate());
        selectedProductResponse.setTransactionId(productPaymentTransactionEntity.getId());
        return selectedProductResponse;
    }
}
