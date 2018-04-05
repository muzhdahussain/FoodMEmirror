package app.foodme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * This file sends login/registration information to the php site for verification.
 *
 * Portions of code adapted from the tutorial series found at: https://www.youtube.com/watch?v=HK515-8-Q_w
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        /**
         * Set to http://192.168.1.5:8080 for Kaylee's house
         * Set to http://70.77.241.161:8080 for elsewhere
         */
        String databaseURL = "http://70.77.241.161:8080";
        String login_url = databaseURL + "/cust_login.php";
        String register_url = databaseURL + "/register.php";
        String emp_login_url = databaseURL + "/emp_login.php";

        // Handles customer login requests
        if (type.equals("cust_login")) {
        try {
                // Retrieves phone number entered by the customer
                String phoneNum = params[1];

                // Makes HTTP connection to the php site
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends login information to the php site
                String postData = URLEncoder.encode("phoneNum", "UTF-8") + "=" + URLEncoder.encode(phoneNum, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("register")) {
            try {
                // Retrieves registration variables entered by the user
                String phoneNum = params[1];
                String name = params[2];
                String email = params[3];

                // Makes HTTP connection to the registration php site
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends registration information to the php site
                String postData = URLEncoder.encode("phoneNum", "UTF-8") + "=" + URLEncoder.encode(phoneNum, "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("emp_login")) {

            try {
                // Retrieves registration variables entered by the user
                String empSIN = params[1];


                // Makes HTTP connection to the registration php site
                URL url = new URL(emp_login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String postData = URLEncoder.encode("empSIN", "UTF-8") + "=" + URLEncoder.encode(empSIN, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        // Creates dialog window for the login response to the user
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Login successful!!")) {
            Intent i = new Intent(context, CustomerMenu.class);
            context.startActivity(i);

        }
        else if(result.equals("Employee login successful!!")){
            Intent i = new Intent(context, EmpMenu.class);
            context.startActivity(i);
        }
        else {
            // Displays login response to the user
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
