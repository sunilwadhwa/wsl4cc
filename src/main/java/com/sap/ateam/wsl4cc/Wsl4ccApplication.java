package com.sap.ateam.wsl4cc;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class Wsl4ccApplication extends Application {

}

/*
 * TODO:
 * 1. (med) Swagger documentation for API and Javadoc for classes
 * 2. (hi) Create unique request # and log that for tracking and identification
 * 3. (hi) Test timestamp data type (not just date, but full timestamp)
 * 4. (med) Convert to MTA, so that you can publish your app to others
 * 5. (med) Parameterize date format and time format
 * 6. (low) Implement GET endpoint to get list of destinations
 * 8. (hi) Handle invalid input data type, i.e. what will happen if character is expected, but boolean is given??
 * 9. 
 */
