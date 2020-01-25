package com.miguelmartin.consumii.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.miguelmartin.consumii.Common.*
import com.miguelmartin.consumii.Common.ApplicationLanguageHelper.Companion.IDIOMA
import com.miguelmartin.consumii.Entities.*
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    lateinit var presenter:MainPresenter
    val viaje = Viaje()
    val viajeAyudas = Viaje()
    private var arrCombustibles:Array<Combustible>? = null
    var datosUsuario:DatosUsuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        val userExiste = presenter.userExiste()
        if(presenter.bienvenidaSiNuevoUsuario(userExiste)) return

        datosUsuario = presenter.getDatosUsuario()
        presenter.cargarPrecioCombustible(datosUsuario)
        presenter.cargarConsumoCoche(datosUsuario?.coche!!)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbCustomTrayectos -> etCustomTrayectos.visibility = View.VISIBLE
                else -> etCustomTrayectos.visibility = View.GONE
            }
        }

        btnAyudaCombustible.setOnClickListener { presenter.mostrarDialogCombustibles(arrCombustibles, datosUsuario) }
        btnAyudaTrayecto.setOnClickListener { presenter.getDistancia() }
        btnAyudaConsumo.setOnClickListener { presenter.getCoches() }

        btnCalcular.setOnClickListener { presenter.calcularResultados() }
        ivSettings.setOnClickListener { irSettings() }
    }

    fun irResultadoActivity(viaje: Viaje){
        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("viaje", viaje)
        }
        startActivity(intent)
    }

    fun irSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun cargarListaCombustibles(arrCombustibles: Array<Combustible>){
        this.arrCombustibles = arrCombustibles
    }

    fun rellenarPrecioCombustible(combustible: Combustible){
        etPrecioFuel.setText("")
        etPrecioFuel.hint = "${combustible.precio}${getString(R.string.m_moneda)} (${combustible.tipo!!.nombre})"
        viajeAyudas.coche.combustible = combustible
    }

    fun rellenarConsumoCoche(coche: Coche){
        etConsumo.setText("")
        var nombre = ""
        if(!coche.nombre.isNullOrEmpty())  nombre = "(${coche.nombre})"
        etConsumo.hint = "${coche.consumo} ${getString(R.string.m_consumo)} $nombre"
        viajeAyudas.coche = coche
    }

    fun rellenarDistancia(distancia:Double, inicio:String, destino:String){
//        var inicio = acortar(inicio, 9)
//        var destino = acortar(destino, 9)
        etDistancia.setText("")
        etDistancia.hint = "${distancia}${getString(R.string.m_distancia)} ($inicio - $destino)"
        viajeAyudas.distanciaTrayecto = distancia
    }

    fun irBienvenida(){
        val intent = Intent(this, BienvenidaActivity::class.java)
        startActivity(intent)
    }

    fun irMapa(){
        val intent = Intent(this, MapActivity::class.java)
        startActivityForResult(intent, RC_GET_DISTANCIA)
    }

    fun getInfoViaje():Viaje{
        if(etDistancia.text.toString().isNotEmpty())
            viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()
        else
            viaje.distanciaTrayecto = viajeAyudas.distanciaTrayecto

        if(etConsumo.text.toString().isNotEmpty()){
            viaje.coche.consumo = etConsumo.text.toString().toFloat()
            viaje.coche.id = 0
            viaje.coche.nombre=""
        } else{
            viaje.coche = viajeAyudas.coche
        }

        if(etPrecioFuel.text.toString().isNotEmpty())
            viaje.coche.combustible = Combustible(precio = etPrecioFuel.text.toString().toFloat())
        else
            viaje.coche.combustible = viajeAyudas.coche.combustible


        when {
            rbUnTrayecto.isChecked -> viaje.numeroTrayectos = 1
            rbDosTrayectos.isChecked -> viaje.numeroTrayectos = 2
            rbCustomTrayectos.isChecked -> {
                if(etCustomTrayectos.text.toString().isNotEmpty())
                    viaje.numeroTrayectos = etCustomTrayectos.text.toString().toInt()
            }
        }
        return viaje
    }

    fun crearDialogCombustibles(arrNombres:Array<String>, arrValores: Array<Combustible>, comunidad: String){
        AlertDialog.Builder(this).apply {
            setTitle("${getString(R.string.seleccione_combustible)} - $comunidad")
            setSingleChoiceItems(arrNombres, -1) { dialogInterface, i ->
                presenter.cargarPrecioCombustible(arrValores[i])
                dialogInterface.dismiss()
            }
            setNeutralButton(getString(R.string.cancelar)) { dialog, _ -> dialog.cancel() }
        }.create().show()
    }

    fun crearDialogCoches(arrNombresCoches: Array<String>, arrValores: Array<Coche>) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_administracion_coches))
            setSingleChoiceItems(arrNombresCoches, -1) { dialogInterface, i ->
                viaje.coche = arrValores[i]
                presenter.rellenarDatosByCoche(arrValores[i], arrCombustibles)
                dialogInterface.dismiss()
            }
            setNeutralButton(getString(R.string.administrar)){_, _ ->presenter.administrarCoches()}
            setNegativeButton(getString(R.string.cancelar)){_, _ ->}
        }.create().show()
    }

    fun datosRellenos(viaje: Viaje):Boolean {
        var ok = true

        if(viaje.numeroTrayectos == 0){
            etCustomTrayectos.error = getString(R.string.err_num_trayectos)
            etCustomTrayectos.requestFocus()
            ok =  false
        } else etCustomTrayectos.error = null

        if(viaje.coche.combustible.precio == 0F){
            etPrecioFuel.error = getString(R.string.err_combustible)
            etPrecioFuel.requestFocus()
            ok =  false
        } else etPrecioFuel.error = null

        if(viaje.coche.consumo == 0F){
            etConsumo.error = getString(R.string.err_consumo)
            etConsumo.requestFocus()
            ok =  false
        } else etConsumo.error = null

        if(viaje.distanciaTrayecto == 0.0){
            etDistancia.error = getString(R.string.err_distancia)
            etDistancia.requestFocus()
            ok =  false
        } else etDistancia.error = null

        return ok
    }

    fun irAdministradorCoches() {
        val intent = Intent(this, AdministradorCochesActivity::class.java)
        startActivityForResult(intent, RC_ADMIN_COCHES)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GET_DISTANCIA) {
            val distancia = data?.getStringExtra(DISTANCIA)
            if(distancia.isNullOrEmpty())return
            val inicio = data.getStringExtra(INICIO)
            val destino = data.getStringExtra(DESTINO)
            rellenarDistancia(distancia.toDouble(), inicio!!, destino!!)
        } else if(requestCode == RC_ADMIN_COCHES){
            datosUsuario = presenter.getDatosUsuario()
            presenter.rellenarDatosByCoche(datosUsuario!!.coche, arrCombustibles)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
