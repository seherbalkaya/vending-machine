package com.example.vending.service.impl;

import com.example.vending.entity.SafeEntity;
import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.repository.SafeRepository;
import com.example.vending.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanknotePayService implements PayService {

    private final String id = "1";
    private final SafeRepository safeRepository;

    @Override
    public Double pay(ProductPaymentRequest productPaymentRequest, Double totalPrice) {
        SafeEntity safeEntity = safeRepository.getById(id);
        Double refundAmount = 0d;
        if (productPaymentRequest.getPrice() > totalPrice) {
            refundAmount = productPaymentRequest.getPrice() - totalPrice;
        }
        safeEntity.setTotalMoney(safeEntity.getTotalMoney() + productPaymentRequest.getPrice());
        safeRepository.save(safeEntity);
        return refundAmount;
    }
}
