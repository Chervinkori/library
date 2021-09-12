package com.chweb.library.controller;

import com.chweb.library.service.example.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chroman <br>
 * 12.09.2021
 */
@Controller
@RequestMapping("/example")
@RequiredArgsConstructor
@Api(value = "example", description = "Test demonstration of functionality", tags = {"example"})
public class ExampleController {
    private final ExampleService service;

    @GetMapping("/send-notification/{mail}")
    @ApiOperation(value = "Send notification by mail")
    public ResponseEntity<Void> sendNotification(@PathVariable String mail) {
        service.sendNotification(mail);
        return ResponseEntity.ok().build();
    }
}
