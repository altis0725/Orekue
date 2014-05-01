package jp.magusa.orekue.android.model;

import android.os.AsyncTask;

public abstract class OTask<Params, Progress, Result> extends AsyncTask<Params, Progress, OResponse<Result>>{

    @Override
    protected OResponse<Result> doInBackground(Params... params) {
        //TODO auto renew session
        return do_in_background(params);
    }

    protected abstract OResponse<Result> do_in_background(Params... params);
    
    @Override
    protected void onPostExecute(OResponse<Result> result) {
        if (result == null) onFinish(1, "Unknown Error", null);
        else {
            onFinish(result.errorCode, result.errorMessage, result.data);
        }
    }
    
    protected abstract void onFinish(int errorCode, String errorMessage, Result result);
}
