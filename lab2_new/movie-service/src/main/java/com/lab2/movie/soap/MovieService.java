package com.lab2.movie.soap;

import com.lab2.movie.model.MovieListResponse;
import com.lab2.movie.model.MovieRequest;
import com.lab2.movie.model.OscarUpdateResponse;
import com.lab2.movie.model.CountResponse;
import com.lab2.movie.model.AverageResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(name = "MovieService", targetNamespace = "http://soap.movie.lab2.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MovieService {

    @WebMethod(operationName = "getAllMovies")
    @WebResult(name = "movieListResponse")
    MovieListResponse getAllMovies(
            @WebParam(name = "page") int page,
            @WebParam(name = "size") int size,
            @WebParam(name = "sort") List<String> sort,
            @WebParam(name = "id") String id,
            @WebParam(name = "name") String name,
            @WebParam(name = "oscarsCount") String oscarsCount,
            @WebParam(name = "goldenPalmCount") String goldenPalmCount,
            @WebParam(name = "length") String length,
            @WebParam(name = "genre") String genre,
            @WebParam(name = "operatorName") String operatorName,
            @WebParam(name = "x") String x,
            @WebParam(name = "y") String y);

    @WebMethod(operationName = "createMovie")
    @WebResult(name = "movie")
    Object createMovie(@WebParam(name = "movieRequest") MovieRequest movieRequest);

    @WebMethod(operationName = "getMovieById")
    @WebResult(name = "movie")
    Object getMovieById(@WebParam(name = "id") long id);

    @WebMethod(operationName = "updateMovie")
    @WebResult(name = "movie")
    Object updateMovie(@WebParam(name = "id") long id, @WebParam(name = "movieRequest") MovieRequest movieRequest);

    @WebMethod(operationName = "deleteMovie")
    @WebResult(name = "success")
    boolean deleteMovie(@WebParam(name = "id") long id);

    @WebMethod(operationName = "addOscarsToMoviesByLength")
    @WebResult(name = "oscarUpdateResponse")
    OscarUpdateResponse addOscarsToMoviesByLength(
            @WebParam(name = "minLength") int minLength,
            @WebParam(name = "oscarsToAdd") int oscarsToAdd);

    @WebMethod(operationName = "getAverageLength")
    @WebResult(name = "averageResponse")
    AverageResponse getAverageLength();

    @WebMethod(operationName = "countMoviesByOperator")
    @WebResult(name = "countResponse")
    CountResponse countMoviesByOperator(
            @WebParam(name = "operatorName") String operatorName,
            @WebParam(name = "operatorBirthday") String operatorBirthday,
            @WebParam(name = "operatorHeight") Long operatorHeight);

    @WebMethod(operationName = "filterMoviesByOperator")
    @WebResult(name = "movieListResponse")
    MovieListResponse filterMoviesByOperator(
            @WebParam(name = "operatorName") String operatorName,
            @WebParam(name = "operatorBirthday") String operatorBirthday,
            @WebParam(name = "operatorHeight") Long operatorHeight);
}