package com.esimtek.gemalto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.esimtek.gemalto.adapter.UserListAdapter
import com.esimtek.gemalto.model.ResultBean
import com.esimtek.gemalto.model.UserBean
import com.esimtek.gemalto.model.UserManagerBean
import com.esimtek.gemalto.network.Api
import com.esimtek.gemalto.network.HttpClient
import com.esimtek.gemalto.util.EncryptionUtil
import kotlinx.android.synthetic.main.activity_user_manage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManageActivity : BaseActivity(), UserAddFragment.Listener, UserModifyFragment.Listener {

    override fun onAddClicked(userName: String, password: String) {
        val unEncryptBuffer = StringBuffer()
        unEncryptBuffer.append(EncryptionUtil.SECRET_KEY).append(HttpClient.staffId).append(userName).append(password)
        val passwordBuffer = StringBuffer()
        unEncryptBuffer.toList().sorted().forEach { passwordBuffer.append(it) }
        val userManagerBean = UserManagerBean(userName, EncryptionUtil.encrypt(passwordBuffer.toString(), "MD5"), 1)
        userManager(userManagerBean)
    }

    override fun onModifyClicked(password: String) {
        val unEncryptBuffer = StringBuffer()
        unEncryptBuffer.append(EncryptionUtil.SECRET_KEY).append(HttpClient.staffId).append(adapter.longClickBean.userName).append(password)
        val passwordBuffer = StringBuffer()
        unEncryptBuffer.toList().sorted().forEach { passwordBuffer.append(it) }
        val userManagerBean = UserManagerBean(adapter.longClickBean.userName, EncryptionUtil.encrypt(passwordBuffer.toString(), "MD5"), 2)
        userManager(userManagerBean)
    }

    private val adapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_manage)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    UserAddFragment().show(supportFragmentManager, "UserAddDialog")
                }
                else -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            return@setOnMenuItemClickListener false
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)
        getUserList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.user_manage_men, menu)
        if ("admin" == adapter.longClickBean.userType)
            menu?.findItem(R.id.action_delete)?.isVisible = false
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_modify -> UserModifyFragment().show(supportFragmentManager, "UserModifyDialog")
            R.id.action_delete ->
                AlertDialog.Builder(this).setTitle(getString(R.string.alert_dialog_title_delete))
                        .setMessage(getString(R.string.are_you_sure_to_delete_user, adapter.longClickBean.userName))
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                        .setPositiveButton(getString(R.string.sure)) { _, _ ->
                            val userManagerBean = UserManagerBean(adapter.longClickBean.userName, "", 3)
                            userManager(userManagerBean)
                        }.show()
        }
        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun getUserList() {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).userList().enqueue(object : Callback<UserBean> {
            override fun onResponse(call: Call<UserBean>, response: Response<UserBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    response.body()?.data?.userInfoBeans?.let { adapter.addItems(it) }
                } else {
                    Toast.makeText(this@UserManageActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@UserManageActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun userManager(bean: UserManagerBean) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).userManager(bean).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@UserManageActivity, getString(when (bean.requestType) {
                        1 -> R.string.add_user_success
                        2 -> R.string.modify_user_success
                        3 -> R.string.delete_user_success
                        else -> R.string.network_success
                    }), Toast.LENGTH_SHORT).show()
                    getUserList()
                } else {
                    Toast.makeText(this@UserManageActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@UserManageActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
