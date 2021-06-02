package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.UserTableFilterRequest;
import edu.bistu.rojserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/management/permission")
public class UserPermissionManagementController
{
    @Resource
    private UserService userService;

    @GetMapping
    public String showManagementPage(@RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "role", required = false) String role,
                                     @RequestParam(name = "username", required = false) String username,
                                     Model model)
    {
        if(page == null)
            page = 0;
        Slice<UserEntity> slice = userService.getUserList(page, role, username);
        model.addAttribute("userList", slice.getContent());
        model.addAttribute("roleList", UserEntity.Role.values());
        model.addAttribute("hasPrevious", slice.hasPrevious());
        model.addAttribute("hasNext", slice.hasNext());
        model.addAttribute("page", slice.getNumber());
        model.addAttribute("roleName", role);
        model.addAttribute("username", username);
        return "template_user_management";
    }

    @PostMapping("/change")
    public String changeUserRole(UserTableFilterRequest request)
    {
        log.info(request.getUsername() + " " + request.getRole());
        try
        {
            userService.changeUserRole(request.getUsername(), request.getRole());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/management/permission?username=" + request.getUsername();
    }

    @PostMapping("/filter")
    public String filterByRole(UserTableFilterRequest request)
    {
        return "redirect:/management/permission?role=" + request.getRole();
    }

    @PostMapping("/search")
    public String searchByUsername(UserTableFilterRequest request)
    {
        return "redirect:/management/permission?username=" + request.getUsername();
    }
}
