package com.aptech.boutquetShop.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Image")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "FileName")
	private String fileName;

	@Lob
	@Column(name = "FilePath", columnDefinition = "TEXT")
	private String filePath;

	@Column(name = "OrgFileName")
	private String orgFileName;

	@Basic
	@Column(name = "OrderNum")
	private Integer orderNum;

}
