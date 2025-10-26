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
package org.tensorflow.lite.examples.bertqa

import android.content.Context
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.text.qa.BertQuestionAnswerer
import org.tensorflow.lite.task.text.qa.BertQuestionAnswerer.BertQuestionAnswererOptions
import org.tensorflow.lite.task.text.qa.QaAnswer
import java.lang.IllegalStateException

/**
 * Helper class for BERT Question Answering using TensorFlow Lite.
 * 
 * This class manages the BERT model initialization, configuration, and inference.
 * It supports multiple delegates (CPU, GPU, NNAPI) for different inference options
 * and provides callbacks for results and errors.
 *
 * @param context Application context for loading model assets
 * @param numThreads Number of threads to use for inference (1-4, default: 2)
 * @param currentDelegate Delegate type to use (0=CPU, 1=GPU, 2=NNAPI, default: 0)
 * @param answererListener Callback interface for receiving results and errors
 */
class BertQaHelper(
    val context: Context,
    var numThreads: Int = 2,
    var currentDelegate: Int = 0,
    val answererListener: AnswererListener?
) {

    // The BERT question answerer instance
    private var bertQuestionAnswerer: BertQuestionAnswerer? = null

    init {
        setupBertQuestionAnswerer()
    }

    /**
     * Clears the current question answerer instance.
     * Call this when changing delegates or to free up resources.
     */
    fun clearBertQuestionAnswerer() {
        bertQuestionAnswerer = null
    }

    /**
     * Initializes the BERT question answerer with the specified configuration.
     * 
     * This method sets up the model with the configured delegate (CPU/GPU/NNAPI)
     * and number of threads. If GPU delegate is requested but not supported,
     * it falls back gracefully and notifies the listener.
     */
    private fun setupBertQuestionAnswerer() {
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default CPU delegate - no additional configuration needed
            }
            DELEGATE_GPU -> {
                // GPU delegate provides faster inference on supported devices
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    answererListener?.onError("GPU is not supported on this device")
                    Log.w(TAG, "GPU delegate requested but not supported on this device")
                }
            }
            DELEGATE_NNAPI -> {
                // NNAPI leverages Android Neural Networks API for acceleration
                baseOptionsBuilder.useNnapi()
            }
            else -> {
                Log.w(TAG, "Unknown delegate type: $currentDelegate, using CPU")
            }
        }

        val options = BertQuestionAnswererOptions.builder()
            .setBaseOptions(baseOptionsBuilder.build())
            .build()

        try {
            bertQuestionAnswerer =
                BertQuestionAnswerer.createFromFileAndOptions(context, BERT_QA_MODEL, options)
            Log.d(TAG, "BERT Question Answerer initialized successfully")
        } catch (e: IllegalStateException) {
            answererListener
                ?.onError("Bert Question Answerer failed to initialize. See error logs for details")
            Log.e(TAG, "TFLite failed to load model with error: " + e.message)
        }
    }

    /**
     * Answers a question based on the provided context.
     *
     * @param contextOfQuestion The passage/context from which to extract the answer
     * @param question The question to be answered
     * 
     * The method measures inference time and returns the best answer along with
     * the inference time through the listener callback.
     */
    fun answer(contextOfQuestion: String, question: String) {
        if (bertQuestionAnswerer == null) {
            Log.w(TAG, "Answerer was null, reinitializing...")
            setupBertQuestionAnswerer()
        }

        // Measure inference time for performance monitoring
        val startTime = SystemClock.uptimeMillis()

        val answers = bertQuestionAnswerer?.answer(contextOfQuestion, question)
        
        val inferenceTime = SystemClock.uptimeMillis() - startTime
        
        // Return results through the listener
        answererListener?.onResults(answers, inferenceTime)
    }

    /**
     * Listener interface for receiving question answering results and errors.
     */
    interface AnswererListener {
        /**
         * Called when an error occurs during model initialization or inference.
         * 
         * @param error Error message describing what went wrong
         */
        fun onError(error: String)

        /**
         * Called when inference completes successfully.
         * 
         * @param results List of possible answers, ordered by confidence
         * @param inferenceTime Time taken for inference in milliseconds
         */
        fun onResults(
            results: List<QaAnswer>?,
            inferenceTime: Long
        )
    }

    companion object {
        // Model filename in assets folder
        private const val BERT_QA_MODEL = "mobilebert.tflite"
        
        // Log tag for debugging
        private const val TAG = "BertQaHelper"
        
        // Delegate type constants
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
    }
}
