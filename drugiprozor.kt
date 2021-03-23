package com.example.l03

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class drugiprozor : AppCompatActivity() {
    var fotoImg: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drugiprozor)

        val mojWeb = findViewById<WebView>(R.id.mojWebView)
        mojWeb.webViewClient = WebViewClient()


        val poziv = intent
        val pomAdr = poziv.getStringExtra("addr") ?: ""
        if (pomAdr == "")
        {
            val mojHTML = "<h1 align='center'>MOB2021 - L04</h1>"
            mojWeb.loadData(mojHTML, "text/html", "utf-8")
        } else
        {
            mojWeb.loadUrl(pomAdr)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sp = getPreferences(MODE_PRIVATE)
        val brojac = sp.getInt("Brojac", 0)
        var uneseno = ""
        val mojWeb = findViewById<WebView>(R.id.mojWebView)
        mojWeb.webViewClient = WebViewClient()
        if (item.itemId == R.id.WebAdrese)
        {
            var podaci = ArrayList<String>()
            for (i in 1..brojac) {
                uneseno = sp.getString("Unos$i", "").toString()
                podaci.add(uneseno)
                println("$i --> $uneseno")
            }
            val dijalog = AlertDialog.Builder(this)
            dijalog.setTitle("Unesene adrese:")
            dijalog.setIcon(android.R.drawable.ic_dialog_info)
            dijalog.setItems(podaci.toTypedArray(),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    mojWeb.loadUrl( "${podaci[i]}")

                })
            dijalog.setNeutralButton("Otkaži", null)
            dijalog.setPositiveButton("UNIN", DialogInterface.OnClickListener {dialog: DialogInterface?, which: Int -> mojWeb.loadUrl("https://www.unin.hr") })
            dijalog.show()
            Toast.makeText(this, "Ispis svih podataka…", Toast.LENGTH_LONG).show()
            return true
        } else if(item.itemId == R.id.Webpopis){
                val sp = getPreferences(MODE_PRIVATE)
                val brojac = sp.getInt("Brojac", 0)
                var podaci = ArrayList<String>()
                if (brojac > 0){
                    val mojWeb = findViewById<WebView>(R.id.mojWebView)
                    var mojHTML = ""
                    for (i in 1..brojac){
                        uneseno = sp.getString("Unos$i", "").toString()
                        mojHTML += "<p><a href='$uneseno'>$uneseno</a></p>"
                    }
                    mojWeb.loadData(mojHTML, "text/html", "UTF-8")
                }else
                    Toast.makeText(this,"Nema podataka za prikaz", Toast.LENGTH_LONG).show()

            return true
        }
        else if (item.itemId == R.id.obrisiAdrese) {
            val spe = getPreferences(AppCompatActivity.MODE_PRIVATE).edit()
            spe.putInt("Brojac", 0)
            spe.apply()
            Toast.makeText(this, "Unesene adrese obrisane!", Toast.LENGTH_LONG).show()
            return true

        }  else
    return false
}

    fun ucitajAdr (v: View){
        Log.i("ucAdr", "UcitajAdr pritisnuto")
        val unosAdresa = findViewById<EditText>(R.id.webUnos)
        val sp = getPreferences(MODE_PRIVATE)

        val tmpUnos = unosAdresa.text.toString()
        val mojWeb = findViewById<WebView>(R.id.mojWebView)
        mojWeb.webViewClient = WebViewClient()

        if (tmpUnos.contentEquals("")) {
            Toast.makeText(this, "Ništa nije uneseno!", Toast.LENGTH_LONG).show()
        } else {
            mojWeb.loadUrl(tmpUnos)
        }

    }

    fun spremiAdr (v: View){
        val unos = findViewById<EditText>(R.id.webUnos)
        val uneseno = unos.text.toString().trim()

        if (uneseno.contentEquals("")) {
            Toast.makeText(this, "Ništa nije uneseno!", Toast.LENGTH_LONG).show()
        } else {
            val sp = getPreferences(MODE_PRIVATE)
            val brojac = sp.getInt("Brojac", 0)
            val spe = sp.edit()
            spe.putString("Unos$brojac", uneseno)
            spe.putInt("Brojac", brojac + 1)
            spe.apply()

            Toast.makeText(this, "Pohranjeno! [$uneseno]", Toast.LENGTH_LONG).show()
        }

    }

}