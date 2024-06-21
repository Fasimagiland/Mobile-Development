package com.arvl.fasimagiland.ui.screen.canvas

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding
import com.arvl.fasimagiland.helper.ImageClassifierHelper
import com.arvl.fasimagiland.ui.component.AnalyzeFalseFragment
import com.arvl.fasimagiland.ui.component.AnalyzeFalseFragmentListener
import com.arvl.fasimagiland.ui.component.AnalyzeFragment
import com.arvl.fasimagiland.ui.component.AnalyzeFragmentListener
import org.tensorflow.lite.task.vision.classifier.Classifications

class CanvasActivity : AppCompatActivity(), UndoRedoListener, ImageClassifierHelper.ClassifierListener, AnalyzeFragmentListener,
    AnalyzeFalseFragmentListener {

    private val binding: ActivityCanvasBinding by lazy {
        ActivityCanvasBinding.inflate(layoutInflater)
    }

    private val viewModel: CanvasViewModel by viewModels()
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var storyId: String

    companion object {
        var currentBrush = Color.WHITE
        var isEraserActive = false
    }

    private var isPencilIconClicked = false
    private var isEraserIconClicked = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Inisialisasi ImageClassifierHelper
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )

        // Observing ViewModel LiveData
        viewModel.storyText.observe(this, Observer { storyText ->
            binding.lstory.text = Html.fromHtml(storyText ?: "")
            // Log ketika teks diperbarui di UI
            Log.d("CanvasActivity", "UI updated with new story text: $storyText")
        })

        viewModel.classificationResults.observe(this, Observer { results ->
            onResults(results, 0) // Update UI with classification results
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            onError(errorMessage ?: "Unknown error")
            // Tampilkan dialog AnalyzeFragment ketika analisis gagal
            if (errorMessage != null && errorMessage.contains("Silahkan gambar ulang!")) {
                showAnalyzeFalseFragmentWithBitmap(viewModel.lastBitmap.value)
            }
        })

        viewModel.isAnalysisSuccessful.observe(this, Observer { isSuccess ->
            if (isSuccess == true) {
                showAnalyzeFragmentWithBitmap(viewModel.lastBitmap.value)
            }
        })

        storyId = intent.getStringExtra("storyId") ?: ""

        // Get intent extras
        val storyText = intent.getStringExtra("storyText")
        storyText?.let { viewModel.setStoryText(it) }

        binding.ivBack.setOnClickListener{
            onBackPressed()
        }


        binding.apply {
            drawPencil.undoRedoListener = this@CanvasActivity



            btnPencil.setOnClickListener {
                isPencilIconClicked = !isPencilIconClicked
                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    drawPencil.stopErasing()
                    isEraserActive = false
                } else {
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                }
            }

            btnEraser.setOnClickListener {
                isEraserIconClicked = !isEraserIconClicked
                if (isEraserIconClicked) {
                    btnEraser.setImageResource(R.drawable.ic_selected_eraser)
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    drawPencil.startErasing()
                    isEraserActive = true
                } else {
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    drawPencil.stopErasing()
                    isEraserActive = false
                }
            }

            btnRedo.setOnClickListener {
                drawPencil.redo()
                updateUndoRedoIcons()
            }

            btnUndo.setOnClickListener {
                drawPencil.undo()
                updateUndoRedoIcons()
            }

            btnAnalyze.setOnClickListener {
                val bitmap = drawPencil.getBitmapFromCanvas()
                viewModel.classifyCanvas(bitmap, imageClassifierHelper)
            }
        }
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        val resultText = results?.joinToString { classification ->
            classification.categories.joinToString { category ->
                "${category.label}: ${category.score}"
            }
        } ?: "Classification failed"

        //Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()

        if (results != null && results.isNotEmpty()) {
            val labels = results.flatMap { it.categories.map { category -> category.label } }
            if (labels.isNotEmpty()) {
                val joinedLabels = labels.joinToString(separator = "\n")
                Log.d("CanvasActivity", "Sending labels for analysis: $joinedLabels")
                viewModel.analyzeText(storyId, joinedLabels)
            }
        }
    }



    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        // Dapatkan bitmap dari DrawCanvas dan tampilkan AnalyzeFalseFragment
        val bitmap = binding.drawPencil.getBitmapFromCanvas()
        showAnalyzeFalseFragmentWithBitmap(bitmap)
    }

    private fun showAnalyzeFalseFragmentWithBitmap(bitmap: Bitmap?) {
        if (bitmap != null) {
            val fragment = AnalyzeFalseFragment.newInstance(bitmap)
            fragment.setFalseListener(this)
            fragment.show(supportFragmentManager, "analyze_false_fragment")
        } else {
            Log.e("CanvasActivity", "Bitmap is null when showing AnalyzeFalseFragment")
        }
    }

    private fun showAnalyzeFragmentWithBitmap(bitmap: Bitmap?) {
        if (bitmap != null) {
            val fragment = AnalyzeFragment.newInstance(bitmap)
            fragment.setListener(this)
            fragment.show(supportFragmentManager, "analyze_fragment")
        } else {
            Log.e("CanvasActivity", "Bitmap is null when showing AnalyzeFragment")
        }
    }

    override fun onAnalyzeFragmentDismiss() {
        binding.drawPencil.clearCanvas()
    }
    override fun onAnalyzeFalseFragmentDismiss() {
        binding.drawPencil.clearCanvas()
    }

    private fun updateUndoRedoIcons() {
        binding.apply {
            btnUndo.setImageResource(
                if (drawPencil.canUndo()) R.drawable.ic_selected_undo else R.drawable.ic_unselected_undo
            )
            btnRedo.setImageResource(
                if (drawPencil.canRedo()) R.drawable.ic_selected_redo else R.drawable.ic_unselected_redo
            )
        }
    }

    override fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        binding.btnUndo.isEnabled = canUndo
        binding.btnRedo.isEnabled = canRedo
        updateUndoRedoIcons()
    }
}
