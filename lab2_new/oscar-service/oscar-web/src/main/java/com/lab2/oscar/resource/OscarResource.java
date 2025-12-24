package com.lab2.oscar.resource;

import com.lab2.oscar.ejb.OscarServiceRemote;
import com.lab2.oscar.model.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.ejb.Stateless;

@Path("/oscar")
@Stateless
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class OscarResource {
    private static final Logger logger = Logger.getLogger(OscarResource.class.getName());
    
    @EJB
    private OscarServiceRemote oscarService;

    @GET
    @Path("/screenwriters/get-loosers")
    public Response getScreenwritersWithNoOscarWins() {
        logger.info("Received request for screenwriters with no Oscar wins");
        try {
            ScreenwritersResponse response = oscarService.getScreenwritersWithNoOscarWins();
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request for screenwriters with no Oscar wins: " + e.getMessage(), e);
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @PATCH
    @Path("/movies/honor-by-length/{min-length}/oscars-to-add")
    public Response addOscarsToMoviesByLength(
            @PathParam("min-length") int minLength,
            @QueryParam("oscars-to-add") int oscarsToAdd) {
        try {
            if (minLength < 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Minimum length must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
            if (oscarsToAdd < 0) {
                logger.warning("Invalid oscarsToAdd parameter: " + oscarsToAdd);
                ErrorResponse error = new ErrorResponse("Bad Request", "Oscars to add must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            OscarUpdateResponse response = oscarService.addOscarsToMoviesByLength(minLength, oscarsToAdd);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid parameter: " + e.getMessage());
            ErrorResponse error = new ErrorResponse("Bad Request", e.getMessage(), java.time.ZonedDateTime.now(), 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request to add Oscars: " + e.getMessage(), e);
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}