package com.example.SmartShop.util;

import com.example.SmartShop.Entity.User;
import com.example.SmartShop.enums.UserRole;
import com.example.SmartShop.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    private static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    // sauvgarder l'utilisateur dans la session
    public static void setUser(HttpSession session, User user){
        session.setAttribute(USER_SESSION_KEY,user);
    }

    // Récupérer l'utilisateur de la session
    public static User getUser(HttpSession session){
        Object user = session.getAttribute(USER_SESSION_KEY);
        if(user == null){
            throw new UnauthorizedException("No user logged in , please log in first");
        }
        return (User) user;
    }

    // Vérifier si l'utilisateur est connecté
    public static boolean isAuthenficated(HttpSession session){
        return session.getAttribute(USER_SESSION_KEY)!= null;
    }

    //supprimer l'utilisateur de la session
    public static void clearUser(HttpSession session){
        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    // Vérifier si l'utilisateur est ADMIN
    public static boolean isAdmin(HttpSession session){
        User user = getUser(session);
        return user.getRole() == UserRole.ADMIN;
    }

    //
    public static boolean isClient(HttpSession session){
        User user = getUser(session);
        return user.getRole() == UserRole.CLIENT;
    }

}
