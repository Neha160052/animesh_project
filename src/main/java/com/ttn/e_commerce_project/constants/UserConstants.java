package com.ttn.e_commerce_project.constants;


public final class UserConstants {
    private UserConstants() {
    }

    // Role
    public static final String ROLE_NOT_FOUND = "No role found for authority : %s";

    // User
    public static final String USER_NOT_FOUND_BY_EMAIL = "No account found with email: ";
    public static final String USER_ALREADY_ACTIVE = "User is already active. Activation link cannot be resent.";
    public static final String USER_NOT_FOUND_BY_ID = "User with user id not found:";
    public static final String IMAGE_UPLOADED = "Image uploaded successfully at ";

    // Customer
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";

    // Seller
    public static final String SELLER_NOT_FOUND = "Seller not found";
    public static final String PROFILE_UPDATED = "Profile updated successfully";
    public static final String PASSWORD_UPDATED = "Password updated successfully";
    public static final String ADDRESS_UPDATED = "Address updated successfully";

    // Role Prefix
    public static final String ROLE_PREFIX = "ROLE_";

    // Account activation
    public static final String ACCOUNT_ACTIVATED = "Account activated Successfully";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token";
    public static final String ACTIVATION_EMAIL_SUBJECT = "Please click on the link below to activate your account";

    public static final String LOGIN_INITIATED = "login initiated";
    public static final String LOGIN_SUCCESSFUL = "login successful";
    public static final String TOKEN_BLACKLISTED = "Token with jti={} refreshJti={} has been blacklisted";

    // Account status messages
    public static final String ACCOUNT_NOT_ACTIVE = "Account not active";
    public static final String ACCOUNT_LOCKED = "Account is locked. Please try again after sometime";
    public static final String ACCOUNT_LOCKED_LIMIT_REACHED = "You have reached the maximum limit of login attempts account locked";
    public static final String INVALID_USERNAME_PASSWORD = "Invalid username or password";

    // Email messages
    public static final String ACCOUNT_LOCKED_MAIL = "Dear user your account has been locked due to many failed login attempts";
    public static final String PASSWORD_RESET_SUBJECT = "Click on the link below to reset you password:";

    // Token validation
    public static final String TOKEN_INVALID_OR_EXPIRED = "Token is either expired or type mismatch";
    public static final String REFRESH_TOKEN_BLACKLISTED = "Refresh token blacklisted please login again";

    // Password mismatch
    public static final String PASSWORD_MISMATCH = "Password and Confirm password should match";

    // Others
    public static final String LOCK_TIME_NULL = "User lock time is null, cannot unlock";

    // Links
    public static final String ACTIVATION_LINK_BASE = "http://localhost:8080/activate?token=";

}