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

@Table(name = "Users")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Basic
	@Column(name = "Email")
	private String email;

	@Basic
	@Column(name = "PasswordHash")
	private String passwordHash;

	@Basic
	@Column(name = "Dob")
	private String dob;

	@Basic
	@Column(name = "Gender")
	private Integer gender;

	@Column(name = "Phone")
	private String phone;

	@Basic
	@Column(name = "Address")
	private String address;

	@Column(name = "CreatedAt")
	private Date createdAt;

	@Basic
	@Column(name = "Role")
	private String role;

	@PrePersist
	private void beforeInsert() {
		System.out.println( Constants.getCurrentDate());
		createdAt = Constants.getCurrentDate();
	}

	@PreUpdate
	private void beforeUpdate() {

	}
}
