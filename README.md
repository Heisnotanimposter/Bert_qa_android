# BERT Question & Answer Android Application

An intelligent Android application that uses BERT (Bidirectional Encoder Representations from Transformers) to answer questions based on context passages. This app demonstrates TensorFlow Lite integration with natural language processing capabilities.

## 🎯 Overview

This Android application allows users to:
- Browse and select from 48 pre-configured passages from the SQuAD dataset
- Ask questions about any passage
- Receive AI-powered answers with automatic highlighting
- Customize inference settings (CPU/GPU/NNAPI, thread count)

## ✨ Key Features

- **Smart Question Answering**: Powered by MobileBERT model
- **Interactive UI**: Material Design with smooth navigation
- **Performance Monitoring**: Real-time inference time display
- **Multiple Delegates**: CPU, GPU, and NNAPI support
- **Answer Highlighting**: Visual feedback for answers
- **Customizable Settings**: Adjust threads and delegates for optimization

## 🚀 Quick Start

### Requirements

- Android Studio Arctic Fox or newer
- Android 6.0 (API Level 23) or higher
- Physical device recommended for best performance

### Build & Run

```bash
# Clone the repository
git clone <repository-url>
cd Bert_qa_android

# Open in Android Studio
# Sync Gradle
# Run on device
```

Models are automatically downloaded during the first build.

## 📱 Usage

1. Launch the app
2. Browse and select a passage
3. Ask a question (or use suggestions)
4. View the highlighted answer
5. Adjust settings as needed

## 📚 Detailed Documentation

See [bert_qa_android/README.md](bert_qa_android/README.md) for comprehensive documentation including:
- Detailed setup instructions
- Architecture overview
- Usage guide
- Development tips
- Troubleshooting

## 🏗️ Project Structure

```
Bert_qa_android/
├── bert_qa_android/          # Main Android app module
│   ├── app/
│   │   ├── src/main/        # Source code
│   │   │   ├── java/        # Kotlin source files
│   │   │   ├── res/         # Resources
│   │   │   └── assets/      # Model files
│   │   └── build.gradle     # App dependencies
│   └── README.md            # Detailed documentation
├── LICENSE                   # MIT License
└── README.md                 # This file
```

## 🧩 Technical Stack

- **Language**: Kotlin
- **Framework**: Android Jetpack (Navigation, Lifecycle, ViewBinding)
- **ML Framework**: TensorFlow Lite
- **UI**: Material Design Components
- **Architecture**: MVVM with fragments

## 📝 License

MIT License - See [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 👤 Author

**SeungwonLee** - 2024

---

**Note**: This application is designed for demonstration and educational purposes. For production use, consider implementing additional features like:
- User history and favorites
- Custom model upload
- Multiple language support
- Export/import datasets
