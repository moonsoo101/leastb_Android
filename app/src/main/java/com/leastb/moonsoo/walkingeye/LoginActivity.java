package com.leastb.moonsoo.walkingeye;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.leastb.moonsoo.walkingeye.Util.DB;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox mAutoLoginCheckBox;




    /**
     * A login screen that offers login via email/password.
     */
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    EditText id_Edit, pass_Edit;
    Button login_Btn, join_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id_Edit = (EditText) findViewById(R.id.id_Edit);
        pass_Edit = (EditText) findViewById(R.id.pass_Edit);
        login_Btn = (Button) findViewById(R.id.login_Btn);
        login_Btn.setOnClickListener(this);
        join_Btn = (Button) findViewById(R.id.join_Btn);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case    R.id.login_Btn :
            {
                Log.d("result","click");
                searchToDatabase(id_Edit.getText().toString(),pass_Edit.getText().toString());
            }
            break;
        }
    }


//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }
//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private void searchToDatabase(String id, String pass){

        class searchData extends AsyncTask<String, Void, String> {
            String id;
            String pass;
            /*WeakReference<Activity> mActivityReference;
            public searchData(Activity activity){
                this.mActivityReference = new WeakReference<Activity>(activity);
            }*/

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("ok")) {
                    ApplicationClass.ID = id;
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected String doInBackground(String... params) {
                Log.d("result","back");
                id = (String) params[0];
                pass = (String) params[1];
                String[] posts = {id, pass};
                DB db = new DB("login.php");
                String result = db.post(posts);
                Log.d("result",result);
                return result;
            }
        }
        //searchData task = new searchData(Application.this);
        searchData task = new searchData();
        task.execute(id, pass);
    }
}