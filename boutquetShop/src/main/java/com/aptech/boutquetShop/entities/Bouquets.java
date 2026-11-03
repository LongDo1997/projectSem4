package com.aptech.boutquetShop.entities;

import com.aptech.boutquetShop.utils.Constants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "Bouquets")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bouquets extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Description")
	private String description;

	@Basic
	@Column(name = "Price")
	private Float price;

	@Basic
	@Column(name = "ImageUrl")
	private String imageUrl;

	@Basic
	@Column(name = "IsActive")
	private boolean isActive;

	@Basic
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "OccasionId")
    private Integer occasionId;

	@PrePersist
	private void beforeInsert() {
		this.createdAt = Constants.getCurrentDate();
	}

	@PreUpdate
	private void beforeUpdate() {

	}

}
