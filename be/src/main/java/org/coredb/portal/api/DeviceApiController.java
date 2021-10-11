package org.coredb.portal.api;

import org.coredb.portal.model.DeviceEntry;
import org.coredb.portal.model.DeviceParams;
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

import org.coredb.portal.service.DeviceService;
import javax.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class DeviceApiController implements DeviceApi {

    private static final Logger log = LoggerFactory.getLogger(DeviceApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private DeviceService deviceService;

    @org.springframework.beans.factory.annotation.Autowired
    public DeviceApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> addChallenge(@NotNull @ApiParam(value = "device token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @NotNull @ApiParam(value = "device name", required = true) @Valid @RequestParam(value = "name", required = true) String name, @NotNull @ApiParam(value = "device value", required = true) @Valid @RequestParam(value = "value", required = true) String value) {
      try {
        deviceService.addChallenge(token, name, value);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
      }
    }

    public ResponseEntity<Void> removeChallenge(@NotNull @ApiParam(value = "device token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @NotNull @ApiParam(value = "device name", required = true) @Valid @RequestParam(value = "name", required = true) String name, @NotNull @ApiParam(value = "device value", required = true) @Valid @RequestParam(value = "value", required = true) String value) {
      try {
        deviceService.removeChallenge(token, name, value);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
      }
    }

    public ResponseEntity<String> addDevice(@ApiParam(value = "parameters for device"  )  @Valid @RequestBody DeviceParams body, HttpServletRequest request) {
      try {
        String token = deviceService.addDevice(body, request.getRemoteAddr());        
        return new ResponseEntity<String>(token, HttpStatus.OK);
      }
      catch(IllegalStateException e) {
        return new ResponseEntity<String>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getAvailable() {
      try {
        Long certs = deviceService.getAvailable();
        return new ResponseEntity<Integer>(certs.intValue(), HttpStatus.OK);
      }
      catch(Exception e) {
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Boolean> checkLogin(@NotNull @ApiParam(value = "device login", required = true) @Valid @RequestParam(value = "login", required = true) String login) {
      try {
        Boolean status = deviceService.checkLogin(login);
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
      }
      catch(Exception e) {
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Boolean> checkToken(@NotNull @ApiParam(value = "expiring token", required = true) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        Boolean status = deviceService.checkToken(token);
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<String> createAccount(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "password", required = true) String password
,@NotNull @ApiParam(value = "expire seconds", required = true) @Valid @RequestParam(value = "expire", required = true) Integer expire
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
) {
    try {
      String url = deviceService.createAccount(login, password, expire);
      return new ResponseEntity<String>(url, HttpStatus.OK);
    }
    catch(NotFoundException e) {
      return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    catch(AccessDeniedException e) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<DeviceEntry> getDevice(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "password", required = true) String password
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
) {
      try {
        DeviceEntry entry = deviceService.getDevice(login, password);
        return new ResponseEntity<DeviceEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<DeviceEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<DeviceEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        return new ResponseEntity<DeviceEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<String> resetAccount(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "password", required = true) String password
,@NotNull @ApiParam(value = "expire seconds", required = true) @Valid @RequestParam(value = "expire", required = true) Integer expire
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
,@ApiParam(value = "id to reset") @Valid @RequestParam(value = "amigoId", required = false) String amigoId
) {
    try {
      String url = deviceService.resetAccount(login, password, expire, amigoId);
      return new ResponseEntity<String>(url, HttpStatus.OK);
    }
    catch(NotFoundException e) {
      return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    catch(AccessDeniedException e) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<Void> testPort(@NotNull @ApiParam(value = "port to test", required = true) @Valid @RequestParam(value = "value", required = true) Integer value, HttpServletRequest request) {
      try {
        deviceService.testPort(request.getRemoteAddr(), value);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(Exception e) {
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

    public ResponseEntity<Void> updateAddress(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token, HttpServletRequest request
) {
      try {
        deviceService.setAddress(token, request.getRemoteAddr());
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> updatePassword(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "current", required = true) String current
,@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "next", required = true) String next
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
) {
      try {
        deviceService.setPassword(login, current, next);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(NotFoundException e) {
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

