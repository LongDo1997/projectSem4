package com.aptech.boutquetShop.entities;

import com.aptech.boutquetShop.utils.Constants;

import javax.persistence.*;

//@Table(name = "Book")
//@Entity
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
public class BookEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Author")
	private String author;

	@Column(name = "Description")
	private String description;

	@Basic
	@Column(name = "Price")
	private Float price;

	@Basic
	@Column(name = "Quantity")
	private Float quantity;

	@Basic
	@Column(name = "ImageList")
	private String imageList;

	@Basic
	@Column(name = "ValiToDt")
	private String valiToDt;

	@Column(name = "CategoryId")
    private Integer categoryId;

	@PrePersist
	private void beforeInsert() {
		this.valiToDt = Constants.MAXIMUM_DATE_STRING;
	}

	@PreUpdate
	private void beforeUpdate() {

	}

}
