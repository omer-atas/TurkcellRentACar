package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.modals.PaymentPostServiceModal;
import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentGetDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.request.creditCartRequests.CreateCreditCardRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.CreateInvoiceRequest;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentManager implements PaymentService {

    private final PaymentDao paymentDao;
    private final ModelMapperService modelMapperService;
    private final InvoiceService invoiceService;
    private final PostService postService;
    private RentService rentService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;
    private IndividualCustomerService individualCustomerService;
    private CorporateCustomerService corporateCustomerService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService, InvoiceService invoiceService, PostService postService,
                          RentService rentService,OrderedAdditionalServiceService orderedAdditionalServiceService,
                          IndividualCustomerService individualCustomerService,CorporateCustomerService corporateCustomerService) {
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.invoiceService = invoiceService;
        this.postService = postService;
        this.rentService = rentService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.individualCustomerService = individualCustomerService;
        this.corporateCustomerService = corporateCustomerService;
    }

    @Override
    public Result addForIndividualCustomer(PaymentPostServiceModal paymentPostServiceModal) throws BusinessException {

        checkIfMakePayment(paymentPostServiceModal.getCreateCreditCardRequest());

        runPaymentSuccessor(paymentPostServiceModal);

        return new SuccessResult(BusinessMessages.PAYMENT_ADD);
    }

    @Transactional
    public void runPaymentSuccessor(PaymentPostServiceModal paymentPostServiceModal) throws BusinessException {

        //add rent -Individual Customer or Corporate Customer

        int rentId;

        if(this.individualCustomerService.getByIndividualCustomerId( paymentPostServiceModal.getCreateRentRequest().getCustomerId()).getData() != null){
            rentId = this.rentService.carRentalForIndividualCustomer(
                    paymentPostServiceModal.getCreateRentRequest());
        }else{
            rentId = this.rentService.carRentalForCorporateCustomer(paymentPostServiceModal.getCreateRentRequest());
        }

        //add ordered additonal service
        this.orderedAdditionalServiceService.addOrderedAdditionalServiceForPayment(paymentPostServiceModal
                .getCreateOrderedAdditionalServiceListRequests().getAdditionalServiceIds(), rentId);

        //add invoice

        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
        createInvoiceRequest.setRentId(rentId);

        int invoiceId = this.invoiceService.add(createInvoiceRequest);
        this.invoiceService.getByInvoiceId(invoiceId).getData().setInvoiceNo(String.valueOf(invoiceId));

        // add payment

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setInvoiceId(invoiceId);

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);
        payment.setAmount(this.invoiceService.getByInvoiceId(invoiceId).getData().getTotalPayment());

        this.paymentDao.save(payment);
    }

    private void checkIfMakePayment(CreateCreditCardRequest createCreditCardRequest) throws BusinessException {

        if (!this.postService.makePayment(createCreditCardRequest.getCardOwnerName(),
                createCreditCardRequest.getCardNumber(),
                createCreditCardRequest.getCardCVC(),
                createCreditCardRequest.getCardEndMonth(),
                createCreditCardRequest.getCardEndYear(),
                createCreditCardRequest.getTotalPrice())) {
            throw new BusinessException(BusinessMessages.PAYMENT_CAN_NOT_MAKE_PAYMENT);
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

        List<PaymentListDto> response = result.stream().map(payment -> this.modelMapperService.forDto().map(payment, PaymentListDto.class)).collect(Collectors.toList());

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
        if (this.paymentDao.getByPaymentId(paymentId) == null) {
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
