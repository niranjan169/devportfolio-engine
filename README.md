# 🚀 DevPortfolio Engine

**Transform your professional profile into a stunning, responsive portfolio website in seconds.**

DevPortfolio Engine is a powerful Java-based CLI tool designed for developers who want a sleek, modern portfolio without the hassle of manual HTML/CSS coding. It parses your Markdown or PDF resume and renders a high-performance, SEO-friendly website.

---

## ✨ Key Features

- **📄 Multi-Format Support**: Seamlessly convert both **Markdown (`.md`)** and **PDF (`.pdf`)** resumes into professional HTML portfolios.
- **🎨 Premium Themes**: Comes with a selection of modern, responsive themes. Customize styles with simple CSS injection.
- **⚡ Live Preview**: Built-in **File Watcher** that automatically rebuilds your portfolio whenever you save changes to your source files.
- **🔍 SEO Optimized**: Generates semantic HTML5 with proper heading hierarchy and meta tags for better discoverability.
- **📦 Zero Configuration**: Smart defaults allow you to generate a portfolio with a single command, while providing deep customization options for advanced users.

---

## 🛠️ Tech Stack

- **Java 17**: Core engine logic.
- **Maven**: Dependency management and build automation.
- **Apache PDFBox**: High-fidelity text extraction from PDF resumes.
- **Gson**: Flexible configuration and theme management.
- **Vanilla CSS**: Premium, lightweight styling without heavy frameworks.

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/niranjan169/devportfolio-engine.git
   cd devportfolio-engine
   ```

2. **Build the project**:
   ```bash
   mvn clean package
   ```

### Usage

1. **Place your resume**: Add your `resume.md` or `resume.pdf` to the project root.
2. **Run the engine**:
   ```bash
   java -jar target/devportfolio-engine-1.0-SNAPSHOT.jar
   ```
3. **View output**: Your generated portfolio will be available in the `dist/` directory.

---

## 🎨 Customization

You can easily modify themes or create your own by editing the CSS files in `src/main/resources/themes/`. The engine supports dynamic theme loading via `config.json`.

```json
{
  "theme": "minimal",
  "source": "resume.md",
  "outputDir": "dist"
}
```

---

## 🤝 Contributing

Contributions are welcome! If you have ideas for new themes or features, feel free to open an issue or submit a pull request.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Developed with ❤️ by [Niranjan](https://github.com/niranjan169)
