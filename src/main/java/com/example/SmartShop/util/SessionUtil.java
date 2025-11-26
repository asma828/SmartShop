package com.example.SmartShop.util;

import com.example.SmartShop.Entity.User;
import com.example.SmartShop.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    // sauvgarder l'utilisateur dans la session
    public static void setUser(HttpSession session, User user){
        session.setAttribute(USER_SESSION_KEY,user);
    }

    public static User getUser(HttpSession session){
        Object user = session.getAttribute(USER_SESSION_KEY);
        if(user == null){
            throw new UnauthorizedException("No user logged in , please log in first");
        }
        return (User) user;
    }


}
