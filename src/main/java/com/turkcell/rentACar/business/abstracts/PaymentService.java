package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.api.modals.PaymentPostServiceModal;
import com.turkcell.rentACar.api.modals.RentEndDateDelayPostServiceModal;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentGetDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.request.paymentRequests.DeletePaymentRequest;
import com.turkcell.rentACar.business.request.paymentRequests.UpdatePaymentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface PaymentService {

    Result addForIndıvıdualCustomer(PaymentPostServiceModal paymentPostServiceModal) throws BusinessException;

    Result gettingPaidForIndividualCustomerDelayEndDate(RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal, int rentId, double totalRentalPriceForTheDelayEndDateOfTheCar) throws BusinessException;

    Result gettingPaidForCorporateCustomerDelayEndDate(RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal, int rentId, double totalRentalPriceForTheDelayEndDateOfTheCar) throws BusinessException;

    Result addForCorporateCustomer(PaymentPostServiceModal paymentPostServiceModal) throws BusinessException;

    DataResult<PaymentGetDto> getByPaymentId(int paymentId);

    DataResult<List<PaymentListDto>> getAll();

    Result update(int paymentId, UpdatePaymentRequest updatePaymentRequest) throws BusinessException;

    Result delete(DeletePaymentRequest deletePaymentRequest) throws BusinessException;
}
