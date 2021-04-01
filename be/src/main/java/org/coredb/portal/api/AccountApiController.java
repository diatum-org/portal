package org.coredb.portal.api;

import org.coredb.portal.model.AccountEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpHeaders;

import org.coredb.portal.service.AccountService;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.nio.file.AccessDeniedException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AccountApiController implements AccountApi {

    private static final Logger log = LoggerFactory.getLogger(AccountApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AccountService accountService;    

    @org.springframework.beans.factory.annotation.Autowired
    public AccountApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AccountEntry> addIdentity(@NotNull @ApiParam(value = "create account token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "password to use for new account", required = true) @Valid @RequestParam(value = "password", required = true) String password
) {
      try {
        AccountEntry entry = accountService.createIdentity(token, password);
        return new ResponseEntity<AccountEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(RestClientException e) {      
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.SERVICE_UNAVAILABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("details", e.getMessage());
        return new ResponseEntity<AccountEntry>(headers, HttpStatus.SERVICE_UNAVAILABLE);
      }
    }

    public ResponseEntity<String> setPassCode(@NotNull @ApiParam(value = "id of amigo to access", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "account login password", required = true) @Valid @RequestParam(value = "password", required = true) String password
) {
      try {
        String code = accountService.setPassCode(amigoId, password);
        return new ResponseEntity<String>("\"" + code + "\"", HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> changePassword(@NotNull @ApiParam(value = "id of amigo to access", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "current password to use for login", required = true) @Valid @RequestParam(value = "current", required = true) String current
,@NotNull @ApiParam(value = "next password to use for login", required = true) @Valid @RequestParam(value = "next", required = true) String next
) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> getRegistry() {
      try {
        String registry = accountService.getRegistry();
        return new ResponseEntity<String>("\"" + registry + "\"", HttpStatus.OK);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AccountEntry> getIdentity(@NotNull @ApiParam(value = "id of amigo to access", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "password to use for login", required = true) @Valid @RequestParam(value = "password", required = true) String password
) {
      try {
        AccountEntry entry = accountService.getIdentity(amigoId, password);
        return new ResponseEntity<AccountEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AccountEntry> setIdentity(@NotNull @ApiParam(value = "reset account password token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "password to use for new account", required = true) @Valid @RequestParam(value = "password", required = true) String password
) {
      try {
        AccountEntry entry = accountService.setIdentity(token, password);
        return new ResponseEntity<AccountEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(RestClientException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.SERVICE_UNAVAILABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("details", e.getMessage());
        return new ResponseEntity<AccountEntry>(headers, HttpStatus.SERVICE_UNAVAILABLE);
      }
    }

}

