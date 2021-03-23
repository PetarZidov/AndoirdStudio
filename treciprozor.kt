package com.example.l03

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class treciprozor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treciprozor)


        val popis = mutableListOf<String>("Prvi")
        popis.add("Drugi")
        popis.add("Treći")
        popis.add("Četvrti")
        popis.add("Stoti")

        val mojaLista = findViewById<ListView>(R.id.lview)
        registerForContextMenu(mojaLista)
        val adapter = object: ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, popis)
        {
            /*
             @RequiresApi(Build.VERSION_CODES.O)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tmp = super.getView(position, convertView, parent)
                var col = Color.parseColor("#ffffff")
                if (position%3==1) col=Color.parseColor("#ffff00")
                else if (position%3==2) col=Color.parseColor("#ff00ff")
                tmp.setBackgroundColor(col)
                return tmp
            }
        }
        */

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var tmp: View? = null
                if (position%2==0)
                    tmp = layoutInflater.inflate(R.layout.mojredak2,null)
                else
                    tmp = layoutInflater.inflate(R.layout.mojredak,null)

                val rBr = tmp.findViewById<TextView>(R.id.redniBroj)
                val txt = tmp.findViewById<TextView>(R.id.podatakTekst)
                rBr.text = (position+1).toString()
                txt.text = popis[position]

                var col  = Color.parseColor("#ffffff")
                if (position%3==0) col=Color.parseColor("#ffff00")
                else if (position%3==1) col=Color.parseColor("#ff00ff")
                val brojke = MutableList(100) { Random.nextInt(1,500) }
                val podaci = MutableList<String> (100, { "" })
                var poms = ""
                for (i in 0..podaci.size-1)
                {
                    poms = ""
                    if (i%5==0) poms = "||#" + Integer.toHexString(Color.rgb(2*i%255, 2*i%255, 2*i%255))
                    podaci[i] = "${i+1}. Podatak = " + brojke[i].toString() + poms

                    println(Integer.toHexString(Color.rgb(2*i%255, 2*i%255, 2*i%255)))

                   /* var color = Integer.toHexString(Color.rgb(2*i%255, 2*i%255, 2*i%255))
                    col = Color.parseColor("#" + color)

                    tmp.setBackgroundColor(col)


                    */
                    /*
                    col = Color.parseColor("#Integer.toHexString(Color.rgb(2*i%255, 2*i%255, 2*i%255))")

                     */
                }


                tmp.setBackgroundColor(col)
                 return tmp
            }

        }



        mojaLista.adapter = adapter
        adapter.notifyDataSetChanged()

        mojaLista.setOnItemClickListener{
            adapterView, view, i, l ->
            // i je pozicija
            Toast.makeText(this, "Klik na "+i.toString()+" -- "+popis[i],Toast.LENGTH_LONG).show()
        }



    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo? )
    {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu!!.setHeaderTitle("Izbornik")
        menu.add("Izbor 1")
        menu.add("Izbor 2")
        menu.add("Izbor 100")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val poz = info.position
        Toast.makeText(this,"Odabrali ste "+item.title+" Lista = "+poz.toString(),Toast.LENGTH_LONG).show()
        return true
    }

}
   