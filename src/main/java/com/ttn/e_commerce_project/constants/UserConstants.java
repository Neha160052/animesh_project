package com.ttn.e_commerce_project.constants;


import java.util.Set;

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
    public static final String CUSTOMER_ALREADY_ACTIVE = "customer already active";
    public static final String CUSTOMER_ACTIVATED_SUCCESSFULLY = "customer activated successfully";
    public static final String CUSTOMER_DEACTIVATED_SUCCESSFULLY = "Customer deactivated successfully with ID: ";
    public static final String CUSTOMER_ALREADY_DEACTIVATED = "Customer was already deactivated with ID: ";
    public static final String CUSTOMER_REGISTERED_SUCCESSFULLY = "Customer registered Successfully";
    public static final String CUSTOMER_DOES_NOT_EXIST = "Customer with the given id does not exist: ";
    public static final String CUSTOMER_ACTIVATION_MAIL = "Dear Customer your account has been activated by the admin, now you can login";


    // Seller
    public static final String SELLER_NOT_FOUND = "Seller not found";
    public static final String PROFILE_UPDATED = "Profile updated successfully";
    public static final String PASSWORD_UPDATED = "Password updated successfully";
    public static final String ADDRESS_UPDATED = "Address updated successfully";
    public static final String SELLER_ALREADY_ACTIVE = "seller already active";
    public static final String SELLER_ACTIVATED_SUCCESSFULLY = "seller activated successfully";
    public static final String SELLER_DEACTIVATED_SUCCESSFULLY = "Seller deactivated successfully with ID: ";
    public static final String SELLER_ALREADY_DEACTIVATED = "Seller was already deactivated with ID: ";
    public static final String SELLER_REGISTERED_SUCCESSFULLY = "Seller Registered Successfully";
    public static final String SELLER_DOES_NOT_EXIST = "Seller with the given id does not exist: ";
    public static final String SELLER_ACTIVATION_MAIL = "Dear Seller your account has been activated by the admin, now you can login";

    //Category
    public static final String CATEGORY_NOT_FOUND = "Category not found with id: ";
    public static final String CATEGORY_MUST_BE_LEAF ="Category must be a leaf node";
    public static final String CATEGORY_UPDATE_SUCCESS = "Category updated  successfully";
    public static final String METADATA_VALUES_UPDATE_SUCCESS = "Category updated successfully";
    //Product
    public static final String PRODUCT_ACTIVATION_MAIL="A new Product has been registered by the seller please review and activate the same";
    public static final String PRODUCT_SAVED_SUCCESSFULLY="Product saved successfully with product id %d";
    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists for this seller, brand, and category";
    public static final String PRODUCT_NOT_FOUND = "Product Not found with given id";
    public static final String PRODUCT_ID_NULL = "Product ID cannot be null";
    public static final String PRODUCT_ACTIVATED_SUCCESS = "Product activated successfully";
    public static final String PRODUCT_ALREADY_ACTIVE = "Product was already active";
    public static final String PRODUCT_DEACTIVATED_SUCCESS = "Product deactivated successfully";
    public static final String PRODUCT_ALREADY_INACTIVE = "Product was already inactive";

    // Role Prefix
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_DOES_NOT_EXIST = "Role does not exist";
    public static final String USERNAME_DOES_NOT_EXIST = "User does not exist";

    // Validation messages
    public static final String INVALID_EMAIL = "enter a valid email address";
    public static final String GST_ALREADY_IN_USE = "GST already exists provide unique one";
    public static final String COMPANY_NAME_ALREADY_EXISTS = "Company name already exists provide a unique name.";

    // Account activation
    public static final String ACCOUNT_ACTIVATED = "Account activated Successfully";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token";
    public static final String ACTIVATION_EMAIL_SUBJECT = "Please click on the link below to activate your account";
    public static final String ACTIVATION_EMAIL_RESPONSE = "Activation link has been sent to your mail";

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
    public static final String EMAIL_SUBJECT = "This is a system generated mail do not reply to this mail";
    public static final String EMAIL_BODY_SELLER = "Dear Seller your password has been updated successfully";

    // Token validation
    public static final String TOKEN_INVALID_OR_EXPIRED = "Token is either expired or type mismatch";
    public static final String REFRESH_TOKEN_BLACKLISTED = "Refresh token blacklisted please login again";

    // Password mismatch
    public static final String PASSWORD_MISMATCH = "Password and Confirm password should match";

    // Others
    public static final String LOCK_TIME_NULL = "User lock time is null, cannot unlock";

    // Links
    public static final String ACTIVATION_LINK_BASE = "http://localhost:8080/activate?token=";
    public static final String BASE_PATH = "uploads";
    public static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "bmp");



    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Messages
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String MISSING_OR_INVALID_AUTH_HEADER = "Missing or invalid Authorization header";
    public static final String RESET_PASSWORD_EMAIL_SENT = "reset password email has been sent";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successful";

    public static final String CUSTOMER_USER_TYPE = "customer";
    public static final String SELLER_USER_TYPE = "seller";

    // Error Messages
    public static final String EMAIL_ALREADY_IN_USE = "Email already in use";
    public static final String CURRENT_PASSWORD_INCORRECT = "Current password is incorrect";
    public static final String NEW_PASSWORD_MISMATCH = "New password and Confirm password should match";
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    public static final String ADDRESS_COULD_NOT_BE_UPDATED = "Address could not be updated";
    public static final String ACCESS_DENIED = "You are not allowed to access this resource";
    public static final String FIELD_NAME_ALREADY_EXISTS = "field name already exists";
    public static final String ACCESS_TOKEN_EXPIRED = "access token expired";

    // Success Messages
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Profile updated successfully";
    public static final String PASSWORD_UPDATED_SUCCESSFULLY = "Dear Customer your password has been updated successfully";
    public static final String ADDRESS_SAVED_SUCCESSFULLY = "Address saved successfully for userId: ";
    public static final String ADDRESS_DELETED_SUCCESSFULLY = "Address deleted successfully";
    public static final String ADDRESS_UPDATED_SUCCESSFULLY = "Address updated successfully";
    public static final String METADATA_FIELD_ADDED_SUCCESSFULLY = "Metadata field created successfully with ID: %d";



}