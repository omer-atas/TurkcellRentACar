package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.request.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.DeleteInvoiceRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.InvoiceDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceManager implements InvoiceService{

    private InvoiceDao invoiceDao;
    private ModelMapperService modelMapperService;
    private RentService rentService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;
    private AdditionalServiceService additionalServiceService;

    @Autowired
    public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService, RentService rentService,
                          OrderedAdditionalServiceService orderedAdditionalServiceService,AdditionalServiceService additionalServiceService) {
        this.invoiceDao = invoiceDao;
        this.modelMapperService = modelMapperService;
        this.rentService = rentService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.additionalServiceService = additionalServiceService;
    }

    @Override
    public int add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException {

        checkIfRentExists(createInvoiceRequest.getRentId());
        checkIfRentReturn(createInvoiceRequest.getRentId());
        checkIfInvoiceNoNotDuplicated(createInvoiceRequest.getInvoiceNo());

        Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
        invoice.setInvoiceId(0);
        invoice.setTotalPayment(calculationTotalPayment(createInvoiceRequest.getRentId(),this.rentService.getByRentId(createInvoiceRequest.getRentId()).getData().getStartingDate(),
                this.rentService.getByRentId(createInvoiceRequest.getRentId()).getData().getEndDate()));
        invoice.setCreationDate(LocalDate.now());
        invoice.setStartingDate(this.rentService.getByRentId(createInvoiceRequest.getRentId()).getData().getStartingDate());
        invoice.setEndDate(this.rentService.getByRentId(createInvoiceRequest.getRentId()).getData().getEndDate());
        invoice.setTotalRentalDays(this.rentService.getByRentId(createInvoiceRequest.getRentId()).getData().getRentalPriceOfTheCar());
        invoice.setRentDay(this.orderedAdditionalServiceService.findNoOfDaysBetween(invoice.getStartingDate(),invoice.getEndDate()));

        this.invoiceDao.save(invoice);

        return invoice.getInvoiceId();
    }

    private void checkIfRentReturn(int rentId) throws BusinessException {
        if(this.rentService.getByRentId(rentId).getData().getEndDate() == null){
            throw new BusinessException(BusinessMessages.INVOICE_RENT_RETURN);
        }
    }

    private double sumOfAdditionalServicesPrice(int rentId){

        double totalAdditionalServicesPrice = 0;

        List<OrderedAdditionalServiceListDto> orderedAdditionalServices= this.orderedAdditionalServiceService.getByRent_RentId(rentId).getData();

        if(orderedAdditionalServices != null){
            for (OrderedAdditionalServiceListDto o : orderedAdditionalServices) {
                totalAdditionalServicesPrice += this.additionalServiceService.getByAdditionalServiceId(o.getOrderedAdditionalServiceId()).getData().getDailyPrice();
            }
        }

        return  totalAdditionalServicesPrice;
    }

    @Override
    public double calculationTotalPayment(int rentId,LocalDate startDate,LocalDate endDate){

        double rentedCarTotalPrice = 0 , totalAdditionalServicesPrice = 0 ,totalpayment, citySwapPrice = 750.00;

        totalAdditionalServicesPrice += sumOfAdditionalServicesPrice(rentId) *
                                                    this.orderedAdditionalServiceService.findNoOfDaysBetween(startDate,endDate);

        rentedCarTotalPrice += this.rentService.getByRentId(rentId).getData().getRentalPriceOfTheCar();

        if(this.rentService.getByRentId(rentId).getData().getToCityId() !=
                this.rentService.getByRentId(rentId).getData().getFromCityId()){
            totalpayment = totalAdditionalServicesPrice + rentedCarTotalPrice + citySwapPrice;
        }else{
            totalpayment = totalAdditionalServicesPrice + rentedCarTotalPrice;
        }

        return  totalpayment;
    }

    private void checkIfRentExists(int rentId) throws BusinessException {

        if(this.rentService.getByRentId(rentId) == null){
            throw new BusinessException(BusinessMessages.RENT_NOT_FOUND);
        }

    }

    private void checkIfInvoiceNoNotDuplicated(String invoiceNo) throws BusinessException {
        if(this.invoiceDao.existsByInvoiceNo(invoiceNo) ){
            throw new BusinessException(BusinessMessages.INVOICE_N0_NOT_DUPLICATED);
        }
    }

    @Override
    public DataResult<InvoiceGetDto> getByInvoiceId(int invoiceId) {

        Invoice result = this.invoiceDao.getByInvoiceId(invoiceId);

        if (result == null) {
            return new ErrorDataResult<InvoiceGetDto>(BusinessMessages.INVOICE_NOT_FOUND);
        }

        InvoiceGetDto response = this.modelMapperService.forDto().map(result, InvoiceGetDto.class);


        return new SuccessDataResult<InvoiceGetDto>(response, BusinessMessages.INVOICE_GET_BY_ID);
    }

    @Override
    public DataResult<InvoiceGetDto> getByRent_RentId(int rentId) {

        Invoice result = this.invoiceDao.getByRent_RentId(rentId);

        if (result == null) {
            return new ErrorDataResult<InvoiceGetDto>(BusinessMessages.INVOICE_RENT_NOT_FOUND);
        }

        InvoiceGetDto response = this.modelMapperService.forDto().map(result, InvoiceGetDto.class);


        return new SuccessDataResult<InvoiceGetDto>(response, BusinessMessages.INVOICE_GET_BY_RENT_ID);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getByRent_Customer_CustomerId(int customerId) {

        List<Invoice> result = this.invoiceDao.getByRent_Customer_CustomerId(customerId);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>(BusinessMessages.INVOICE_CUSTOMER_NOT_FOUND);
        }

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }

        return new SuccessDataResult<List<InvoiceListDto>>(response, BusinessMessages.INVOICE_CUSTOMER);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllInvoicesInSpecificDateRange(LocalDate fromDate, LocalDate toDate) {

        List<Invoice> result = this.invoiceDao.getAllInvoicesInSpecificDateRange(fromDate,toDate);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>(BusinessMessages.INVOICE_IN_SPECIFIC_DATE_RANGE);
        }

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAll() {

        List<Invoice> result = this.invoiceDao.findAll();

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response, BusinessMessages.INVOICE_GET_ALL);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Invoice> result = this.invoiceDao.findAll(pageable).getContent();

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response, BusinessMessages.INVOICE_GET_ALL_PAGED);
    }

    private List<InvoiceListDto> manuelMappingForGetAll(List<Invoice> result,List<InvoiceListDto> response){
        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }
        return  response;
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "invoiceNo");

        List<Invoice> result = this.invoiceDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>(BusinessMessages.INVOICE_GET_ALL_SORTED);
        }

        List<InvoiceListDto> response = result.stream()
                .map(brand -> this.modelMapperService.forDto().map(brand, InvoiceListDto.class))
                .collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response);
    }

    private void IdCorrector(Invoice invoice, Invoice invoiceUpdate) {
        invoiceUpdate.setInvoiceId(invoice.getInvoiceId());
        invoiceUpdate.setRent(invoice.getRent());
        invoiceUpdate.setInvoiceNo(invoice.getInvoiceNo());
    }

    @Override
    public Result update(int invoiceId, UpdateInvoiceRequest updateInvoiceRequest) throws BusinessException {

        checkIfInvoiceExists(invoiceId);

        Invoice invoice = this.invoiceDao.getByInvoiceId(invoiceId);

        Invoice invoiceUpdate = this.modelMapperService.forRequest().map(invoice, Invoice.class);

        IdCorrector(invoice, invoiceUpdate);

        invoiceUpdate.setTotalPayment(updateInvoiceRequest.getTotalPayment());

        this.invoiceDao.save(invoiceUpdate);

        return new SuccessResult(invoiceUpdate.getInvoiceId() + BusinessMessages.INVOICE_UPDATE);
    }

    @Override
    public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) throws BusinessException {

        checkIfInvoiceExists(deleteInvoiceRequest.getInvoiceId());

        Invoice invoice = this.modelMapperService.forRequest().map(deleteInvoiceRequest, Invoice.class);

        this.invoiceDao.deleteById(invoice.getInvoiceId());

        return new SuccessResult(deleteInvoiceRequest.getInvoiceId() +  BusinessMessages.INVOICE_DELETE);
    }

    @Override
    public void checkIfInvoiceExists(int invoiceId) throws BusinessException {

        if(this.invoiceDao.getByInvoiceId(invoiceId) == null){
            throw new BusinessException( BusinessMessages.INVOICE_NOT_FOUND);
        }
    }
}