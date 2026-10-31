package com.aptech.boutquetShop.entities;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Data
@Embeddable
@ToString
public class BaseEntity {

	@Basic
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Basic
	@Column(name = "UpdatedAt")
	@UpdateTimestamp
	private Date updatedAt;

	@Basic
	@Column(name = "UserModifyId")
	private Integer userModifyId;

	@Basic
	@Column(name = "UserCreatedId")
	private Integer userCreatedId;

	@Basic
	@Column(name = "Status")
	private Integer status;
}
