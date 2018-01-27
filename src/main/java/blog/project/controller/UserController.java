package blog.project.controller;

import blog.project.bindingModel.UserBindingModel;
import blog.project.bindingModel.UserEditBindingModel;
import blog.project.entity.Article;
import blog.project.entity.Role;

import blog.project.entity.User;
import blog.project.repository.RoleRepository;
import blog.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class UserController {
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
  private   UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) throws IOException {
        if (!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
            return "redirect:/register";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        String encoded = Base64.getEncoder().encodeToString(userBindingModel.getProfilePicture().getBytes());

        user.setProfilePicture(encoded);

        user.addRole(userRole);
        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                                                                    .getAuthentication()
                                                                    .getPrincipal();
         User user = this.userRepository.findByEmail(principal.getUsername());

         model.addAttribute("user", user);
         model.addAttribute("view", "user/profile");

         return "base-layout";
    }

    @GetMapping("/user/edit")
    public String edit(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("view", "user/edit");

        return "base-layout";
    }

    @PostMapping("/user/edit")
    public String editProcess(UserEditBindingModel userBindingModel) throws IOException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());

        if(!StringUtils.isEmpty(userBindingModel.getPassword())
                && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())){
            if(userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
            user.setFullName(userBindingModel.getFullName());
            user.setEmail(userBindingModel.getEmail());

        }

        user.setFullName(userBindingModel.getFullName());
        String encoded = Base64.getEncoder().encodeToString(userBindingModel.getProfilePicture().getBytes());
        user.setProfilePicture(encoded);



        this.userRepository.saveAndFlush(user);

        return "redirect:/";
    }

}
