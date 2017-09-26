package com.example.hp.image;

/**
 * Created by HP on 11/9/2017.
 */
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by User on 24/6/2017.
 */

public class stackTrace {
    public static String trace(Exception ex) {
        StringWriter outStream = new StringWriter();
        ex.printStackTrace(new PrintWriter(outStream));
        return outStream.toString();
    }
}

