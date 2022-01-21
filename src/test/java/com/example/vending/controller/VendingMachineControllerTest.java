package com.example.vending.controller;

import com.example.vending.constant.PaymentTypeDefinition;
import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.model.ProductPaymentResponse;
import com.example.vending.model.ProductResponse;
import com.example.vending.model.SelectedProductResponse;
import com.example.vending.service.impl.VendingMachineServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class VendingMachineControllerTest {

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @Mock
    private VendingMachineServiceImpl vendingMachineService;

    @InjectMocks
    private VendingMachineController vendingMachineController;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(vendingMachineController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void should_successfully_when_select_product_request() throws Exception {
        //given
        String productCode = "a2";
        String transactionId = UUID.randomUUID().toString();

        ProductResponse response = new ProductResponse();
        response.setProductCode(productCode);
        response.setProductName("gofret");
        response.setTransactionId(transactionId);

        Mockito.when(vendingMachineService.selectProduct(productCode)).thenReturn(response);
        //when
        String result =
                mvc.perform(MockMvcRequestBuilders.post("/product-type/a2"))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                        .andReturn().getResponse().getContentAsString();

        ProductResponse responseObject = objectMapper.readValue(result, ProductResponse.class);
        //then
        Assertions.assertEquals("gofret", responseObject.getProductName());
        Assertions.assertEquals("a2", responseObject.getProductCode());
        Assertions.assertEquals(transactionId, responseObject.getTransactionId());

        Mockito.verify(vendingMachineService, Mockito.times(1)).selectProduct(productCode);
    }


    @Test
    void should_successfully_when_selected_product_info_request() throws Exception {
        //given
        String productCode = "a2";
        String transactionId = UUID.randomUUID().toString();
        Integer productCount = 1;

        SelectedProductResponse response = new SelectedProductResponse();
        response.setProductCode(productCode);
        response.setTotalPrice(5d);
        response.setSelectedCount(productCount);
        response.setTransactionId(transactionId);

        Mockito.when(vendingMachineService.selectedProductInfo(productCode, transactionId, productCount)).thenReturn(response);
        //when
        String result =
                mvc.perform(MockMvcRequestBuilders.patch("/select-product/a2")
                                .param("transactionId", transactionId)
                                .param("count", productCount.toString()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                        .andReturn().getResponse().getContentAsString();

        SelectedProductResponse responseObject = objectMapper.readValue(result, SelectedProductResponse.class);
        //then
        Assertions.assertEquals(5d, responseObject.getTotalPrice());
        Assertions.assertEquals(1, responseObject.getSelectedCount());
        Assertions.assertEquals("a2", responseObject.getProductCode());
        Assertions.assertEquals(transactionId, responseObject.getTransactionId());

        Mockito.verify(vendingMachineService, Mockito.times(1)).selectedProductInfo(productCode, transactionId, productCount);
    }

    @Test
    void should_successfully_when_product_payment_request() throws Exception {
        //given
        String transactionId = UUID.randomUUID().toString();
        ProductPaymentRequest request = new ProductPaymentRequest();
        request.setPrice(5d);
        request.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());

        ProductPaymentResponse response = new ProductPaymentResponse();
        response.setTotalPrice(5d);
        response.setProductCount(1);
        response.setProductName("gofret");
        response.setPaymentType(PaymentTypeDefinition.BANKNOTE.name());
        response.setRefundAmount(0d);

        Mockito.when(vendingMachineService.productPayment(Mockito.anyString(), Mockito.any(ProductPaymentRequest.class))).thenReturn(response);
        //when
        String result =
                mvc.perform(MockMvcRequestBuilders.patch("/product-payment/" + transactionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                        .andReturn().getResponse().getContentAsString();

        ProductPaymentResponse responseObject = objectMapper.readValue(result, ProductPaymentResponse.class);
        //then
        Assertions.assertEquals(5d, responseObject.getTotalPrice());
        Assertions.assertEquals(1, responseObject.getProductCount());
        Assertions.assertEquals("gofret", responseObject.getProductName());
        Assertions.assertEquals(PaymentTypeDefinition.BANKNOTE.name(), responseObject.getPaymentType());
        Assertions.assertEquals(0d, responseObject.getRefundAmount());

        Mockito.verify(vendingMachineService, Mockito.times(1)).productPayment(Mockito.anyString(), Mockito.any(ProductPaymentRequest.class));
    }
}