package es.sopra.prototype.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.sopra.prototype.vo.UserData;
import es.sopra.prototype.commons.ConstantsUrl;
import es.sopra.prototype.commons.ConstantsUrlParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(ConstantsUrl.API_USERDATA)
public interface UserDataApi {

	@ApiOperation(value = "View properties of user data selected", response = UserData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved user data"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
	//@CrossOrigin(origins = "http://localhost:8000", maxAge = 3600)
	@GetMapping(path = ConstantsUrl.API_USER + ConstantsUrlParams.ID, produces = "application/json")
	public UserData getProduct(Long id);
}
