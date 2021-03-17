/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.portal.api;

import org.coredb.portal.model.AccountEntry;
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

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@Api(value = "account", description = "the account API")
public interface AccountApi {

    @ApiOperation(value = "", nickname = "addIdentity", notes = "Create a new identity", response = AccountEntry.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "account created", response = AccountEntry.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/account/identity",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<AccountEntry> addIdentity(@NotNull @ApiParam(value = "create account token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "password to use for new account", required = true) @Valid @RequestParam(value = "password", required = true) String password
);


    @ApiOperation(value = "", nickname = "changePassword", notes = "Change account password", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "account updated"),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/account/password",
        method = RequestMethod.PUT)
    ResponseEntity<Void> changePassword(@NotNull @ApiParam(value = "id of amigo to access", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "current password to use for login", required = true) @Valid @RequestParam(value = "current", required = true) String current
,@NotNull @ApiParam(value = "next password to use for login", required = true) @Valid @RequestParam(value = "next", required = true) String next
);


    @ApiOperation(value = "", nickname = "getRegistry", notes = "Default registry", response = String.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success") })
    @RequestMapping(value = "/account/registry",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> getRegistry();


    @ApiOperation(value = "", nickname = "getIdentity", notes = "Retrive account access token", response = AccountEntry.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "account created", response = AccountEntry.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/account/identity",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<AccountEntry> getIdentity(@NotNull @ApiParam(value = "id of amigo to access", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "password to use for login", required = true) @Valid @RequestParam(value = "password", required = true) String password
);


    @ApiOperation(value = "", nickname = "setIdentity", notes = "Attach existing identity", response = AccountEntry.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "account created", response = AccountEntry.class),
        @ApiResponse(code = 401, message = "invalid password"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/account/identity",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<AccountEntry> setIdentity(@NotNull @ApiParam(value = "reset account password token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "password to use for new account", required = true) @Valid @RequestParam(value = "password", required = true) String password
);

}


