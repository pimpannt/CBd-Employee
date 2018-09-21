package hello;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.xml.ws.http.HTTPException;

@EnableSwagger2
@Api(tags = "CbD-Employee", value = "/demo", description = "CbD Employee Demo")
@Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called employeeRepository
	           // Which is auto-generated by Spring, we will use it to handle the data
	private EmployeeRepository employeeRepository;


	@ApiOperation(value = "Create new CbD employee", nickname = "getAllEmployee", notes = "This api is used to create new CbD employee.Only employee ID is required")
	// Define information of HTTP response for this API for displaying on Swagger
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal server error occurred"),
			@ApiResponse(code = 503, message = "Service Unavailable")})
	@PostMapping(path = "/add")
	public @ResponseBody String addNewEmployee(@RequestParam String empID,
											   @RequestParam(value = "name",defaultValue = " ") String name,
											   @RequestParam(value = "project",defaultValue = " ") String project,
											   @RequestParam(value = "hobby",defaultValue = " ") String hobby){
		//check for Bad Request
		if (!empID.startsWith("ZZ00")){
			sendBadRequest();
			return "Wrong format employee ID";
		}
		else {
			Employee e = new Employee();
			e.setEMPLOYEE_ID(empID);
			e.setNAME(name);
			e.setPROJECT(project);
			e.setHOBBY(hobby);
			employeeRepository.save(e);
			return "Saved";
		}

	}


	@ApiOperation(value = "Update employee profile", nickname = "updateEmployeeProfile", notes = "This api is used to update CbD employee profile")
	// Define information of HTTP response for this API for displaying on Swagger
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal server error occurred"),
			@ApiResponse(code = 503, message = "Service Unavailable")})
	@PutMapping(path = "/edit")
	public @ResponseBody String editProfile(@RequestParam String empID,
											@RequestParam String name,
											@RequestParam String project,
											@RequestParam String hobby){
		employeeRepository.update(hobby,name,project,empID);
		return "Updated";
	}

	@ApiOperation(value = "Get CbD Employee List", nickname = "getAllEmployee", notes = "This API is used to get  all CbD employee list")
	// Define information of HTTP response for this API for displaying on Swagger
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal server error occurred"),
			@ApiResponse(code = 503, message = "Service Unavailable")})
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Employee> getAllUsers() {
		// This returns a JSON or XML with the users
		return employeeRepository.findAll();
	}

	@ResponseBody
	public ResponseEntity sendBadRequest() {
		throw new badRequestException();
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Bad Request")
	public class badRequestException extends RuntimeException {}

}

