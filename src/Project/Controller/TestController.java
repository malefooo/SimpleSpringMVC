package Project.Controller;

import Project.Service.ServiceImpl.TestServiceImpl;
import com.Annotation.AutoWrite;
import com.Annotation.Controller;
import com.Annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

	@AutoWrite
	private TestServiceImpl testService;

	@RequestMapping("/hello")
	public String Hello(HttpServletRequest req, HttpServletResponse resp){
		return testService.Hello();
//		return "123";
	}
}
