package com.linecorp.menu.validate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.linecorp.menu.validate.adapter.DeviceCodeSpinnerAdapter
import com.linecorp.menu.validate.adapter.LanguageCodeSpinnerAdapter
import com.linecorp.menu.validate.adapter.RegionSpinnerAdapter
import com.linecorp.menu.validate.adapter.ServerTypeSpinnerAdapter
import com.linecorp.menu.validate.data.MenuParameterItem
import com.linecorp.menu.validate.data.StaticMenuMeta


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnFocusChangeListener {


    private val TAG ="MainActivity"
    private var btnPlay: Button? = null
    private var serverType: Spinner? = null
    private var languageCode: Spinner? = null
    private var DeviceCode: Spinner? = null
    private var region: Spinner? = null
    private var version: EditText? = null

    private var serverTypeSpinnerAdapter: ServerTypeSpinnerAdapter? = null
    private var languageCodeSpinnerAdapter: LanguageCodeSpinnerAdapter? = null
    private var deviceCodeSpinnerAdapter: DeviceCodeSpinnerAdapter? = null
    private var regionSpinnerAdapter: RegionSpinnerAdapter? = null


    private var menuParameterItem: MenuParameterItem? = MenuParameterItem("http://appresources.beta.line.naver.jp/moretab/list.json?lang=%s&device=%s&region=%s&v=%s","en","iphone","id")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlay = findViewById(R.id.confirm_btn) as Button


        btnPlay?.setTag(menuParameterItem)
        serverType= findViewById(R.id.serverType_spinner) as Spinner
        languageCode = findViewById(R.id.language_spinner) as Spinner
        DeviceCode = findViewById(R.id.device_spinner) as Spinner

        region = findViewById(R.id.region_spinner) as Spinner

        version = findViewById(R.id.version) as EditText


        version!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (version!!.tag != null) {

                    val currentClipCount = version!!.tag as Int

                    if (currentClipCount > 0) {
                        if (s.length > 0) {
                            if (Integer.parseInt(s.toString()) <= currentClipCount) {

                            } else {
//                                Toast.makeText(
//                                    this@MainActivity,
//                                    String.format("최대 트랙 %s 수를 넘었습니다.", currentClipCount),
//                                    Toast.LENGTH_LONG
//                                ).show()
                                version!!.setText("")
                            }

                        }
                    }
                }

            }

            override fun afterTextChanged(s: Editable) {}
        })


        setDataAdapter()

        btnPlay!!.setOnClickListener { view: View ->

            val versionCode = version!!.getText().toString()

            val intent = Intent(this@MainActivity, MenuTabListActivity::class.java)

            if (view.getTag() != null && TextUtils.isEmpty(versionCode)==false ) {
               val memuParameterItem =  view?.getTag() as (MenuParameterItem)

                Log.d(TAG,"memuParameterItem language ${memuParameterItem.languageCode} device ${memuParameterItem.device} region ${memuParameterItem.region} version :  ${versionCode}");
                val requestUrl =String.format(memuParameterItem.serverName,memuParameterItem.languageCode,memuParameterItem.device,memuParameterItem.region,versionCode)
                Log.d(TAG," requestUrl :" +requestUrl)
                intent.putExtra("MENU_URL",requestUrl)
                intent.putExtra("DeviceType",memuParameterItem.device)
                startActivity(intent)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        if (parent.adapter is LanguageCodeSpinnerAdapter) {

            val title = (parent.getItemAtPosition(position) as StaticMenuMeta.LanguageItem).title
            val languageCode = (parent.getItemAtPosition(position) as StaticMenuMeta.LanguageItem).languageCode
                menuParameterItem?.languageCode =languageCode


        } else if (parent.adapter is DeviceCodeSpinnerAdapter) {
            val title = (parent.getItemAtPosition(position) as StaticMenuMeta.DeviceItem).title
            val deviceCode = (parent.getItemAtPosition(position) as StaticMenuMeta.DeviceItem).device
            menuParameterItem?.device =deviceCode

        } else if (parent.adapter is RegionSpinnerAdapter) {
            val title = (parent.getItemAtPosition(position) as StaticMenuMeta.RegionItem).title
            val region = (parent.getItemAtPosition(position) as StaticMenuMeta.RegionItem).region
            menuParameterItem?.region =region
        } else if(parent.adapter is ServerTypeSpinnerAdapter){
            val serverName = (parent.getItemAtPosition(position) as StaticMenuMeta.ServerTypeItem).serverName
            menuParameterItem?.serverName =serverName
        }

        Log.d(TAG,"select memuParameterItem serverType ${menuParameterItem?.serverName} language ${menuParameterItem?.languageCode} device ${menuParameterItem?.device} region ${menuParameterItem?.region} version :  ${version?.text.toString()}");
        btnPlay!!.tag = menuParameterItem

    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun setDataAdapter() {

            val languageList= StaticMenuMeta.LanguageItem.Language_LIST
            val deviceList =StaticMenuMeta.DeviceItem.DEVICE_ITEM_LIST
            val regionList = StaticMenuMeta.RegionItem.Region_LIST
            val serverList = StaticMenuMeta.ServerTypeItem.serverType_LIST



        serverTypeSpinnerAdapter = ServerTypeSpinnerAdapter(this,serverList)
        serverType!!.adapter = serverTypeSpinnerAdapter
        serverType!!.onItemSelectedListener = this

        languageCodeSpinnerAdapter = LanguageCodeSpinnerAdapter(this,languageList)
        languageCode!!.adapter = languageCodeSpinnerAdapter
        languageCode!!.onItemSelectedListener = this

        deviceCodeSpinnerAdapter = DeviceCodeSpinnerAdapter(this,deviceList)
        DeviceCode!!.adapter = deviceCodeSpinnerAdapter
        DeviceCode!!.onItemSelectedListener = this


        regionSpinnerAdapter = RegionSpinnerAdapter(this,regionList)
        region!!.adapter = regionSpinnerAdapter
        region!!.onItemSelectedListener = this

    }

    private fun resetControl() {
        btnPlay!!.tag = null
        languageCode!!.setSelection(0)
        region!!.setSelection(0)
        DeviceCode!!.setSelection(0)
        version!!.setText("")
    }

    companion object {

        private val TAG = "@@MainActivity"


    }


}

