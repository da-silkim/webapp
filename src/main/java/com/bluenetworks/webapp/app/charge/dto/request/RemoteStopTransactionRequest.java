package com.bluenetworks.webapp.app.charge.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RemoteStopTransactionRequest {

	private String chargePointId;
    private String transactionId;
    
}
