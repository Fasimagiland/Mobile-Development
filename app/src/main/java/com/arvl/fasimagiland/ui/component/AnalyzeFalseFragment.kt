package com.arvl.fasimagiland.ui.component

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.arvl.fasimagiland.R

interface AnalyzeFalseFragmentListener {
    fun onAnalyzeFalseFragmentDismiss()
}
class AnalyzeFalseFragment : DialogFragment() {

    private var listenerFalse: AnalyzeFalseFragmentListener? = null

    companion object {
        private const val ARG_IMAGE_BITMAP = "argImageBitmap"

        fun newInstance(imageBitmap: Bitmap): AnalyzeFalseFragment {
            val args = Bundle().apply {
                putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
            }
            val fragment = AnalyzeFalseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun setFalseListener(listenerFalse: AnalyzeFalseFragmentListener) {
        this.listenerFalse = listenerFalse
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analyze_false, container, false)

        val imageView = view.findViewById<ImageView>(R.id.iv_analyze)
        val bitmap = arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP)
        imageView.setImageBitmap(bitmap)

        view.findViewById<Button>(R.id.btn_get_started).setOnClickListener {
            listenerFalse?.onAnalyzeFalseFragmentDismiss()
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

}
