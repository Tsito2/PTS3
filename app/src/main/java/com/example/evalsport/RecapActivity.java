package com.example.evalsport;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecapActivity extends AppCompatActivity{

    Button buttonAnnuler;
    Button buttonValider;
    TextView listeRecap;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        buttonAnnuler = findViewById(R.id.buttonCancel);
        buttonValider = findViewById(R.id.validationButton);
        listeRecap = findViewById(R.id.listeModifs);

        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
            setTitle(getIntent().getExtras().getString("etape") + "/Fin de la séance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        buttonValider.setOnClickListener(view -> {
            handleConnect(view);
        });

        buttonAnnuler.setOnClickListener(view -> {
            finish();
        });
    }

    public void handleConnect(View v) {
        //debugTextView.setText("Connexion en cours...\n");

        Thread thr = new Thread(new Runnable() {
            public void run() {
                // Serveur
                String host     = "https://la-projets.univ-lemans.fr/~inf2pj01/";
                // Page php retournant du JSON
                String page     = "update.php";

                HttpURLConnection urlConnection = null;

                // Résultat de la requête
                String result = null;

                // Contenu affiché dans le TextView reply
                StringBuilder replyContent = new StringBuilder();
                //replyContent.append(login + "\n" + password + "\n");

                try {
                    // Définition de l'URL
                    URL url = new URL(host + page);

                    // Obtention d'un HttpURLConnection
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // Définition d'un délai de réponse
                    urlConnection.setConnectTimeout(2000);

                    // Données envoyée par POST
                    //String data = "query=" + query;
                    String data = "json="+json.toString();

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
                    replyContent.append("PAS DE CONNEXION\n");
                }
                catch (Exception e) {
                    // Erreur imprévue (peut se produire et doit être gérée)
                    e.printStackTrace();
                    replyContent.append("ERREUR EXCEPTION");
                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (result != null) {
                    // Affichage du résultat (pour avoir une trace)

                    // Extraction du contenu JSON
                    if (result.compareTo("NOK") == 0) { // l'identification du prof n'est pas correcte
                        System.out.println("Connexion refusée !");
                        replyContent.append("Connexion refusée !\n");
                    } else if (result.contains("LOGIN NOT FOUND")) {
                        System.out.println("Identifiants incorrects !");
                        replyContent.append("Identifiants incorrects !");
                    } else { // l'identification du prof est  correcte
                        // Extraction des données JSON
                        listeRecap.setText("Les données ont été envoyées");
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }

                });//.init(les paramètres réels));

            }
        });
        // Lancement du thread
        thr.start();
    }


}