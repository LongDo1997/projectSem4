package com.aptech.boutquetShop.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.aptech.boutquetShop.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Role")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Description")
	private String description;

	@Basic
	@Column(name = "ValiToDt")
	private Date valiToDt;

	@PrePersist
	private void beforeInsert() {
		this.valiToDt = Constants.getCurrentDate();
	}

	@PreUpdate
	private void beforeUpdate() {

	}

}
