package com.example.demo.Controller;

import com.example.demo.Service.*;
import com.example.demo.Service.Firebase.FirebaseService;
import com.example.demo.Service.AdminManagement;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.ifcMergeService;
import com.example.demo.Service.ifcPostService;
import com.example.demo.Service.ifcGetService;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class IfcController {

    private final ifcPostService HelloService;
    private final ifcMergeService IfcMergeService;

    @Autowired
    public IfcController(ifcPostService IfcGetService, ifcMergeService ifcMergeService) {
        this.HelloService = IfcGetService;
        IfcMergeService = ifcMergeService;
    }

    static public JsonBimServerClientFactory factory;
    static public BimServerClient client;

    {
        try {
            factory = new JsonBimServerClientFactory("http://localhost:8082");
            client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
        } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/postIfcAsSubProject")
    @ResponseBody
    public ResponseEntity<String> postIfc(@RequestParam("file") MultipartFile file, String schema, Long parentPoid){
        return ifcPostService.postIfc(file, schema, parentPoid);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public ResponseEntity<String> getIfc(@RequestParam Long fileName, HttpServletResponse response){
        return ifcGetService.downloadIfc(fileName,response);}

    @GetMapping("/getProjectList")
    @ResponseBody
    public ResponseEntity<String> getProjectList(@RequestParam String token){
        return ifcGetService.authGetAllProjects(token);
    }

    @GetMapping("/deleteProject")
    @ResponseBody
    public ResponseEntity<String> deleteProject(@RequestParam Long oid){
        return ifcPostService.deleteProject(oid);
    }

    @PostMapping("/merge")
    @ResponseBody
    public ResponseEntity<String> merge(@RequestParam("file") MultipartFile file, long mergeFile2) {
        return ifcMergeService.mergeIfc(file, mergeFile2);}

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam String username,String password){
        return AuthenticationService.login(username,password);
    }

    @GetMapping("/AddUserToProject")
    @ResponseBody
    public ResponseEntity<String> AddUserToProject(@RequestParam Long parent0Id,String username, String token){
        return AdminManagement.addUser(parent0Id,username, token);
    }

    @GetMapping("/RemoveUserFromProject")
    @ResponseBody
    public ResponseEntity<String> RemoveUserFromProject(@RequestParam Long parent0Id,String username, String token){
        return AdminManagement.removeUserProject(parent0Id,username, token);
    }

    @GetMapping("/ViewUsers")
    @ResponseBody
    public ResponseEntity<String> ViewUsers(@RequestParam Long parent0Id, String token){
        return AdminManagement.ViewUsers(parent0Id, token);
    }

    @GetMapping("/CreateProject")
    @ResponseBody
    public ResponseEntity<String> createProject(@RequestParam String parent0Id, String schema, String token){
        return AdminManagement.createProject(parent0Id, schema, token);
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register (@RequestParam String emailUsername, String password, String name){
        return AuthenticationService.register(emailUsername, password,name);
    }

    @GetMapping("/getAllNotification")
    public ResponseEntity<String> getAllNotification(@RequestHeader Long id){
        return FirebaseService.getAllNotification(id);
    }

}

