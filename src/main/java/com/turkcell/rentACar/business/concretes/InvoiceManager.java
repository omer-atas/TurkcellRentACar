package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.RentService;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
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
    private CustomerService customerService;

    @Autowired
    public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService,RentService rentService,CustomerService customerService) {
        this.invoiceDao = invoiceDao;
        this.modelMapperService = modelMapperService;
        this.rentService = rentService;
        this.customerService = customerService;
    }

    @Override
    public Result add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException {

        ccheckIfRentAvaliable(createInvoiceRequest.getRentId());
        checkIfRentExists(createInvoiceRequest.getRentId());
        checkIfInvoiceNoNotDuplicated(createInvoiceRequest.getInvoiceNo());

        Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);

        this.invoiceDao.save(invoice);

        return new SuccessResult("Invoice added : " + invoice.getInvoiceId());
    }

    private void ccheckIfRentAvaliable(int rentId) throws BusinessException {

        if(this.invoiceDao.getByRent_RentId(rentId) != null){
            throw new BusinessException("Göndeerilen kiranın faturası bulunmaktadır.");
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

        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAll() {

        List<Invoice> result = this.invoiceDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>("Invoices not listed");
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
    public DataResult<List<InvoiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Invoice> result = this.invoiceDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<InvoiceListDto>>("Invoices not list - getAllPaged - ");
        }

        List<InvoiceListDto> response = result.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());

        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }

        return new SuccessDataResult<List<InvoiceListDto>>(response, "Brands Listed Successfully");
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

        for(int i=0 ; i< result.size() ; i++){
            response.get(i).setCustomerId(result.get(i).getRent().getCustomer().getCustomerId());
        }

        return new SuccessDataResult<List<InvoiceListDto>>(response);
    }

    private void IdCorrector(Invoice invoice, Invoice invoiceUpdate) {
        invoiceUpdate.setInvoiceId(invoice.getInvoiceId());
        invoiceUpdate.setRent(invoice.getRent());
    }

    @Override
    public Result update(int invoiceId, UpdateInvoiceRequest updateInvoiceRequest) throws BusinessException {

        checkIfInvoiceNoNotDuplicated(updateInvoiceRequest.getInvoiceNo());

        Invoice invoice = this.invoiceDao.getByInvoiceId(invoiceId);

        Invoice invoiceUpdate = this.modelMapperService.forRequest().map(invoice, Invoice.class);

        IdCorrector(invoice, invoiceUpdate);

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

    private void checkIfInvoiceExists(int invoiceId) throws BusinessException {

        if(this.invoiceDao.getByInvoiceId(invoiceId) == null){
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
