package com.linecorp.menu.validate

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.linecorp.menu.validate.network.menu.request.MenuApiRequestor
import com.linecorp.menu.validate.network.model.MenuModel
import com.linecorp.menu.validate.network.model.parsing.LVApiResponseModel
import com.linecorp.menu.validate.network.model.parsing.LVApiResponseModelListener
import com.linecorp.menu.validate.network.model.parsing.LVModelResult
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.linecorp.menu.validate.adapter.FragmentAdapter


class MenuTabListActivity :AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var adapter : FragmentAdapter?=null
    var deviceType:String ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        val url = intent.getStringExtra("MENU_URL")
         deviceType = intent.getStringExtra("DeviceType")
        if (TextUtils.isEmpty(url) == false) {
            MenuApiRequestor.INSTANCE.requestMenuInfo(url, apiListener)
        }

        tabLayout = findViewById(R.id.tabLayout) as TabLayout
        viewPager = findViewById(R.id.viewPager) as ViewPager
    }

    fun initUi(){
        tabLayout?.newTab()?.setText("more tab")?.let { tabLayout?.addTab(it) }
        tabLayout?.newTab()?.setText("more sub tab")?.let { tabLayout?.addTab(it) }
        tabLayout?.newTab()?.setText("category list")?.let { tabLayout?.addTab(it) }
        tabLayout?.newTab()?.setText("item area")?.let { tabLayout?.addTab(it) }
        tabLayout?.setTabGravity(TabLayout.GRAVITY_FILL)

    }

    fun setData(menuModel: MenuModel,deviceType:String?){
        adapter = FragmentAdapter(
            this,
            supportFragmentManager,
            tabLayout?.getTabCount() ?: 0
        )
        adapter?.setMenuModel(menuModel,deviceType);
        viewPager?.setAdapter(adapter)

        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
    private val apiListener = object: LVApiResponseModelListener<MenuModel>{
        override fun onLoadModel(result: LVModelResult, model: LVApiResponseModel<MenuModel>?) {
            if(result !=null && result.isSucceeded && model?.body !=null) {
                initUi()
                setData(model?.body,deviceType)
            }
        }

    }
}