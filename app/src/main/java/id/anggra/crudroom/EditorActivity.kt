package id.anggra.crudroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import id.anggra.crudroom.data.AppDatabase
import id.anggra.crudroom.data.entity.User

class EditorActivity : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var btnSave: Button
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        fullName = findViewById(R.id.full_name)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        btnSave = findViewById(R.id.btn_save)

        database = AppDatabase.getInstance(applicationContext)

        var intent = intent.extras
        if (intent != null) {
            fullName.setText(intent.getString("full_name"))
            email.setText(intent.getString("email"))
            phone.setText(intent.getString("phone"))
        }


        btnSave.setOnClickListener {
            if (fullName.text.toString().isNotEmpty() && email.text.toString()
                    .isNotEmpty() && phone.text.toString().isNotEmpty()
            ) {
                if (intent != null) {
                    // edit data
                    database.userDao().update(User(
                        intent.getInt("id", 0),
                        fullName.text.toString(),
                        email.text.toString(),
                        phone.text.toString()
                    ))

                } else {
                    // tambah data
                    database.userDao().insertAll(
                        User(
                            null,
                            fullName.text.toString(),
                            email.text.toString(),
                            phone.text.toString()
                        )
                    )
                }
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Silahkan isi semua data terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}