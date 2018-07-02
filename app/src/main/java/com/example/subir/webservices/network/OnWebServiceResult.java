package com.example.subir.webservices.network;

import com.example.subir.webservices.utils.CommonUtilities;

/**
 * Created by AdityaDua on 12/07/17.
 */

public interface OnWebServiceResult {

    void getWebResponse(String result, CommonUtilities.SERVICE_TYPE type);
}
