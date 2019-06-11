package com.guosh.demo.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.guosh.demo.domain.User;
import com.guosh.demo.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "用户API")
@RestController
@RequestMapping(path = {"/api/user","/user"})
public class UserController {
    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
     private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    //第三方注册或者绑定逻辑
    @RequestMapping(value = "/regist",method = RequestMethod.POST)
    public void regist(String username, String password, HttpServletRequest request) {
        User user= userRepository.findByUsernameOrMobile(username,username);
        if(user==null){
            User addUser =new User();
            addUser.setUsername(username);
            addUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(addUser).getId();
        }
        providerSignInUtils.doPostSignUp(username,new ServletWebRequest(request));
    }


    @RequestMapping(value = "/me",method = RequestMethod.GET)
    public Object getLoginUser() {
        //获取详细登陆信息
        //SecurityContextHolder.getContext().getAuthentication();
        //获取用户的登陆信息
        return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    }

    @RequestMapping(method = RequestMethod.POST)
    @JsonView(User.UserSimpleView.class)
    //@Valid启用校验password不允许为空
    @ApiOperation(value="用户创建服务")
    public User createUser(@Valid @RequestBody User user){
        //int a=0/0;
        //如果校验有错误是true并打印错误信息
//        if(errors.hasErrors()){
//            errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
//        }
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());
        user.setId("1");
        return user;
    }

    @RequestMapping(value = "/{id:\\d+}",method = RequestMethod.PUT)
    @JsonView(User.UserSimpleView.class)
    //@Valid启用校验password不允许为空
    @ApiOperation(value="用户更新服务")
    public User updateUser(@Valid @RequestBody User user, BindingResult errors){
        //如果校验有错误是true并打印错误信息
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
        }
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());
        user.setId("1");
        return user;
    }


    @RequestMapping(value = "/{id:\\d+}",method = RequestMethod.DELETE)
    //@Valid启用校验password不允许为空
    @ApiOperation(value="用户删除服务")
    public void deleteUser(@PathVariable String id){
        System.out.println(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    //只显示用户名
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value="用户列表服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号",paramType = "query")
    })
    public List<User> query(@RequestParam(name = "username",required = false) String username, @ApiIgnore @PageableDefault(page = 1,size = 20,sort = "username",direction = Sort.Direction.DESC) Pageable pageable){
        List<User>users=new ArrayList();
//        users.add(new User("1","aaa","111",null));
//        users.add(new User("2","bbb","222",null));
//        users.add(new User("3","ddd","333",null));
        return users;
    }

    @RequestMapping(value = "/{id:\\d+}",method = RequestMethod.GET)
    // 正则表达式 :\\d+ 表示只能输入数字
    //用户名密码都显示
    @JsonView(User.UserDetailView.class)
    @ApiOperation(value="根据id查询用户服务")
    public User  userInfo(@PathVariable String id){
        User user=new User();
        user.setUsername("tom");
        return user;
    }
}
