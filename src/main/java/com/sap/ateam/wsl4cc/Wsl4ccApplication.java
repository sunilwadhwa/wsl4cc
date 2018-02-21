package com.sap.ateam.wsl4cc;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class Wsl4ccApplication extends Application {

}

/*
 * TODO:
 * 1. (hi) Work on security options - NONE, BASIC, SAML.
 * 2. (hi) Create unique request # and log that for tracking and identification
 * 3. (hi) Test timestamp data type (not just date, but full timestamp)
 * 4. (hi) Basic documentation (blog) to download and install
 * 
 * 11. (med) Convert to MTA, so that you can publish your app to others
 * 12. (med) Parameterize date format and time format
 * 13. (med) Swagger documentation for API and Javadoc for classes
 * 
 * 21. (low) Implement GET endpoint to get list of destinations
 */
