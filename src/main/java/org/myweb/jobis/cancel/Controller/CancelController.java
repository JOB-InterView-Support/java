package org.myweb.jobis.cancel.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.cancel.model.dto.CancelRequest;
import org.myweb.jobis.cancel.model.dto.CancelResponse;
import org.myweb.jobis.cancel.model.service.CancelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class CancelController {

    private final CancelService cancelService;

    @PostMapping("/{paymentKey}/cancel")
    public ResponseEntity<CancelResponse> cancelPayment(
            @PathVariable String paymentKey,
            @RequestBody CancelRequest cancelRequest) {

        log.info("결제 취소 요청 수신 - paymentKey: {}, reason: {}", paymentKey, cancelRequest.getReason());

        // CancelRequest에 paymentKey 설정
        cancelRequest.setPaymentKey(paymentKey);

        CancelResponse response = cancelService.cancelPayment(cancelRequest);

        return ResponseEntity.ok(response);
    }
}

