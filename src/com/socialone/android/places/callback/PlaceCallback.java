package com.socialone.android.places.callback;


import com.socialone.android.places.Response;

/**
 * @author Axxiss
 */
public abstract class PlaceCallback {

    /**
     * Called when a successful response is received. Successful means that all the parameter were
     * correct and the server sent the response normally.
     */
    public abstract void onSuccess(Response response);

    /**
     * When there is an error on the connection, something went wrong or a malformed request was
     * sent to the server, this method will be called.
     *
     * @param exception the error.
     */
    public abstract void onException(final Exception exception);
}
