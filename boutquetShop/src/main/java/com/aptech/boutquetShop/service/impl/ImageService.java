package com.aptech.boutquetShop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImageService {

//	@Autowired
//	private ImageRepository imageRepository;
//
//	@Value("${upload.image.rootPath}")
//	private String uploadDir;
//
//	@Value("${upload.image.savePath}")
//	private String savePath;
//
////	public List<ImageSearchResultDTO> getImagesById(List<String> imgIds) {
////		ImageSearchReqDTO req = new ImageSearchReqDTO();
////		req.setIds(imgIds);
////		List<ImageSearchResultDTO> res = imageRepository.findImageDTOList(req);
////		return imageRepository.findImageDTOList(req);
////    }
//
//	public List<ImageSearchResultDTO> getImagesById(BaseResponseDTO dto) {
//
//		if (!StringUtils.isEmpty(dto.getImageList())) {
//			List<String> imgIds = new ArrayList<String>(Arrays.asList(dto.getImageList().split(",")));
//			ImageSearchReqDTO req = new ImageSearchReqDTO();
//			req.setIds(imgIds);
//
//			return imageRepository.findImageDTOList(req);
//		}
//		return null;
//	}
//
//	public List<String> createImage(MultipartFile[] files, String tableUpload) throws IOException {
//		if (files == null) {
//			return new ArrayList();
//		}
//		String filePath = uploadDir + "/" + tableUpload;
//		ImageUploadedDTO imageUploadedDTO = this.uploadImage(files, filePath, tableUpload);
//		imageRepository.saveAll(imageUploadedDTO.getImageEntities());
//		List<ImageSearchResultDTO> imageSearchResultDTOs = imageRepository.findImageDTOListByFileName(imageUploadedDTO);
//		List<String> lstImageId = new ArrayList<>();
//		imageSearchResultDTOs.stream().forEach(img -> {
//			lstImageId.add(img.getId());
//		});
//
//		return lstImageId;
//	}
//
//	public ImageUploadedDTO uploadImage(MultipartFile[] files, String filePath, String tableUpload) throws IOException {
//		ImageUploadedDTO imageUploadedDTO = new ImageUploadedDTO();
//		imageUploadedDTO.setFilePath(savePath + "/" + tableUpload);
//
//		List<String> fileNames = new ArrayList<>();
//
//		List<ImageEntity> imageEntities = new ArrayList<>();
//
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//		String current = df.format(new Date());
//
//		Path uploadPath = Paths.get(filePath);
//
//		try {
//			if (!Files.exists(uploadPath)) {
//				Files.createDirectories(uploadPath);
//			}
//
//			for (int i = 0; i < files.length; i++) {
//				InputStream inputStream = files[i].getInputStream();
//				String fileType = files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf("."));
//				String fileName = tableUpload + "_" + current + "_" + (i + 1) + fileType;
//				Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
//
//				ImageEntity image = new ImageEntity();
//				image.setFilePath(savePath + "/" + tableUpload);
//				image.setStatus(0);
//				image.setOrgFileName(files[i].getOriginalFilename());
//				image.setFileName(fileName);
//				image.setOrderNum(i);
//				imageEntities.add(image);
//				fileNames.add(fileName);
//
//			}
//		} catch (IOException e) {
//			throw new IOException("Upload Image Fail !!!");
//		}
//
//		imageUploadedDTO.setImageEntities(imageEntities);
//		imageUploadedDTO.setFileNames(fileNames);
//
//		return imageUploadedDTO;
//
//	}


}
