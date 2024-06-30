package com.apacheIntegration.integrationOne.integrationOne.repositories;

import com.apacheIntegration.integrationOne.integrationOne.entities.FileDynamics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends	 JpaRepository<FileDynamics, Long> {

}
