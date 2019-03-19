package Project.Service.ServiceImpl;

import Project.Service.TestService;
import com.Annotation.Service;

@Service
public class TestServiceImpl implements TestService {

	public String Hello(){
		return "hello";
	}
}
