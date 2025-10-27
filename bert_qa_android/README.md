# TensorFlow Lite BERT Question & Answer Android Application

![App example UI.](screenshot.png?raw=true "Screenshot QA screen")

## 📚 Overview

This is an end-to-end BERT Question & Answer application built with TensorFlow Lite for Android. The app demonstrates how to use BERT models to answer questions based on context passages. The demo includes 48 passages from the SQuAD (Stanford Question Answering Dataset) for users to explore, with the ability to ask custom questions and get AI-powered answers.

### ✨ Features

- **Question Answering**: Ask questions about any passage and get AI-generated answers
- **Pre-configured Passages**: Browse through 48 curated passages from the SQuAD dataset
- **Custom Questions**: Type your own questions or select from suggested questions
- **Answer Highlighting**: Automatically highlights the answer within the context
- **Performance Options**: Configurable number of threads and delegate options (CPU, GPU, NNAPI)
- **Real-time Inference**: View inference time for performance monitoring
- **Material Design UI**: Modern, clean interface with intuitive navigation

## 🚀 Getting Started

### Prerequisites

- **[Android Studio](https://developer.android.com/studio/index.html)** (Android Studio 2022.1 or newer recommended)
- A physical Android device with:
  - Minimum OS version: **Android 6.0 (API Level 23)**
  - Developer mode enabled
  - USB debugging enabled

> **Note**: While an emulator can run the app, a physical device is recommended for optimal performance.

### Building the Application

1. **Clone or Download** this repository
2. **Open Android Studio** and select "Open an existing Android Studio project"
3. Navigate to and select the `bert_qa_android` directory
4. Wait for Gradle sync to complete (may take a few minutes on first build)
5. Connect your Android device via USB
6. Click the green **Run** button (▶️) or press `Shift+F10`

The app will automatically download the required model files on first build.

### Project Structure

```
bert_qa_android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org/tensorflow/lite/examples/bertqa/
│   │   │   │       ├── MainActivity.kt              # Main activity
│   │   │   │       ├── BertQaHelper.kt              # BERT QA processing logic
│   │   │   │       ├── fragments/                   # UI fragments
│   │   │   │       │   ├── DatasetFragment.kt      # Passage selection
│   │   │   │       │   └── QaFragment.kt           # QA interface
│   │   │   │       └── dataset/                    # Data models
│   │   │   │           ├── DataSet.kt
│   │   │   │           └── LoadDataSetClient.kt
│   │   │   ├── res/                                # Resources
│   │   │   │   ├── layout/                         # XML layouts
│   │   │   │   ├── values/                         # Strings, colors, styles
│   │   │   │   └── drawable/                       # Icons and graphics
│   │   │   └── assets/                             # Model files (auto-downloaded)
│   │   └── androidTest/                            # Test files
│   ├── build.gradle                                # App dependencies
│   └── download_models.gradle                      # Auto-download models
├── build.gradle                                    # Project config
├── gradle.properties                               # Gradle settings
└── settings.gradle                                 # Project settings
```

## 📖 Usage

### How to Use the App

1. **Select a Passage**: When the app launches, you'll see a list of 48 available passages
2. **Choose a Passage**: Tap on any passage title to open it
3. **Read the Context**: The full passage will be displayed at the top
4. **Ask a Question**:
   - Type your question in the text field
   - Or tap on any suggested question below
   - Tap the ask button (✉️) when ready
5. **View the Answer**: The answer will be highlighted in the passage text
6. **Adjust Settings** (optional): Use the bottom sheet to configure:
   - Number of threads (1-4)
   - Delegate type (CPU/GPU/NNAPI)

### Tips for Best Results

- **Ask Clear Questions**: Frame questions that can be answered from the passage
- **Check Inference Time**: Lower times indicate better performance
- **Experiment with Delegates**: Try GPU for faster inference (if supported)
- **Use Suggested Questions**: Pre-configured questions work well with the app

## 🔧 Technical Details

### Model Information

- **Model**: MobileBERT
- **Format**: TensorFlow Lite (.tflite)
- **Framework**: TensorFlow 2.x
- **Task Library**: TensorFlow Lite Task Library for Text
- **Size**: ~24 MB (automatically downloaded)

### Delegates

The app supports three inference delegates:

1. **CPU** (Default): Works on all devices, stable performance
2. **GPU**: Fastest on supported devices (accelerated inference)
3. **NNAPI**: Leverages Android Neural Networks API

### Performance Optimization

- **Threads**: Adjustable from 1-4 threads for parallel inference
- **Auto-download**: Models are automatically downloaded and cached
- **GPU Detection**: Automatically detects GPU support

## 🛠️ Development

### Key Components

- **BertQaHelper.kt**: Core inference logic and model management
- **QaFragment.kt**: Main QA interface with answer highlighting
- **DatasetFragment.kt**: Passage selection and navigation
- **LoadDataSetClient.kt**: JSON dataset loading

### Dependencies

- TensorFlow Lite Task Library: `org.tensorflow:tensorflow-lite-task-text:0.3.0`
- TensorFlow Lite GPU: `org.tensorflow:tensorflow-lite-gpu:2.9.0`
- AndroidX Navigation Components
- Material Components
- CameraX (for future enhancements)

### Customizing the Dataset

To use your own dataset:

1. Replace `qa.json` in the assets folder
2. Follow this JSON structure:
```json
{
  "titles": [["Title 1"], ["Title 2"], ...],
  "contents": [["Content 1"], ["Content 2"], ...],
  "questions": [
    ["Question 1.1", "Question 1.2", ...],
    ["Question 2.1", "Question 2.2", ...],
    ...
  ]
}
```

## 🐛 Troubleshooting

### Common Issues

**App crashes on launch**
- Ensure you have an internet connection for first build (model download)
- Check that your device has at least 100MB free storage

**Answers are not accurate**
- Verify your question can be answered from the passage
- Try simplifying complex questions
- Use suggested questions as reference

**Slow inference**
- Try using GPU delegate (if supported)
- Reduce the number of threads
- Close other apps to free up resources

**Models not downloading**
- Check internet connection
- Manually download from:
  - Model: https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/bert_qa/android/models_tflite_task_library_bert_qa_lite-model_mobilebert_1_metadata_1.tflite
  - Dataset: https://storage.googleapis.com/download.tensorflow.org/models/tflite/bert_qa/contents_from_squad.json
- Place files in `app/src/main/assets/`

## 📝 License

This project is licensed under the Apache License 2.0. See the [LICENSE](../LICENSE) file for details.

## 🙏 Credits

- Original implementation based on TensorFlow Lite examples
- Dataset: Stanford Question Answering Dataset (SQuAD)
- Model: MobileBERT fine-tuned for QA tasks

## 📞 Support

For issues and questions:
- Check the [TensorFlow Lite documentation](https://www.tensorflow.org/lite)
- Report issues on GitHub
- Review the code comments for implementation details

---

**Happy Question Answering! 🎯**
