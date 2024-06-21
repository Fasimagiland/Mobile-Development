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

interface AnalyzeFragmentListener {
    fun onAnalyzeFragmentDismiss()
}

class AnalyzeFragment : DialogFragment() {

    private var listener: AnalyzeFragmentListener? = null

    companion object {
        private const val ARG_IMAGE_BITMAP = "argImageBitmap"

        fun newInstance(imageBitmap: Bitmap): AnalyzeFragment {
            val args = Bundle().apply {
                putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
            }
            return AnalyzeFragment().apply {
                arguments = args
            }
        }
    }

    fun setListener(listener: AnalyzeFragmentListener) {
        this.listener = listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analyze, container, false)

        val imageView = view.findViewById<ImageView>(R.id.iv_analyze)
        val bitmap = arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP)
        imageView.setImageBitmap(bitmap)

        view.findViewById<Button>(R.id.btn_get_started).setOnClickListener {
            listener?.onAnalyzeFragmentDismiss()
            dismiss()
        }

        return view
    }
}
