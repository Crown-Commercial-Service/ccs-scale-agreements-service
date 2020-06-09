package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

//@Entity
//@Immutable
//@Table(name = "lot_sectors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sector {

	@Id
	@Column(name = "sector_code")
	String code;

	@Column(name = "sector_name")
	String name;

	@Column(name = "sector_description")
	String description;

	@ManyToMany(mappedBy = "sectors")
	Set<Lot> lots;
}
