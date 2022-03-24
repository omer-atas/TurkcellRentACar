package com.turkcell.rentACar.business.adapters.postAdapters;

import com.turkcell.rentACar.core.bankServices.PostService;
import com.turkcell.rentACar.business.outServices.FakeZiraatBankManager;
import org.springframework.stereotype.Service;

@Service
public class ZiraatBankPostAdapter implements PostService {

    @Override
    public boolean makePayment(String cardOwnerName, String cardNumber, int cardCVC,int cardEndMonth, int cardEndYear,double amount) {

        FakeZiraatBankManager fakeZiraatBankManager = new FakeZiraatBankManager();
        boolean result =fakeZiraatBankManager.makePayment(cardOwnerName,cardNumber,cardEndMonth,cardEndYear,cardCVC,amount);

        return  result;
    }

}
