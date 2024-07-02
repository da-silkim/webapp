package com.bluenetworks.webapp.app.charge.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@NoArgsConstructor
public class DataTransferDto {

	private String chargePointId;
	private String vendorId;
	private String messageId;
	private JsonNode data;

	public String DataTransferDtoToString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
