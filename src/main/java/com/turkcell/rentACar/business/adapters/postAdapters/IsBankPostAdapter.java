package com.turkcell.rentACar.business.adapters.postAdapters;

import com.turkcell.rentACar.business.outServices.FakeIsBankManager;
import com.turkcell.rentACar.core.bankServices.PostService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class IsBankPostAdapter implements PostService {

    @Override
    public boolean makePayment(String cardOwnerName, String cardNumber, int cardCVC, int cardEndMonth, int cardEndYear, double amount) {

        FakeIsBankManager fakeIsBankManager = new FakeIsBankManager();
        boolean result = fakeIsBankManager.makePayment(cardOwnerName, cardNumber, cardEndMonth, cardEndYear, cardCVC, amount);

        return result;
    }

}