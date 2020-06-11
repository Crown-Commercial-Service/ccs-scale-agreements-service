package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class LotRouteToMarketKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "lot_id")
	Integer lotId;

	String routeToMarketName;

}
