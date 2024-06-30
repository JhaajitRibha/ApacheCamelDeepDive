package com.apacheIntegration.integrationOne.integrationOne.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apacheIntegration.integrationOne.integrationOne.entities.FileDynamics;
import com.apacheIntegration.integrationOne.integrationOne.services.FIleDynamicsService;

@RestController
@RequestMapping("/v1/api/files")
public class FileDynamicController {
	@Autowired
	private FIleDynamicsService fileDynamicService;

	@PostMapping(value="/saveData",consumes = {"application/json","application/xml"},produces= {"application/json","application/xml"})
	public ResponseEntity<FileDynamics> saveFileData(@RequestBody FileDynamics fileData) {
		try {
			FileDynamics savedFileData = fileDynamicService.saveFile(fileData);
			return ResponseEntity.ok(savedFileData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
