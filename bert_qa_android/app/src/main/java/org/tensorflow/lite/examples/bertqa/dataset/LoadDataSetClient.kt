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
package org.tensorflow.lite.examples.bertqa.dataset

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream

/**
 * Client for loading the question-answering dataset from JSON file.
 * 
 * This class handles loading the dataset from the assets folder,
 * parsing the JSON content, and converting it into a DataSet object.
 * 
 * @param context Application context for accessing assets
 */
class LoadDataSetClient(private val context: Context) {

    private companion object {
        private const val TAG = "BertAppDemo"
        private const val JSON_DIR = "qa.json"
    }

    /**
     * Loads and parses the JSON dataset file from assets.
     * 
     * @return DataSet object containing titles, contents, and questions,
     *         or null if loading fails
     */
    fun loadJson(): DataSet? {
        var dataSet: DataSet? = null
        try {
            // Open the JSON file from assets
            val inputStream: InputStream = context.assets.open(JSON_DIR)
            val bufferReader = inputStream.bufferedReader()
            val stringJson: String = bufferReader.use { it.readText() }
            
            // Parse JSON using Gson
            val datasetType = object : TypeToken<DataSet>() {}.type
            dataSet = Gson().fromJson(stringJson, datasetType)
            
            Log.d(TAG, "Dataset loaded successfully")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load dataset: ${e.message}")
        }
        return dataSet
    }
}
