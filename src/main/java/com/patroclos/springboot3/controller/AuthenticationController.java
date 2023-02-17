package com.patroclos.springboot3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patroclos.springboot3.pojo.AuthenticationRequest;
import com.patroclos.springboot3.pojo.AuthenticationResponse;
import com.patroclos.springboot3.pojo.Book;
import com.patroclos.springboot3.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationService service;

  @Operation(summary = "Authenticate to get access JWT token")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Success Authentication", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Book.class)) }),
    @ApiResponse(responseCode = "400", description = "Failed Authentication", 
      content = @Content), 
    @ApiResponse(responseCode = "404", description = "Failed Authentication", 
      content = @Content) })
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }


}