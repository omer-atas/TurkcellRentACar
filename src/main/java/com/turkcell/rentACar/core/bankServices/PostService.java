package com.turkcell.rentACar.core.bankServices;

public interface PostService {

    boolean addPayment(String cardOwnerName, String cardNumber, int cardCVC,int cardEndMonth, int cardEndYear,double amount);
}
