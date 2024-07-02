package com.bluenetworks.webapp.app.charge.dto.request;

import com.bluenetworks.webapp.app.charge.dto.type.MessageTrigger;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TriggerMessageRequest {

	private MessageTrigger requestedMessage;
	private String chargePointId;
    private String connectorId;
    
}
