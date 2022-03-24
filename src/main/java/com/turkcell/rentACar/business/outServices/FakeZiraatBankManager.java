package com.turkcell.rentACar.business.outServices;

// Bu koda dokunulamaz...
public class FakeZiraatBankManager {

    public boolean makePayment( String cardOwnerName,String cardNumber, int cardEndMonth, int cardEndYear,int cardCVC,double amount) {
        return true;
    }
}
