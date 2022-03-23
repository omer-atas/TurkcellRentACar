package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.modals.PaymentPostServiceModal;
import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.abstracts.RentService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentGetDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.request.creditCartRequests.CreateCreditCardRequest;
import com.turkcell.rentACar.business.request.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.request.paymentRequests.DeletePaymentRequest;
import com.turkcell.rentACar.business.request.paymentRequests.UpdatePaymentRequest;
import com.turkcell.rentACar.core.bankServices.PostService;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACar.entities.concretes.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentManager implements PaymentService {

    private PaymentDao paymentDao;
    private ModelMapperService modelMapperService;
    private InvoiceService invoiceService;
    private PostService postService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService,InvoiceService invoiceService,PostService postService) {
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.invoiceService = invoiceService;
        this.postService = postService;
    }

    @Override
    public Result add(PaymentPostServiceModal paymentPostServiceModal) throws BusinessException {


        this.invoiceService.checkIfInvoiceExists(paymentPostServiceModal.getCreatePaymentRequest().getInvoiceId());
        checkIfPaymentForTheInvoice(paymentPostServiceModal.getCreatePaymentRequest().getInvoiceId());
        checkIfRentPossible(paymentPostServiceModal.getCreateCreditCardRequest(),
                this.invoiceService.getByInvoiceId(paymentPostServiceModal.getCreatePaymentRequest().getInvoiceId()).getData().getTotalPayment());

        Payment payment = this.modelMapperService.forRequest().map(paymentPostServiceModal.getCreateCreditCardRequest(), Payment.class);
        payment.setPaymentId(0);
        payment.setAmount(this.invoiceService.getByInvoiceId(paymentPostServiceModal.getCreatePaymentRequest().getInvoiceId()).getData().getTotalPayment());

        this.paymentDao.save(payment);

        return new SuccessResult(BusinessMessages.PAYMENT_ADD + payment.getPaymentId());
    }

    private void checkIfPaymentForTheInvoice(int invoiceId) throws BusinessException {
        if(this.paymentDao.getByInvoice_InvoiceId(invoiceId) != null){
            throw new BusinessException(BusinessMessages.PAYMENT_FOR_THE_INVOICE);
        }
    }

    private void checkIfRentPossible(CreateCreditCardRequest createCreditCardRequest,double totalPayment) throws BusinessException {

        if (!this.postService.addPayment(createCreditCardRequest.getCardOwnerName(), createCreditCardRequest.getCardNumber(), createCreditCardRequest.getCardCVC(),
                createCreditCardRequest.getCardEndMonth(), createCreditCardRequest.getCardEndYear(), totalPayment)) {
            throw new BusinessException(BusinessMessages.RENT_CAN_NOT_MAKE_PAYMENT);
        }
    }

    @Override
    public DataResult<PaymentGetDto> getByPaymentId(int paymentId) {

        Payment result = this.paymentDao.getByPaymentId(paymentId);

        if (result == null) {
            return new ErrorDataResult<PaymentGetDto>(BusinessMessages.PAYMENT_NOT_FOUND);
        }

        PaymentGetDto response = this.modelMapperService.forDto().map(result, PaymentGetDto.class);

        return new SuccessDataResult<PaymentGetDto>(response, BusinessMessages.PAYMENT_GET_BY_ID);
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll() {

        List<Payment> result = this.paymentDao.findAll();

        List<PaymentListDto> response = result.stream()
                .map(payment -> this.modelMapperService.forDto().map(payment, PaymentListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<PaymentListDto>>(response, BusinessMessages.PAYMENT_GET_ALL);
    }

    @Override
    public Result update(int paymentId, UpdatePaymentRequest updatePaymentRequest) throws BusinessException {

        checkIfPaymentExists(paymentId);

        Payment payment = this.paymentDao.getByPaymentId(paymentId);

        Payment paymentUpdate = this.modelMapperService.forRequest().map(updatePaymentRequest, Payment.class);

        IdCorrector(payment, paymentUpdate);

        this.paymentDao.save(paymentUpdate);

        return new SuccessResult(payment.getPaymentId() + BusinessMessages.PAYMENT_UPDATE);
    }

    private void checkIfPaymentExists(int paymentId) throws BusinessException {
        if(this.paymentDao.getByPaymentId(paymentId) == null){
            throw new BusinessException(BusinessMessages.PAYMENT_NOT_FOUND);
        }
    }

    private void IdCorrector(Payment payment, Payment paymentUpdate) {
        paymentUpdate.setPaymentId(payment.getPaymentId());
        paymentUpdate.setInvoice(payment.getInvoice());
    }

    @Override
    public Result delete(DeletePaymentRequest deletePaymentRequest) throws BusinessException {

        checkIfPaymentExists(deletePaymentRequest.getPaymentId());

        Payment payment = this.modelMapperService.forRequest().map(deletePaymentRequest, Payment.class);

        this.paymentDao.deleteById(payment.getPaymentId());

        return new SuccessResult(deletePaymentRequest.getPaymentId() + BusinessMessages.PAYMENT_DELETE);
    }
}
