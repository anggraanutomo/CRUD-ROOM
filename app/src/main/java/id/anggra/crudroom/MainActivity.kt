package id.anggra.crudroom

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.anggra.crudroom.adapter.UserAdapter
import id.anggra.crudroom.data.AppDatabase
import id.anggra.crudroom.data.entity.User

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab : FloatingActionButton
    private var list = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)

        database = AppDatabase.getInstance(applicationContext)
        adapter = UserAdapter(list)
        adapter.setDialog(object : UserAdapter.Dialog {
            override fun onClick(position: Int) {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle(list[position].fullName)
                dialog.setItems(R.array.items_option, DialogInterface.OnClickListener{
                    dialog, which ->
                    when(which){
                        0 -> {
                            val intent = Intent(this@MainActivity, EditorActivity::class.java)
                            intent.putExtra("id", list[position].uid)
                            intent.putExtra("full_name", list[position].fullName)
                            intent.putExtra("email", list[position].email)
                            intent.putExtra("phone", list[position].phone)
                            startActivity(intent)
                        }
                        1 -> {
                            val dialog = AlertDialog.Builder(this@MainActivity)
                            dialog.setTitle("Hapus Data")
                            dialog.setMessage("Apakah anda yakin ingin menghapus data ini?")
                            dialog.setPositiveButton("Ya", DialogInterface.OnClickListener{
                                dialog, which ->
                                database.userDao().delete(list[position])
                                onResume()
                            })
                            dialog.setNegativeButton("Tidak", DialogInterface.OnClickListener{
                                dialog, which ->
                                dialog.dismiss()
                            })
                            dialog.show()
                        }
                    }
                })
                val dialogView = dialog.create()
                dialogView.show()
            }

        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))

        fab.setOnClickListener{
            startActivity(Intent(this, EditorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData() {
        list.addAll(database.userDao().getAll())
        adapter.notifyDataSetChanged()
    }
}