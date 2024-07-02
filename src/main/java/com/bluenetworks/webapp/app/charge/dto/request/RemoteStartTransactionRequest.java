package com.bluenetworks.webapp.app.charge.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RemoteStartTransactionRequest {

	private String chargePointId;

    private String idTag;
    private Integer connectorId;
    private Long chargingProfileId;
    
}
