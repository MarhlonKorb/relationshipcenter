package com.relationshipcenter.controllers;

import com.relationshipcenter.dto.RequestInput;
import com.relationshipcenter.services.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/request-assistance")
public class RequestAssistenceController {

    private final RequestService requestService;

    public RequestAssistenceController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<?> requestAssistance(@RequestBody RequestInput requestInput) {
        requestService.enqueue(requestInput.toModel());
        return ResponseEntity.ok().build();
    }
}
