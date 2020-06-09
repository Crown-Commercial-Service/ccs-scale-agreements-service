package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Immutable
@Table(name = "lots")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lot {

	@Id
	@Column(name = "lot_id")
	Integer id;

	@Column(name = "lot_number")
	String number;

	@Column(name = "lot_name")
	String name;

	@Column(name = "lot_description")
	String description;

	@Column(name = "lot_type")
	String lotType;

	@Column(name = "start_date")
	LocalDate startDate;

	@Column(name = "end_date")
	LocalDate endDate;

	@ManyToOne
	@JoinColumn(name = "commercial_agreement_id")
	CommercialAgreement agreement;

	// @ManyToMany
	// @JoinTable(name = "lot_sectors", joinColumns = @JoinColumn(name = "lot_id"),
	// inverseJoinColumns = @JoinColumn(name = "sector_code"))
	// Set<Sector> sectors;

}
