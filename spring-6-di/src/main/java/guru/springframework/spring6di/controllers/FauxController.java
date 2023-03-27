package guru.springframework.spring6di.controllers;


import guru.springframework.spring6di.services.EnvironmentService;
import guru.springframework.spring6di.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class FauxController {
    private final EnvironmentService environmentService;

    public FauxController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public String sayHello(){
        return "You are in " + environmentService.getEnv() + " environment";
    }
}
