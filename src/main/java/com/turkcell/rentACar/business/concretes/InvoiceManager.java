package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
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
    public Result add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException {

        checkIfRentAvaliable(createInvoiceRequest.getRentId());
        checkIfRentExists(createInvoiceRequest.getRentId());
        checkIfRentReturn(createInvoiceRequest.getRentId());
        checkIfInvoiceNoNotDuplicated(createInvoiceRequest.getInvoiceNo());

        Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);

        invoice.setTotalPayment(calculationTotalPayment(createInvoiceRequest.getRentId()));
        invoice.setCreationDate(LocalDate.now());

        this.invoiceDao.save(invoice);

        return new SuccessResult("Invoice added : " + invoice.getInvoiceId());
    }

    private void checkIfRentReturn(int rentId) throws BusinessException {
        if(this.invoiceDao.getByRent_RentId(rentId).getRent().getEndDate() == null){
            throw new BusinessException("The invoice could not be issued because the rental return information was not entered.");
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
    public double calculationTotalPayment(int rentId){

        double rentedCarTotalPrice = 0 , totalAdditionalServicesPrice = 0 ,totalpayment, citySwapPrice = 750.00;

        LocalDate startDate = this.rentService.getByRentId(rentId).getData().getStartingDate();
        LocalDate endDate = this.rentService.getByRentId(rentId).getData().getEndDate();

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

    @Override
    public void calculatingDailyPriceToSubtractAfterAdditionalServiceUpdate(double dailyPrice,int rentId){

        LocalDate startDate = this.rentService.getByRentId(rentId).getData().getStartingDate();
        LocalDate endDate = this.rentService.getByRentId(rentId).getData().getEndDate();

        double dailyPriceToSubtractAfterAdditionalServiceUpdate = dailyPrice * this.orderedAdditionalServiceService.findNoOfDaysBetween(startDate,endDate);
        double totalPayment = this.invoiceDao.getByRent_RentId(rentId).getTotalPayment();

        this.invoiceDao.getByRent_RentId(rentId).setTotalPayment(totalPayment-dailyPriceToSubtractAfterAdditionalServiceUpdate);
    }

    @Override
    public void calculatingDailyPriceToAddingAfterAdditionalServiceUpdate(double dailyPrice,int rentId){

        LocalDate startDate = this.rentService.getByRentId(rentId).getData().getStartingDate();
        System.out.println(startDate);

        LocalDate endDate = this.rentService.getByRentId(rentId).getData().getEndDate();
        System.out.println(endDate);

        double day = this.orderedAdditionalServiceService.findNoOfDaysBetween(startDate,endDate);
        System.out.println(day);

        double dailyPriceToSubtractAfterAdditionalServiceUpdate = dailyPrice * day;

        double totalPayment = this.invoiceDao.getByRent_RentId(rentId).getTotalPayment();

        this.invoiceDao.getByRent_RentId(rentId).setTotalPayment(totalPayment + dailyPriceToSubtractAfterAdditionalServiceUpdate);
    }



    private void checkIfRentAvaliable(int rentId) throws BusinessException {

        if(this.invoiceDao.getByRent_RentId(rentId) != null){
            throw new BusinessException("There is an invoice for the sent rent..");
        }

    }

    private void checkIfRentExists(int rentId) throws BusinessException {

        if(this.rentService.getByRentId(rentId) == null){
            throw new BusinessException("There is no rent corresponding to the sent id");
        }

    }

    private void checkIfInvoiceNoNotDuplicated(String invoiceNo) throws BusinessException {
        if(this.invoiceDao.existsByInvoiceNo(invoiceNo) ){
            throw new BusinessException("Invoice no can't be the same");
        }
    }

    @Override
    public DataResult<InvoiceGetDto> getByInvoiceId(int invoiceId) {

        Invoice result = this.invoiceDao.getByInvoiceId(invoiceId);

        if (result == null) {
            return new ErrorDataResult<InvoiceGetDto>("Invoice no not found");
        }

        InvoiceGetDto response = this.modelMapperService.forDto().map(result, InvoiceGetDto.class);


        return new SuccessDataResult<InvoiceGetDto>(response, "Success");
    }

    @Override
    public DataResult<InvoiceGetDto> getByRent_RentId(int rentId) {

        Invoice result = this.invoiceDao.getByRent_RentId(rentId);

        if (result == null) {
            return new ErrorDataResult<InvoiceGetDto>("Invoice no not found");
        }

        InvoiceGetDto response = this.modelMapperService.forDto().map(result, InvoiceGetDto.class);


        return new SuccessDataResult<InvoiceGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<InvoiceListDto>> getByRent_Customer_CustomerId(int customerId) {

        List<Invoice> result = this.invoiceDao.getByRent_Customer_CustomerId(customerId);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>("Gönderilen id'ye ait müşterinin fatura kaydı bulunmamaktadır");
        }

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllInvoicesInSpecificDateRange(LocalDate fromDate, LocalDate toDate) {

        List<Invoice> result = this.invoiceDao.getAllInvoicesInSpecificDateRange(fromDate,toDate);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>("Gönderilen aralıkta herhani bir fatura kaydı bulunmamaktadır");
        }

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAll() {

        List<Invoice> result = this.invoiceDao.findAll();

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Invoice> result = this.invoiceDao.findAll(pageable).getContent();

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Brands Listed Successfully");
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
            return new ErrorDataResult<List<InvoiceListDto>>("Invoices not list - getAllSorted -");
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

        return new SuccessResult(invoiceUpdate.getInvoiceId() + " updated..");
    }

    @Override
    public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) throws BusinessException {

        checkIfInvoiceExists(deleteInvoiceRequest.getInvoiceId());

        Invoice invoice = this.modelMapperService.forRequest().map(deleteInvoiceRequest, Invoice.class);

        this.invoiceDao.deleteById(invoice.getInvoiceId());

        return new SuccessResult(deleteInvoiceRequest.getInvoiceId() + " deleted..");
    }

    @Override
    public void extractionOfAdditionalServicesPrice(int orderedAdditionalServiceId) throws BusinessException {

        OrderedAdditionalServiceGetDto orderedAdditionalService = this.orderedAdditionalServiceService.getByOrderedAdditionalServiceId(orderedAdditionalServiceId).getData();

        LocalDate startingDate = this.rentService.getByRentId(orderedAdditionalService.getRentId()).getData().getStartingDate();
        LocalDate endDate = this.rentService.getByRentId(orderedAdditionalService.getRentId()).getData().getEndDate();

        double day = this.orderedAdditionalServiceService.findNoOfDaysBetween(startingDate,endDate);

        double dailyPrice = this.additionalServiceService.getByAdditionalServiceId(orderedAdditionalService.getAdditionalServiceId()).getData().getDailyPrice();

        double totalPayment = this.invoiceDao.getByRent_RentId(orderedAdditionalService.getRentId()).getTotalPayment();

        totalPayment -= (day * dailyPrice);

        int rentId = this.rentService.getByRentId(this.orderedAdditionalServiceService.getByOrderedAdditionalServiceId(orderedAdditionalServiceId).getData().getRentId()).getData().getRentId();

        UpdateInvoiceRequest updateInvoiceRequest = updatetotalPaymnetInInvoice(rentId,totalPayment);

        this.update(this.invoiceDao.getByRent_RentId(orderedAdditionalServiceId).getInvoiceId(),updateInvoiceRequest);

    }

    private UpdateInvoiceRequest updatetotalPaymnetInInvoice(int rentId,double totalpayment){

        Invoice invoice = this.invoiceDao.getByRent_RentId(rentId);
        UpdateInvoiceRequest updateInvoiceRequest = this.modelMapperService.forDto().map(invoice,UpdateInvoiceRequest.class);
        updateInvoiceRequest.setTotalPayment(totalpayment);

        return updateInvoiceRequest;
    }

    private void checkIfInvoiceExists(int invoiceId) throws BusinessException {

        if(this.invoiceDao.getByInvoiceId(invoiceId) == null){
            throw new BusinessException("There is no data in the id sent");
        }
    }
}