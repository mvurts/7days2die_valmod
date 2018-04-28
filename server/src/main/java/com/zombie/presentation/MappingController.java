package com.zombie.presentation;

import com.zombie.infrastructure.MappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mvurts on 28.04.2018
 */
@Slf4j
@RestController
public class MappingController {

    private final MappingService mappingService;

    @Autowired
    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    @GetMapping("/mapping/reload_schema")
    public ResponseEntity reloadSchema() {
        try {
            mappingService.initSchemaData();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Cannot reload schema", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
