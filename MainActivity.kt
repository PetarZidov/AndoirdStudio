package com.example.l03

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import java.io.File

class MainActivity : AppCompatActivity() {
    var fotoImg: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fotoImg = findViewById<ImageView>(R.id.imageView3)
        Log.i("onCr", "Pokrenuto onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mojmenu, menu)
        return true
    }

    fun prikaziToast(v: View) {
        Log.i("Tost ", "Toast pritisnut")
        Toast.makeText(this, "Pritisnut toast gumb", Toast.LENGTH_LONG).show()
    }

    fun prikaziSnackbar(v: View) {
        Log.i("Snack", "Snackbar pritisnut")
        Snackbar.make(v, "Snackbar ", Snackbar.LENGTH_LONG).setAction("Klikni!", null).show();
    }

    fun klikNaUpitnik(v: View) {
        val pom = AlertDialog.Builder(this)
        pom.setIcon(android.R.drawable.ic_dialog_info)
        pom.setTitle("Dijalog")
        pom.setMessage("Kako ste")
        pom.setPositiveButton(
            "Dobro",
            DialogInterface.OnClickListener { dialogInterface, i ->
                Log.i("MOB2021", "Vi ste dobro") })
        pom.setNegativeButton(
            "Lose",
            DialogInterface.OnClickListener { dialogInterface, i ->
                Log.i(
                    "MOB2021",
                    "Vi ste lose"
                )
            })
        pom.setNeutralButton(
            "Super",
            DialogInterface.OnClickListener { dialogInterface, i ->
                Log.i(
                    "MOB2021",
                    "Vi ste super"
                )
            })
        pom.show()
    }

    fun klikNaPovecalo(v: View) {
        var IntPolje = IntArray(250)

        for (i in 0..IntPolje.size) {
            if (i % 3 == 0 && i % 5 == 0) {
                println("$i -- ${IntPolje[i]}")
            }
        }
    }

    fun snimiUnos(v: View) {

        val unos = findViewById<EditText>(R.id.mojUnos)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sp = getPreferences(MODE_PRIVATE)
        val brojac = sp.getInt("Brojac", 0)
        var uneseno = ""
        if (item.itemId == R.id.prikaziUnos)
        {
            var podaci = ArrayList<String>()
            for (i in 1..brojac) {
                uneseno = sp.getString("Unos$i", "").toString()
                podaci.add(uneseno)
                println("$i --> $uneseno")
            }
            val dijalog = AlertDialog.Builder(this)
            dijalog.setTitle("MOB2021-L02-uneseni podaci")
            dijalog.setIcon(android.R.drawable.ic_dialog_info)
            dijalog.setItems(podaci.toTypedArray(),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    println("Odabrano ${podaci[i]}")
                })
            dijalog.setNeutralButton("Otkaži", null)
            dijalog.show()
            Toast.makeText(this, "Ispis svih podataka…", Toast.LENGTH_LONG).show()
            return true
        }else if(item.itemId == R.id.webPregled){
            val webIntent = Intent(this, drugiprozor::class.java)
            startActivity(webIntent)
            return true
        } else if(item.itemId == R.id.uninWeb){
            val webIntent = Intent(this, drugiprozor::class.java)
            webIntent.putExtra("addr", "https://www.unin.hr")
            startActivity(webIntent)
            return true

        } else if (item.itemId == R.id.obrisiUnos) {
            val spe = getPreferences(AppCompatActivity.MODE_PRIVATE).edit()
            spe.putInt("Brojac", 0)
            spe.apply()
            Toast.makeText(this, "Brojač resestiran! Unosi obrisani", Toast.LENGTH_LONG).show()
            return true

        }else if(item.itemId == R.id.listView){
            val listaIntent = Intent(this, treciprozor::class.java)
            startActivity(listaIntent)
            return true
        } else if (item.itemId == R.id.prikaziFotke) {
            Log.i("fotk","Pritisnuto fotke")
            val sp = getPreferences(MODE_PRIVATE)
            val brojac = sp.getInt("FotoBrojac", 0)
            var podatak = ""
            if (brojac == 0){
                Toast.makeText(this, "Nema fotografija!", Toast.LENGTH_LONG).show()
            }   else {
                var podaci = ArrayList<String>()
                for (i in 1..brojac) {
                    podatak = sp.getString("foto$i", "").toString()
                    podaci.add("foto$i.jpg")
                    println("$i --> $podatak")
                }
                val dijalog = AlertDialog.Builder(this)
                dijalog.setTitle("MOB2021-L03-snimljenje fotografije")
                dijalog.setIcon(android.R.drawable.ic_dialog_info)
                dijalog.setItems(
                    podaci.toTypedArray(),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        println("Odabrano ${podaci[i]}")
                        val folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val datoteka = File(folder?.absolutePath + "/" + podaci[i])
                        val fotka = BitmapFactory.decodeFile(datoteka.path)
                        if (fotka != null) {
                            fotoImg?.setImageBitmap(fotka)
                        }
                    })
                dijalog.setNeutralButton("Otkaži", null)
                dijalog.show()
            }
            return true
        } else
            return false
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun pokreniFotku(v: View){

        if(traziOvlasti()){
            val sp = getPreferences(MODE_PRIVATE)
            var brojac = sp.getInt("FotoBrojac", 0)

            val folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val datoteka = File(folder?.absolutePath + "/foto${brojac + 1}.jpg")
            datoteka.createNewFile()
            val s1 = FileProvider.getUriForFile(this, "com.example.l03.fileprovider", datoteka)
            println("Datoteka = " + datoteka)

            val kamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            kamera.putExtra(MediaStore.EXTRA_OUTPUT, s1)
            startActivityForResult(kamera, 12345)
        } else {
            Toast.makeText(this, "Nemate ovlasti!", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun traziOvlasti():Boolean
    {
        if ((checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
            (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED))
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 12345){

            if(resultCode == RESULT_OK){
                val sp = getPreferences(Context.MODE_PRIVATE)
                var brojac = sp.getInt("FotoBrojac", 0)
                val spe = sp.edit()
                spe.putInt("FotoBrojac", brojac + 1)
                spe.apply()

                var imamThumbnail = false
                if (data != null){
                    if (data.hasExtra("data")){
                        fotoImg?.setImageBitmap(data?.extras?.get("data") as Bitmap)
                        imamThumbnail = true
                    }
                }
                if (!imamThumbnail)
                {
                    val folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val datoteka = File(folder?.absolutePath + "/foto${brojac + 1}.jpg")
                    val slika = BitmapFactory.decodeFile(datoteka.path)
                    if(slika!=null){
                        fotoImg?.setImageBitmap(slika)
                    }
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }


}