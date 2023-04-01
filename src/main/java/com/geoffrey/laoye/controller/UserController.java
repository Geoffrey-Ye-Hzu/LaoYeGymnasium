package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.geoffrey.laoye.common.BaseContext;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.User;
import com.geoffrey.laoye.service.AddressBookService;
import com.geoffrey.laoye.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;



@RestController
@RequestMapping("/user")
@Slf4j
@SuppressWarnings("all")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    //@Autowired
    //private RedisTemplate redisTemplate;


    ///**
    // * 获取验证码
    // *
    // * @param user
    // * @param request
    // * @return
    // */
    //@Cacheable()
    //@PostMapping("/sendMsg")
    //public R<String> sendMsg(@RequestBody User user, HttpServletRequest request) {
    //    //获取手机号
    //    String phone = user.getPhone();
    //
    //    if (StringUtils.isNotEmpty(phone)) {
    //        //生成随机的4位验证码
    //        String code = ValidateCodeUtils.generateValidateCode4String(4);
    //        log.info("code = {}", code);
    //
    //        //调用阿里云提供的短信服务API完成发送短信
    //        //SMSUtils.sendMessage("老叶外卖","",phone,code);
    //
    //        //需要将生成的验证码保存到Session
    //        //request.getSession().setAttribute(phone, code);
    //
    //        //将生成的验证码缓存到Redis中，并且设置有效期为5分钟
    //        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
    //        return R.success("手机验证码发送成功");
    //    }
    //    return R.error("手机短信发送失败");
    //}

    /**
     * 登录操作
     *
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody Map map) {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取密码
        String password = map.get("password").toString();
        //生成的验证码
        //String code = (String) request.getSession().getAttribute(phone);
        //从redis中获取缓存的验证码
        //String code = (String) redisTemplate.opsForValue().get(phone);
        //客户输入的验证码
        //String sendCode = map.get("code").toString();
        ////判断输入的验证码是否对应（页面提交的验证码和Session中保存的验证码进行比对）
        //if (code != null && code.equals(sendCode)) {
        //    //验证码比对一致，则登录成功
        //    //判断当前手机号对应的用户是否是新用户，若是新用户则自动完成注册（存储该用户信息）
        //    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //    queryWrapper.eq(User::getPhone, phone);
        //    User user = userService.getOne(queryWrapper);
        //    if (user == null) {
        //        //新用户，完成信息录入（注册）
        //        user = new User();
        //        user.setPhone(phone);
        //        user.setName("新用户" + phone);
        //        user.setStatus(1);
        //        userService.save(user);
        //    }
        //    log.debug("user.getId()= {}", user.getId());
        //    //登录成功，将用户的id存入Session并返回登录成功的结果
        //    //request.getSession().setAttribute("user", user.getId());
        //    //登录成功，删除redis中缓存的验证码
        //    redisTemplate.delete(phone);
        //    return R.success(user);
        //}

        //验证码比对一致，则登录成功
        //判断当前手机号对应的用户是否是新用户，若是新用户则自动完成注册（存储该用户信息）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        queryWrapper.eq(User::getPassword, password);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return R.error("输入的账号或密码有误");
        }
        log.debug("user.getId()= {}", user.getId());
        //登录成功，将用户的id存入Session并返回登录成功的结果
        request.getSession().setAttribute("user", user.getId());
        //登录成功，删除redis中缓存的验证码
        //redisTemplate.delete(phone);
        return R.success(user);
        //return R.error("登录失败");
    }


    /**
     * 注册账号操作
     *
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/register")
    public R<User> register(HttpServletRequest request, @RequestBody Map map) {
        User user = userService.saveRegister(map);
        if (user==null){
            return R.error("该账号已存在");
        }
        addressBookService.saveNewUserInfo(user);

        return R.success(user);
}




    /**
     * 登出操作
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    private R<String> loginout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");

        return R.success("登出成功");
    }

    /**
     * 获取当前用户id
     */

    @GetMapping
    private R<Long> getUserId(){
        Long currentId = BaseContext.getCurrentId();
        return R.success(currentId);
    }


}
