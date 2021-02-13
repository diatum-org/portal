/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.portal.api;

import org.coredb.portal.model.DeviceEntry;
import org.coredb.portal.model.DeviceParams;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@Api(value = "device", description = "the device API")
public interface DeviceApi {


    @ApiOperation(value = "", nickname = "addDevice", notes = "Register a device name", response = String.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = String.class),
        @ApiResponse(code = 406, message = "device limit reached"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<String> addDevice(@ApiParam(value = "parameters for device"  )  @Valid @RequestBody DeviceParams body, HttpServletRequest request
);


    @ApiOperation(value = "", nickname = "checkLogin", notes = "check if login is available", response = Boolean.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "update successful", response = Boolean.class),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/login",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Boolean> checkLogin(@NotNull @ApiParam(value = "device login", required = true) @Valid @RequestParam(value = "login", required = true) String login
);


    @ApiOperation(value = "", nickname = "getAvailable", notes = "check if cert can be issued", response = Integer.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "ok", response = Integer.class),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/available",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> getAvailable();


    @ApiOperation(value = "", nickname = "checkToken", notes = "check if token is valid", response = Boolean.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "ok", response = Boolean.class),
        @ApiResponse(code = 404, message = "not found"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/token",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Boolean> checkToken(@NotNull @ApiParam(value = "expiring token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "createAccount", notes = "Generate create account link", response = String.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "ok", response = String.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/accounts",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<String> createAccount(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "password", required = true) String password
,@NotNull @ApiParam(value = "expire seconds", required = true) @Valid @RequestParam(value = "expire", required = true) Integer expire
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
);


    @ApiOperation(value = "", nickname = "getDevice", notes = "Retrieve record", response = DeviceEntry.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful request", response = DeviceEntry.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<DeviceEntry> getDevice(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "passowrd", required = true) String passowrd
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
);


    @ApiOperation(value = "", nickname = "resetAccount", notes = "Generate reset account password link", response = String.class, tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "ok", response = String.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/accounts",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<String> resetAccount(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "password", required = true) String password
,@NotNull @ApiParam(value = "expire seconds", required = true) @Valid @RequestParam(value = "expire", required = true) Integer expire
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
,@ApiParam(value = "id to reset") @Valid @RequestParam(value = "emigoId", required = false) String emigoId
);


    @ApiOperation(value = "", nickname = "testPort", notes = "Test port forwarding", tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "update successful"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/port",
        method = RequestMethod.PUT)
    ResponseEntity<Void> testPort(@NotNull @ApiParam(value = "port to test", required = true) @Valid @RequestParam(value = "value", required = true) Integer value, HttpServletRequest request
);


    @ApiOperation(value = "", nickname = "updateAddress", notes = "Update address", tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "update successful"),
        @ApiResponse(code = 401, message = "invalid token"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/address",
        method = RequestMethod.PUT)
    ResponseEntity<Void> updateAddress(@NotNull @ApiParam(value = "access token", required = true) @Valid @RequestParam(value = "token", required = true) String token, HttpServletRequest request
);


    @ApiOperation(value = "", nickname = "updatePassword", notes = "Update password", tags={ "device", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "update successful"),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/device/password",
        method = RequestMethod.PUT)
    ResponseEntity<Void> updatePassword(@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "current", required = true) String current
,@NotNull @ApiParam(value = "user password", required = true) @Valid @RequestParam(value = "next", required = true) String next
,@ApiParam(value = "user login") @Valid @RequestParam(value = "login", required = false) String login
);

}

