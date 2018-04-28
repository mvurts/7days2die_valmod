package com.zombie.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mvurts on 27.04.2018
 */
@RestController
public class EchoController {

    @GetMapping("/echo")
    public ResponseEntity getInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(new EchoInfo("7days2die browser", "1.0"));
    }


    @NoArgsConstructor
    @AllArgsConstructor
    public static class EchoInfo {
        @Getter @Setter private String name;
        @Getter @Setter private String version;
    }

}
