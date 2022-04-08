package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.request.invoiceRequests.DeleteInvoiceRequest;
import com.turkcell.rentACar.business.request.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoicesController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/getall")
    public DataResult<List<InvoiceListDto>> getAll() {
        return this.invoiceService.getAll();
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<InvoiceListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.invoiceService.getAllSorted(direction);
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<InvoiceListDto>> getAllPaged(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return this.invoiceService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getByInvoiceId/{invoiceId}")
    public DataResult<InvoiceGetDto> getByInvoiceId(@RequestParam("invoiceId") int invoiceId) {
        return this.invoiceService.getByInvoiceId(invoiceId);
    }

    @GetMapping("/getByRent_Customer_CustomerId/{customerId}")
    public DataResult<List<InvoiceListDto>> getByRent_Customer_CustomerId(@RequestParam("customerId") int customerId) {
        return this.invoiceService.getByRent_Customer_CustomerId(customerId);
    }

    @GetMapping("/getAllInvoicesInSpecificDateRange")
    public DataResult<List<InvoiceListDto>> getAllInvoicesInSpecificDateRange(@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return this.invoiceService.getAllInvoicesInSpecificDateRange(fromDate, toDate);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("invoiceId") int invoiceId, @RequestBody @Valid UpdateInvoiceRequest updateInvoiceRequest) throws BusinessException {
        return this.invoiceService.update(invoiceId, updateInvoiceRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteInvoiceRequest deleteInvoiceRequest) throws BusinessException {
        return this.invoiceService.delete(deleteInvoiceRequest);
    }
}
