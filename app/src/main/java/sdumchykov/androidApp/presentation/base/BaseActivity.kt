package sdumchykov.androidApp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VBinding : ViewBinding>(private val inflaterMethod: (LayoutInflater) -> VBinding) :
    AppCompatActivity() {

    private var _binding: VBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflaterMethod.invoke(layoutInflater)
        setContentView(binding.root)

        setListeners()
        setObservers()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    protected open fun setListeners() {}
    protected open fun setObservers() {}
}