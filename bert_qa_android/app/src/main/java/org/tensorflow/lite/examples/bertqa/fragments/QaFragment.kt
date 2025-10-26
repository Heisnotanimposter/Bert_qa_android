/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tensorflow.lite.examples.bertqa.fragments

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.tensorflow.lite.examples.bertqa.BertQaHelper
import org.tensorflow.lite.examples.bertqa.R
import org.tensorflow.lite.examples.bertqa.databinding.FragmentQaBinding
import org.tensorflow.lite.examples.bertqa.dataset.LoadDataSetClient
import org.tensorflow.lite.task.text.qa.QaAnswer

/**
 * Fragment for displaying a passage and allowing users to ask questions.
 * 
 * This fragment displays the full passage text, provides a question input field,
 * and shows the AI-generated answer with highlighting. Users can also access
 * suggested questions and adjust inference settings.
 */
class QaFragment : Fragment(), BertQaHelper.AnswererListener {
    private var _fragmentQaBinding: FragmentQaBinding? = null
    private val fragmentQaBinding get() = _fragmentQaBinding!!

    // BERT QA helper for inference
    private lateinit var bertQaHelper: BertQaHelper
    
    // Navigation arguments containing the selected passage index
    private val args: QaFragmentArgs by navArgs()
    
    // The passage content to display
    private var content: String = ""
    
    // List of suggested questions for this passage
    private var questions: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentQaBinding = FragmentQaBinding.inflate(inflater, container, false)
        return fragmentQaBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize BERT QA helper
        bertQaHelper = BertQaHelper(context = requireContext(), answererListener = this)
        
        // Load the selected passage content and questions
        val client = LoadDataSetClient(requireActivity())
        client.loadJson()?.let {
            content = it.getContents()[args.datasetPosition]
            questions = it.questions[args.datasetPosition]
        }

        // Display the passage content
        fragmentQaBinding.tvDatasetContent.text = content
        
        // Setup UI components
        initRecyclerView()
        handleListener()
    }

    /**
     * Initializes the RecyclerView with suggested questions.
     */
    private fun initRecyclerView() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        with(fragmentQaBinding.recyclerView) {
            adapter = QaAdapter(questions) { position ->
                setQuestion(position)
            }
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
    }

    /**
     * Sets up all click listeners and text watchers for the UI.
     */
    private fun handleListener() {
        // Text watcher to enable/disable ask button based on input
        fragmentQaBinding.edtQuestion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Enable/disable ask button based on whether question is not empty
                val shouldAskButtonActive = charSequence.toString().isNotEmpty()
                fragmentQaBinding.imgBtnAsk.isClickable = shouldAskButtonActive
                fragmentQaBinding.imgBtnAsk.setImageResource(
                    if (shouldAskButtonActive) R.drawable.ic_ask_active else R.drawable.ic_ask_inactive
                )
            }
            
            override fun afterTextChanged(editable: Editable?) {}
        })

        // Ask button: submit the question for processing
        fragmentQaBinding.imgBtnAsk.setOnClickListener {
            answerQuestion(fragmentQaBinding.edtQuestion.text.toString())
        }

        // Decrease number of threads for inference (min: 1)
        fragmentQaBinding.bottomSheetLayout.threadsMinus.setOnClickListener {
            if (bertQaHelper.numThreads > 1) {
                bertQaHelper.numThreads--
                updateControlsUi()
            }
        }

        // Increase number of threads for inference (max: 4)
        fragmentQaBinding.bottomSheetLayout.threadsPlus.setOnClickListener {
            if (bertQaHelper.numThreads < 4) {
                bertQaHelper.numThreads++
                updateControlsUi()
            }
        }

        // Delegate selection: CPU, GPU, or NNAPI
        fragmentQaBinding.bottomSheetLayout.spinnerDelegate.setSelection(0, false)
        fragmentQaBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    bertQaHelper.currentDelegate = position
                    updateControlsUi()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    /**
     * Updates the UI controls with current settings and reinitializes the answerer.
     * Called when thread count or delegate is changed.
     */
    private fun updateControlsUi() {
        fragmentQaBinding.bottomSheetLayout.threadsValue.text = bertQaHelper.numThreads.toString()
        // Clear answerer to force reinitialization with new settings
        bertQaHelper.clearBertQuestionAnswerer()
    }

    /**
     * Sets a suggested question in the edit text when user taps it.
     */
    private fun setQuestion(position: Int) {
        fragmentQaBinding.edtQuestion.setText(questions[position])
    }

    /**
     * Processes the question and generates an answer.
     * 
     * @param question The question to be answered
     */
    private fun answerQuestion(question: String) {
        bertQaHelper.answer(content, question)
    }

    /**
     * Highlights the answer text within the passage.
     * 
     * @param answer The answer text to highlight
     */
    private fun highlightAnswer(answer: String) {
        val start = content.indexOf(answer)
        val end = start + answer.length

        val str = SpannableString(content)
        str.setSpan(
            BackgroundColorSpan(requireActivity().getColor(R.color.highlight_background_color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            ForegroundColorSpan(requireActivity().getColor(R.color.highlight_text_color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        fragmentQaBinding.tvDatasetContent.text = str
    }

    override fun onError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: List<QaAnswer>?, inferenceTime: Long) {
        results?.first()?.let {
            highlightAnswer(it.text)
        }

        fragmentQaBinding.tvInferenceTime.text = String.format(
            requireActivity().getString(R.string.bottom_view_inference_time),
            inferenceTime
        )
    }

    override fun onDestroyView() {
        fragmentQaBinding.edtQuestion.addTextChangedListener(null)
        super.onDestroyView()
    }

    override fun onDestroy() {
        bertQaHelper.clearBertQuestionAnswerer()
        super.onDestroy()
    }
}
