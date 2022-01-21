package com.example.vending.controller;

import com.example.vending.exception.MissingPaymentException;
import com.example.vending.exception.NotFoundPayStrategyException;
import com.example.vending.exception.NotFoundProductException;
import com.example.vending.exception.NotFoundTransactionException;
import com.example.vending.exception.StockWarningException;
import com.example.vending.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handleException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("sistemsel bir hata oluştu").build();

        return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingPaymentException.class)
    public ResponseEntity<ErrorModel> handleMissingPaymentException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("yüklenen miktar yeterli değil").build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundPayStrategyException.class)
    public ResponseEntity<ErrorModel> handleNotFoundPayStrategyException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("sistemde tanımlı olmayan bir ödeme türü belirlendi").build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<ErrorModel> handleNotFoundProductException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("girmiş olduğunuz ürün koduna tanımlı bir ürün bulunamadı").build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundTransactionException.class)
    public ResponseEntity<ErrorModel> handleNotFoundTransactionException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("lütfen tekrar deneyin").build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockWarningException.class)
    public ResponseEntity<ErrorModel> handleStockWarningException(HttpServletRequest request, Exception exception) {
        ErrorModel errorModel = ErrorModel.builder().message("yeterli ürün bulunmamaktadır").build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }
}
