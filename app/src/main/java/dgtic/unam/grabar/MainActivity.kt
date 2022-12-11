package dgtic.unam.grabar

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dgtic.unam.grabar.R
import dgtic.unam.grabar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var video   : VideoView
    private lateinit var button  : ImageButton
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main )

        supportActionBar?.setDisplayHomeAsUpEnabled( true )
        supportActionBar?.setHomeButtonEnabled( true )

        binding= ActivityMainBinding.inflate( layoutInflater )
        setContentView( binding.root )

        if( ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.CAMERA
            )  != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf( android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA ),
                1000
            )
        }

        binding.btnGrabar.setOnClickListener( View.OnClickListener {
            var intent = Intent( MediaStore.ACTION_VIDEO_CAPTURE )
            activityResultLauncher.launch( intent )
        } )

        activityResultLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) { result : ActivityResult? ->
            if( result!!.resultCode == Activity.RESULT_OK )
            {
                val videoUri = result!!.data!!.data
                video.setVideoURI( videoUri )
                video.setMediaController( MediaController( this ) )
                video.requestFocus()
                video.start()
            }
            else
            {
                Toast.makeText( this, "Error al capturar video", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean
    {
        onBackPressed()
        return true
    }
}