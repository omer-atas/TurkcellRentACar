package com.turkcell.rentACar.core.bankServices;

public interface PostService {

    boolean makePayment(String cardOwnerName, String cardNumber, int cardCVC,int cardEndMonth, int cardEndYear,double amount);
}
