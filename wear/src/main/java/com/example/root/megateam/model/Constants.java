package com.example.root.megateam.model;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by fra on 14/11/15.
 */
public class Constants {
    public static final String PREF_IS_STARTING_GROUP = "PREF_IS_STARTING_GROUP";
    public static final String PREF_GROUP_NAME = "PREF_GROUP_NAME";
    public static final String PREF_NICKNAME = "PREF_NICKNAME";

    public static final String TAG = "WTT";



    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException,
            ClassNotFoundException {
        byte [] data = Base64.decode(s, 0);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }

}
