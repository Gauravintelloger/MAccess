package technology.dubaileading.maccessuser.ui.dialog

import android.app.Dialog
import android.content.Context

import android.view.Window

import technology.dubaileading.maccessuser.databinding.DialogComingSoonBinding

class ComingSoonDialog(context : Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        var binding = DialogComingSoonBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setCancelable(false)

        binding.textOk.setOnClickListener{
            dismiss()
        }
    }

}