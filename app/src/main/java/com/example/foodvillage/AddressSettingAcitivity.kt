package com.example.foodvillage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodvillage.databinding.ActivityAddressSettingBinding

class AddressSettingAcitivity : AppCompatActivity(){

    private var mBinding: ActivityAddressSettingBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_setting)

        // 바인딩
        mBinding = ActivityAddressSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnActivityAddressSettingCancel.setOnClickListener{
            this.finish()
        }

        // 현재 위치 설정 액티비티로 전환
        binding.clyActivityAddressSettingCurrentpos.setOnClickListener{
            val intent= Intent(this, CurrentAddreessActivity::class.java)
            startActivity(intent)
        }


    }
}