package com.danielme.tipsandroid.asynctask;

import java.net.URL;

import com.danieme.tipsandroid.asynctask.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	ProgressBar progressBar;
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressBar = (ProgressBar) findViewById(R.id.progresss);
		imageView = (ImageView) findViewById(R.id.imageView1);
		// ejecutamos la tarea asícrona (podemos pasarle un array de cadenas)
		new BackgroundTask()
				.execute("https://danielmedotcom.files.wordpress.com/2013/01/android-tip.png");
	}

	private class BackgroundTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			// mostramos el círculo de progreso
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap bmp = null;
			try {
				// se detiene la ejecución del hilo 3 segundos para que de
				// tiempo a ver el progress
				Thread.sleep(3000);
				URL url = new URL(urls[0]);
				bmp = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
			} catch (Exception e) {
				Log.e(MainActivity.class.toString(), e.getMessage());
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
			progressBar.setVisibility(View.GONE);
			// si tenemos la imagen la mostramos
			if (result != null) {
				MainActivity.this.imageView.setImageBitmap(result);
				MainActivity.this.imageView.setVisibility(View.VISIBLE);
			}
			// si no, informamos del error
			else {
				Builder builder = new Builder(MainActivity.this);
				builder.setTitle(R.string.title);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage(R.string.error);
				builder.setNeutralButton(getString(R.string.ok), null);

				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				alertDialog.setCancelable(false);
			}
		}
	}

}