package com.lab2.oscar.ejb;

import com.lab2.oscar.model.ScreenwritersResponse;
import com.lab2.oscar.model.OscarUpdateResponse;

import javax.ejb.Remote;

@Remote
public interface OscarServiceRemote {
    ScreenwritersResponse getScreenwritersWithNoOscarWins();
    OscarUpdateResponse addOscarsToMoviesByLength(int minLength, int oscarsToAdd);
}