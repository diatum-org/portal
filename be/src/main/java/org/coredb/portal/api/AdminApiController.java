package org.coredb.portal.api;

import org.coredb.portal.model.PortalConfig;
import org.coredb.portal.model.Domain;
import org.coredb.portal.model.PortalStat;
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

import org.coredb.portal.service.AuthService;
import org.coredb.portal.service.AdminService;

import java.nio.file.AccessDeniedException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AdminApiController implements AdminApi {

    private static final Logger log = LoggerFactory.getLogger(AdminApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AdminService adminService;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public AdminApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> addStat(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @Min(0) @Max(100) @ApiParam(value = "time", required = true, allowableValues = "") @Valid @RequestParam(value = "processor", required = true) Integer processor
,@NotNull @ApiParam(value = "current memory free", required = true) @Valid @RequestParam(value = "memory", required = true) Long memory
,@NotNull @ApiParam(value = "current storage free", required = true) @Valid @RequestParam(value = "storage", required = true) Long storage
) {
    try {
      authService.accessToken(token);
      adminService.addStat(processor, memory, storage);
      return new ResponseEntity<Void>(HttpStatus.OK);
    }
    catch(AccessDeniedException e) {
      return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<List<PortalStat>> getStats(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
    try {
      authService.accessToken(token);
      List<PortalStat> stats = adminService.getStats();
      return new ResponseEntity<List<PortalStat>>(stats, HttpStatus.OK);
    }
    catch(AccessDeniedException e) {
      return new ResponseEntity<List<PortalStat>>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      return new ResponseEntity<List<PortalStat>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<PortalConfig> getConfig(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
    try {
      authService.accessToken(token);
      PortalConfig config = adminService.getConfig();
      return new ResponseEntity<PortalConfig>(config, HttpStatus.OK);
    }
    catch(AccessDeniedException e) {
      return new ResponseEntity<PortalConfig>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      return new ResponseEntity<PortalConfig>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<Void> setPortalNode(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,
        @ApiParam(value = "portal node", required = false) @Valid @RequestParam(value = "value", required = false) String value) {
      try {
        authService.accessToken(token);
        adminService.setConfigNode(value);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); 
      }
      catch(Exception e) {
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    public ResponseEntity<Void> setPortalToken(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,
        @ApiParam(value = "access token", required = false) @Valid @RequestParam(value = "value", required = false) String value) {
      try {
        authService.accessToken(token);
        adminService.setConfigToken(value);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); 
      }
      catch(Exception e) {
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Domain> addDomain(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        authService.accessToken(token);
        Domain domain = adminService.addDomain();
        return new ResponseEntity<Domain>(domain, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<Domain>(HttpStatus.UNAUTHORIZED); 
      }
      catch(Exception e) {
        return new ResponseEntity<Domain>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Domain> setDomain(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,
        @ApiParam(value = "updated config", required = true )  @Valid @RequestBody Domain body) {
      try {
        authService.accessToken(token);
        Domain domain = adminService.updateDomain(body);
        return new ResponseEntity<Domain>(domain, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Domain>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<Domain>(HttpStatus.UNAUTHORIZED); 
      }
      catch(Exception e) {
        return new ResponseEntity<Domain>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}
