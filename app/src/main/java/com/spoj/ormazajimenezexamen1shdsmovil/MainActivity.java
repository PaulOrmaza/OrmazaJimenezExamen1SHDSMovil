package com.spoj.ormazajimenezexamen1shdsmovil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView txt_respuesta; // Muestra la respuesta del servidor
    private Button btn_enviar_solicitud; // Botón para enviar la solicitud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar las variables con los elementos del diseño
        txt_respuesta = findViewById(R.id.txt_respuesta);
        btn_enviar_solicitud = findViewById(R.id.btn_enviar_solicitud);

        // Configurar el evento para el botón
        btn_enviar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud(); // Llama al método para realizar la solicitud HTTP
            }
        });
    }

    /**
     * Método para realizar una solicitud HTTP al servidor.
     * Se ejecuta en un hilo secundario para evitar bloquear la interfaz de usuario.
     */
    private void enviarSolicitud() {
        // Crear un hilo para realizar la solicitud en segundo plano
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor
                    URL url = new URL("http://192.168.137.79:3003/ormaza");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 2. Leer la respuesta del servidor
                    // BufferedReader para leer la respuesta línea por línea
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder(); // Almacena toda la respuesta del servidor
                    String linea;

                    // Mientras haya contenido, añadirlo al StringBuilder
                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }
                    reader.close(); // Cerramos el lector después de leer

                    // 3. Mostrar la respuesta en el TextView (en el hilo principal)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_respuesta.setText(respuesta.toString()); // Muestra la respuesta en la interfaz
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir el error en consola para depuración

                    // Mostrar un mensaje de error en la interfaz (en el hilo principal)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Inicia el hilo para realizar la solicitud
    }
}
