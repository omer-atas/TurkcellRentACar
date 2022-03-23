package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.api.modals.PaymentPostServiceModal;
import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentGetDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.request.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.request.paymentRequests.DeletePaymentRequest;
import com.turkcell.rentACar.business.request.paymentRequests.UpdatePaymentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid PaymentPostServiceModal paymentPostServiceModal) throws BusinessException {
        return this.paymentService.add(paymentPostServiceModal);
    }

    @GetMapping("/getall")
    public DataResult<List<PaymentListDto>> getAll() {
        return this.paymentService.getAll();
    }

    @GetMapping("/getByPaymentId/{paymentId}")
    public DataResult<PaymentGetDto> getByPaymentId(@RequestParam("paymentId") int paymentId) {
        return this.paymentService.getByPaymentId(paymentId);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("paymentId") int paymentId,
                         @RequestBody @Valid UpdatePaymentRequest updatePaymentRequest) throws BusinessException {
        return this.paymentService.update(paymentId, updatePaymentRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeletePaymentRequest deletePaymentRequest) throws BusinessException {
        return this.paymentService.delete(deletePaymentRequest);
    }

}
