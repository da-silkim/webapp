package com.bluenetworks.webapp.app.charge.dto.response;

import com.bluenetworks.webapp.app.charge.dto.type.RemoteStartStopStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteStartTransactionResponse {

	private RemoteStartStopStatus status;
}
