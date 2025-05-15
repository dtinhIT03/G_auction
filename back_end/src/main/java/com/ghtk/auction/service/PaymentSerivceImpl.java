package com.ghtk.auction.service;

import com.ghtk.auction.config.VNPayConfig;
import com.ghtk.auction.dto.request.PaymentRequest;
import com.ghtk.auction.dto.request.vnpay.VNPayPaymentResponse;
import com.ghtk.auction.dto.response.PaymentResponse;
import com.ghtk.auction.dto.response.auction.AuctionV1Response;
import com.ghtk.auction.dto.response.product.ProductV1Response;
import com.ghtk.auction.dto.response.user.PageResponse;
import com.ghtk.auction.dto.response.user.UserResponse;
import com.ghtk.auction.entity.Auction;
import com.ghtk.auction.entity.Payment;
import com.ghtk.auction.entity.User;
import com.ghtk.auction.enums.PaymentStatus;
import com.ghtk.auction.exception.NotFoundException;
import com.ghtk.auction.mapper.*;
import com.ghtk.auction.repository.AuctionRepository;
import com.ghtk.auction.repository.PaymentRepository;
import com.ghtk.auction.repository.UserRepository;
import com.ghtk.auction.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentSerivceImpl extends BaseService<Payment,Long, PaymentRequest, PaymentResponse,
        PaymentRepository, PaymentMapper>{
    private final VNPayConfig vnPayConfig;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductV1Mapper productMapper;
    private final AuctionV1Mapper auctionV1Mapper;

    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found"));
        UserResponse buyerRes = userMapper.toUserResponse(payment.getBuyer());
        User owner = userRepository.findById(payment.getAuction().getProduct().getOwnerId())
                .orElseThrow(() -> new NotFoundException("not found"));
        // map owner using userMapper
        UserResponse ownerRes = userMapper.toUserResponse(owner);

        // map product using ProductMapper
        ProductV1Response productRes = productMapper.convertEntity2Response(payment.getAuction().getProduct());
        productRes.setOwnerResponse(ownerRes);
        // map auction using AuctionMapper
        AuctionV1Response auctionRes = auctionV1Mapper.convertEntity2Response(payment.getAuction());
        auctionRes.setProductResponse(productRes);
        PaymentResponse paymentResponse = mapper.convertEntity2Response(payment);
        paymentResponse.setBuyerRes(buyerRes);
        paymentResponse.setAuctionRes(auctionRes);
        return paymentResponse;
    }

    public VNPayPaymentResponse createVnPayPayment(HttpServletRequest request){
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        long paymnent_id = Integer.parseInt(request.getParameter("payment_id"));
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", String.valueOf(paymnent_id));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VNPayPaymentResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }


    @Override
    protected void validRequestCreate(PaymentRequest request) {
        super.validRequestCreate(request);
    }

    @Override
    protected void validRequestUpdate(Long aLong, PaymentRequest request, Payment entity) {
        super.validRequestUpdate(aLong, request, entity);
    }

    @Override
    protected void afterMappingRequestToEntity(PaymentRequest request, Payment entity) {
         Auction auction =  auctionRepository.findById(request.getAuction_id())
                 .orElseThrow(() -> new NotFoundException("not found"));
        User buyer =  userRepository.findById(request.getBuyer_id())
                .orElseThrow(() -> new NotFoundException("not found"));
        entity.setAuction(auction);
        entity.setBuyer(buyer);
    }

    @Override
    protected void afterSaveEntity(Payment entity, PaymentRequest request) {
        super.afterSaveEntity(entity, request);
    }

    @Override
    protected void afterUpdateEntity(Payment entity, PaymentRequest request) {
        super.afterUpdateEntity(entity, request);
    }

    @Override
    protected void mapUpdatedEntity(PaymentRequest request, Payment entity) {
        if(PaymentStatus.PAID.equals(request.getStatus())){
            entity.setDatePayment(LocalDateTime.now());
        }
    }

    @Override
    protected void beforeDelete(Long aLong, Payment entity) {
        super.beforeDelete(aLong, entity);
    }

    public PageResponse<PaymentResponse> search(int pageNo, int pageSize) {
        long offset = pageNo * pageSize;
        List<Payment> payments = repository.search(offset, pageSize);

        List<PaymentResponse> paymentResponses = payments.stream().map(payment -> {
            // map buyer using userMapper
            UserResponse buyerRes = userMapper.toUserResponse(payment.getBuyer());
            User owner = userRepository.findById(payment.getAuction().getProduct().getOwnerId())
                    .orElseThrow(() -> new NotFoundException("not found"));
            // map owner using userMapper
            UserResponse ownerRes = userMapper.toUserResponse(owner);

            // map product using ProductMapper
            ProductV1Response productRes = productMapper.convertEntity2Response(payment.getAuction().getProduct());
            productRes.setOwnerResponse(ownerRes);
            // map auction using AuctionMapper
            AuctionV1Response auctionRes = auctionV1Mapper.convertEntity2Response(payment.getAuction());
            auctionRes.setProductResponse(productRes);
            // map payment
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .paymentMethod(payment.getPaymentMethod())
                    .depositAmount(payment.getDepositAmount())
                    .deadline(payment.getDeadline())
                    .datePayment(payment.getDatePayment())
                    .buyerRes(buyerRes) // use buyerRes here
                    .auctionRes(auctionRes) // use auctionRes here
                    .build();

        }).toList();

        return PageResponse.<PaymentResponse>builder()
                .content(paymentResponses)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
    }


    public PageResponse<PaymentResponse> searchMyPayment(int pageNo, int pageSize, Jwt jwt) {
        Long userId = (Long)jwt.getClaims().get("id");
        User user = userRepository.findById(userId).orElseThrow(
                () ->  new NotFoundException("Khong tim thay user hop le")
        );
        long offset = pageNo * pageSize;
        List<Payment> payments = repository.searchMyPayment(userId,offset, pageSize);

        List<PaymentResponse> paymentResponses = payments.stream().map(payment -> {
            // map buyer using userMapper
            UserResponse buyerRes = userMapper.toUserResponse(payment.getBuyer());
            User owner = userRepository.findById(payment.getAuction().getProduct().getOwnerId())
                    .orElseThrow(() -> new NotFoundException("not found"));
            // map owner using userMapper
            UserResponse ownerRes = userMapper.toUserResponse(owner);

            // map product using ProductMapper
            ProductV1Response productRes = productMapper.convertEntity2Response(payment.getAuction().getProduct());
            productRes.setOwnerResponse(ownerRes);
            // map auction using AuctionMapper
            AuctionV1Response auctionRes = auctionV1Mapper.convertEntity2Response(payment.getAuction());
            auctionRes.setProductResponse(productRes);
            // map payment
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .paymentMethod(payment.getPaymentMethod())
                    .depositAmount(payment.getDepositAmount())
                    .deadline(payment.getDeadline())
                    .datePayment(payment.getDatePayment())
                    .buyerRes(buyerRes) // use buyerRes here
                    .auctionRes(auctionRes) // use auctionRes here
                    .build();

        }).toList();

        return PageResponse.<PaymentResponse>builder()
                .content(paymentResponses)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
    }

}
