package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evalsport.models.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView debugTextView;
    private EditText passwordEditText;
    private EditText usernameEditText;
    Button connectButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.pseudoEditText);
        connectButton = findViewById(R.id.connectButton);
        debugTextView = findViewById(R.id.debugTextView);

        connectButton.setOnClickListener(view -> {
            Log.e(TAG, usernameEditText.getText().toString());
            if (usernameEditText.getText().toString().contains("chrono"))
            {
                Intent chronoActivity = new Intent(this, ChronoActivity.class);
                startActivity(chronoActivity);
                finish();
            }
            else if (usernameEditText.getText().toString().contains("recap")){
                Intent recapActivity = new Intent(this, RecapActivity.class);
                startActivity(recapActivity);
                finish();
            }



            handleConnect(view);

        });

    }

    protected void showList(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    public void handleConnect(View v) {
        debugTextView.setText("Connexion en cours...\n");

        Thread thr = new Thread(new Runnable() {
            public void run() {
                // Serveur
                String host     = "https://la-projets.univ-lemans.fr/~inf2pj01/";
                // Page php retournant du JSON
                String page     = "login.php";

                // Identifiants prof (en dur pour les tests)
                //String login    = "ernet"; // le login provient du champ login de l'interface de connexion
                //String password = "be";    // le login provient du champ mot de passe de l'interface de connexion

                String login    = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                HttpURLConnection urlConnection = null;

                // Résultat de la requête
                String result = null;

                // Contenu affiché dans le TextView reply
                StringBuilder replyContent = new StringBuilder();
                replyContent.append(login + "\n" + password + "\n");

                try {
                    // Définition de l'URL
                    URL url = new URL(host + page);

                    // Obtention d'un HttpURLConnection
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // Définition d'un délai de réponse
                    urlConnection.setConnectTimeout(2000);

                    // Données envoyée par POST
                    //String data = "query=" + query;
                    String data = "login=" + login + "&password=" + password;

                    // Préparation de la requête POST
                    urlConnection.setDoOutput(true);							// nécessaire pour POST
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                    // Obtention d'un flux d'écriture sur l'URL pour envoyer les données
                    OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                    osw.write(data);
                    osw.flush();
                    osw.close();

                    // Envoi de la requête
                    urlConnection.connect();

                    // Obtention d'un flux de lecture pour la réponse
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    StringBuilder sb = new StringBuilder();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));

                    // Lecture de la réponse
                    for (String line = r.readLine(); line != null; line = r.readLine()) {
                        sb.append(line); // si la réponse était multilignes, on ajouterait les lignes (pas le cas ici);
                    }
                    // Fermeture du flux
                    in.close();

                    // Obtention de la chaîne de caractères
                    result = sb.toString();
                }
                catch (IOException e) {
                    // Erreur de connexion (peut se produire et doit être gérée)
                    Log.d(TAG, "PAS DE CONNEXION");
                    replyContent.append("PAS DE CONNEXION\n");
                }
                catch (Exception e) {
                    // Erreur imprévue (peut se produire et doit être gérée)
                    e.printStackTrace();
                    Log.d(TAG, "ERREUR EXCEPTION");
                    replyContent.append("ERREUR EXCEPTION");
                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (result != null) {
                    // Affichage du résultat (pour avoir une trace)
                    Log.d(TAG, result);

                    Log.d(TAG, "Extraction contenu JSON");
                    // Extraction du contenu JSON
                    if (result.compareTo("NOK") == 0) { // l'identification du prof n'est pas correcte
                        System.out.println("Connexion refusée !");
                        replyContent.append("Connexion refusée !\n");
                    } else if (result.contains("LOGIN NOT FOUND")) {
                        System.out.println("Identifiants incorrects !");
                        replyContent.append("Identifiants incorrects !");
                    } else { // l'identification du prof est  correcte
                        // Extraction des données JSON
                        try {
                            JSONObject json = new JSONObject(result);
                            Log.d(TAG,json.getString("nomProfesseur") + " connection acceptée");
                            replyContent.append(json.getString("nomProfesseur") + " connection acceptée\n");

                            // Extraction de la liste des classes
                            JSONArray eleves = new JSONArray();
                            JSONArray classes = json.getJSONArray("classes");
                            for (int i = 0; i < classes.length(); i++) {
                                JSONObject classe = classes.getJSONObject(i);
                                replyContent.append("idClasse = " + classe.getString("idClasse") + " - " + classe.getString("nomClasse") + "\n");

                                Log.d(TAG, "idClasse = " + classe.getString("idClasse") + " - " + classe.getString("nomClasse"));
                            }
                            // Extraction de la liste des sports
                            JSONArray sports = json.getJSONArray("sports");
                            for (int j = 0; j < sports.length(); j++) {
                                JSONObject sport = sports.getJSONObject(j);
                                Log.d(TAG, "idSport = " + sport.getString("idSport") + " - " + sport.getString("nomSport"));
                                //replyContent.append("idSport = " + sport.getString("idSport") + " - " + sport.getString("nomSport") + "\n");
                            }

                            // Start list activity ( classes list)
                            Intent classesActivityIntent = new Intent(v.getContext(), ListActivity.class);
                            System.out.println(classes);
                            classesActivityIntent.putExtra("classes", classes.toString());
                            classesActivityIntent.putExtra("sports", sports.toString());
                            classesActivityIntent.putExtra("json", json.toString());
                            startActivity(classesActivityIntent);
                            finish();

                        }
                        catch (JSONException e) {
                            // Erreur JSON (ne devrait jamais se produire si la page php fait bien son travail)
                            Log.e("RESULT : ", result);
                            e.printStackTrace();
                            Log.d(TAG, "JSON Exception");
                            replyContent.append("Mauvaise réponse du serveur (erreur JSON)\n");
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //debugTextView.setText(debugTextView.getText() + replyContent.toString());
                        //debugTextView.append("Connexion terminée");
                       // System.out.println("OAEDFDJVF");
                    }

/*
                    // Méthode permettant de passer des paramètre à l'object Runnable
                    public Runnable init(les paramètres formels) {
                        this.content = content;
                        return this;
                    }
*/
                });//.init(les paramètres réels));

            }
        });
        // Lancement du thread
        thr.start();
    }



}