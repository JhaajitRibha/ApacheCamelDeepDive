package com.apacheIntegration.integrationOne.integrationOne.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apacheIntegration.integrationOne.integrationOne.entities.FileDynamics;
import com.apacheIntegration.integrationOne.integrationOne.repositories.FileRepository;

@Service
public class FIleDynamicsService {

	@Autowired
	private FileRepository fileRepository;
	
	public FileDynamics saveFile(FileDynamics fileDynamics) {
		return fileRepository.save(fileDynamics);
		
	}
}
