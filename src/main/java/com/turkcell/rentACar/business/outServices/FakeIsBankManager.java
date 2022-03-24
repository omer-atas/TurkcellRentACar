package com.turkcell.rentACar.business.outServices;

// Dokunalamaz..
public class FakeIsBankManager {

    public boolean makePayment(String cardOwnerName, String cardEndMonth, int cardEndYear, int cardNumber, int cardCVC, double amount) {
        return true;
    }
}
