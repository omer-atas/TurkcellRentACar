package com.turkcell.rentACar.business.adapters.bankAdapters;

import com.turkcell.rentACar.core.bankServices.PostService;
import com.turkcell.rentACar.core.bankServices.FakeZiraatBankManager;
import org.springframework.stereotype.Service;

@Service
public class ZiraatBankAdapter implements PostService {

    @Override
    public boolean addPayment(String cardOwnerName, String cardNumber, int cardCVC,int cardEndMonth, int cardEndYear,int amount) {

        FakeZiraatBankManager fakeZiraatBankManager = new FakeZiraatBankManager();
        return fakeZiraatBankManager.addPayments(cardNumber,cardOwnerName,cardEndMonth,cardEndYear,cardCVC,amount);
    }

}
