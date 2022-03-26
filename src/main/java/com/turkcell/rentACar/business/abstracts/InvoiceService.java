package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.request.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.DeleteInvoiceRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {

    int add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException;

    DataResult<InvoiceGetDto> getByInvoiceId(int invoiceId);

    DataResult<List<InvoiceListDto>> getByRent_RentId(int rentId);

    DataResult<List<InvoiceListDto>> getByRent_Customer_CustomerId(int customerId);

    DataResult<List<InvoiceListDto>> getAllInvoicesInSpecificDateRange(LocalDate fromDate,LocalDate toDate);

    DataResult<List<InvoiceListDto>> getAll();

    DataResult<List<InvoiceListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<InvoiceListDto>> getAllSorted(Sort.Direction direction);

    Result update(int invoiceId, UpdateInvoiceRequest updateInvoiceRequest) throws BusinessException;

    Result delete(DeleteInvoiceRequest deleteInvoiceRequest) throws BusinessException;

    double calculationTotalPayment(int rentId,LocalDate startDate,LocalDate endDate);

    void checkIfInvoiceExists(int invoiceId) throws BusinessException;
}
