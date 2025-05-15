package com.ghtk.auction.controller.payment;

import com.ghtk.auction.controller.BaseResource;
import com.ghtk.auction.dto.request.PaymentRequest;
import com.ghtk.auction.dto.request.vnpay.VNPayPaymentResponse;
import com.ghtk.auction.dto.response.ApiResponse;
import com.ghtk.auction.dto.response.PaymentResponse;
import com.ghtk.auction.dto.response.user.PageResponse;
import com.ghtk.auction.service.PaymentSerivceImpl;
import com.ghtk.auction.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseResource<PaymentRequest, PaymentResponse,PaymentSerivceImpl,Long> {
//    private final PaymentSerivceImpl paymentService;

    protected PaymentController(PaymentSerivceImpl service) {
        super(service);
    }

    @GetMapping("/vn-pay")
    public ApiResponse<VNPayPaymentResponse> pay(HttpServletRequest request) {
        return ApiResponse.success(service.createVnPayPayment(request));
    }
    @GetMapping("/vn-pay-callback")
    public ApiResponse<VNPayPaymentResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ApiResponse.success(VNPayPaymentResponse.builder()
                    .code("00")
                    .message("success")
                    .paymentUrl("")
                    .build());
        } else {
            return new ApiResponse<>();
        }
    }
    @GetMapping("/search")
    public ApiResponse<PageResponse<PaymentResponse>> search(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
    ){
        return ApiResponse.success(service.search(pageNo,pageSize));
    }
    @GetMapping("/search-my-payment")
    public ApiResponse<PageResponse<PaymentResponse>> searchMyPayment(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @AuthenticationPrincipal Jwt jwt
    ){
        return ApiResponse.success(service.searchMyPayment(pageNo,pageSize,jwt));
    }
}
