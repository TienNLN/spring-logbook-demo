package com.tiennln.springlogbookokhttpdemo.controllers;

import com.tiennln.springlogbookokhttpdemo.dtos.ResponseDTO;
import com.tiennln.springlogbookokhttpdemo.dtos.ValidateUniqueFieldRequestDTO;
import com.tiennln.springlogbookokhttpdemo.dtos.ValidateUniqueFieldResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author TienNLN on 10/01/2023
 */
@RestController
@RequestMapping("/api/tests")
@Slf4j
public class TestControllers {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLogTest() {
        return new ResponseEntity<>(
                new ResponseDTO(null, "Success", 200),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/validateUniqueField/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateUniqueField(@PathVariable String email) {

        HttpHeaders header = new HttpHeaders();

        header.setContentType(MediaType.APPLICATION_JSON);
        header.setCacheControl("no-cache");

        header.add("Asc.PosID", "App");
        header.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjgiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiZnB0YXBpIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZW1haWxhZGRyZXNzIjoiZnB0YXBpQGFzY2VudGlzLmNvbS5zZyIsIkFzcE5ldC5JZGVudGl0eS5TZWN1cml0eVN0YW1wIjoiUVVSRkg2QkFBM0pUSkw0WVNJSUdNWlhOQ1E3WTJXV0QiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJWZW5kb3IiLCJodHRwOi8vd3d3LmFzcG5ldGJvaWxlcnBsYXRlLmNvbS9pZGVudGl0eS9jbGFpbXMvdGVuYW50SWQiOiIzMDMxIiwic3ViIjoiOCIsImp0aSI6IjcyZjNkNDFlLTk4ZTYtNDIzZi05ZDgwLWM5NjQ2NGY2NmI3MyIsImlhdCI6MTY3MjIxODM0NiwibmJmIjoxNjcyMjE4MzQ2LCJleHAiOjE2NzIzMDQ3NDYsImlzcyI6IlNwcm9qIiwiYXVkIjoiU3Byb2oifQ.GiSq7NHCCQ_5Xk0iadIomXbGE9e6WTG-rUnbTZ7LUu4");

        HttpEntity<ValidateUniqueFieldRequestDTO> request = new HttpEntity<>(
                ValidateUniqueFieldRequestDTO.builder()
                        .field("Email")
                        .value("nhintn@fsoft.com.vn")
                        .crmMemberID("")
                        .build(),
                header
        );

        ResponseEntity<ValidateUniqueFieldResponseDTO> response = restTemplate.exchange("https://ionorchard.uat-mms.ascentis.com.sg/gateway/api/services/app/MemberAccount/ValidateUniqueField",
                HttpMethod.POST,
                request,
                ValidateUniqueFieldResponseDTO.class);

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
