# github to HackMD 圖片路徑不相容解決辦法
## 緣起
當我們想要將github上面的.md檔丟到 HackMD的時候，往往會因為圖片路徑不相容而產生一堆錯誤，滿江紅啊。(就算是用"從GitHub拉取"還是無法避免)  


原本長這樣  
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/4.png?raw=true)  


從GitHub貼上之後，文字部分是OK的，但是圖片就會出現錯誤  
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/5.png?raw=true)

好，一看就知道是路徑問題，那該如何解決呢??  
我們要先知道GitHub上的圖片在呼叫時通常是以相對位置來呼叫  
```markdown
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/2.png?raw=true)
```
但是把這東西放到HackMD，他是看不懂的，你需要給他一個絕對位置，例如
```markdown
![alt text](https://github.com/ImagineAILab/ai-by-hand-excel/blob/main/gallery.png?raw=true)
```
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/6.png?raw=true)  

這是一個GitHub Raw URL，這網址怎麼得到的，就是點進去你原本放在GitHub上面的圖片，然後案右鍵，選取"複製影像連結"即可完成  

## 解決問題
知道問題之後，要想辦法解決，我們當然可以一個一個慢慢改，但這樣就太不人性了，會降低寫作意願的，十分危險。  
我就想說，可不可以用程式解決一切，上了ChatGPT問了之後得到的回答是可以，於是就有了這東西

```java
///有???的地方記得自己改
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.regex.*;

public class MarkdownImageConverter {
    // 設定你的 GitHub 倉庫資訊
    private static final String GITHUB_USERNAME = "???";   // 你的 GitHub 帳號
    private static final String REPO_NAME = "????";          // 你的 GitHub 儲存庫名稱
    private static final String BRANCH_NAME = "main";           // 你的分支（通常是 main 或 master）
    private static final String SUB_PATH = "???"; // GitHub 子資料夾（若有）

    public static void main(String[] args) {
        File directory = new File("."); // 當前資料夾
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".md"));

        if (files != null) {
            for (File file : files) {
                processMarkdownFile(file);
            }
        }
    }

    private static void processMarkdownFile(File mdFile) {
        try {
            // 讀取 Markdown 檔案（使用 UTF-8）
            List<String> lines = Files.readAllLines(mdFile.toPath(), StandardCharsets.UTF_8);
            String content = String.join("\n", lines);

            // 1️⃣ 替換 ![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/GitHub Raw URL?raw=true)
            Pattern imgTagPattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"(.*?)>");
            Matcher imgTagMatcher = imgTagPattern.matcher(content);
            StringBuffer sb = new StringBuffer();

            while (imgTagMatcher.find()) {
                String imagePath = imgTagMatcher.group(1); // 擷取圖片路徑
                String imageRawUrl = convertToGithubRawUrl(imagePath);
                imgTagMatcher.appendReplacement(sb, "![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/" + imageRawUrl + "?raw=true)");
            }
            imgTagMatcher.appendTail(sb);
            content = sb.toString();

            // 2️⃣ 替換 ![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/4.png?raw=true) → ![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/GitHub Raw URL?raw=true)
            Pattern markdownImgPattern = Pattern.compile("!\\[.*?\\]\\((?!http)([^)]+)\\)");
            Matcher markdownImgMatcher = markdownImgPattern.matcher(content);
            sb = new StringBuffer();

            while (markdownImgMatcher.find()) {
                String imagePath = markdownImgMatcher.group(1); // 擷取圖片路徑
                String imageRawUrl = convertToGithubRawUrl(imagePath);
                markdownImgMatcher.appendReplacement(sb, "![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/" + imageRawUrl + "?raw=true)");
            }
            markdownImgMatcher.appendTail(sb);
            content = sb.toString();

            // 儲存新的 Markdown 檔案（使用 UTF-8）
            String newFileName = mdFile.getName().replace(".md", "_converted.md");
            Files.write(Paths.get(newFileName), content.getBytes(StandardCharsets.UTF_8));

            System.out.println("✅ 已轉換 " + mdFile.getName() + "，新檔案：" + newFileName);
        } catch (IOException e) {
            System.err.println("❌ 無法處理 " + mdFile.getName());
            e.printStackTrace();
        }
    }

    // 轉換相對路徑為 GitHub Raw URL
    private static String convertToGithubRawUrl(String imagePath) {
        return String.format(
            "https://github.com/%s/%s/blob/%s/%s/%s?raw=true",
            GITHUB_USERNAME, REPO_NAME, BRANCH_NAME, SUB_PATH, imagePath
        );
    }
}
```

這程式說白了就是去偵測整個.md檔中所有```<img>```跟```![]()```這類型的語法，並且將原本HackMD看不懂的相對路徑，轉成看得懂的GitHub Raw URL

## 該如何使用這程式
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/7.png?raw=true)  

```
Root/
├── bin/
├── lib/
├── src/
│   └── MarkdownImageConverter.java
└── README.md
```
其中，```MarkdownImageConverter.java```就是剛剛上面的那個程式，```README.md```就是你想要轉的.md檔  
執行完MarkdownImageConverter.java後會變這樣  
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/8.png?raw=true)  

```
Root/
├── bin/
├── lib/
├── src/
│   └── MarkdownImageConverter.java
└── README.md
└── README_converted.md
```  
多了一個```README_converted.md```

如果東西都有跑出來，那大致上就成功了

##  比較README.md & README_converted.md 在HackMD上的呈現效果
### README.md
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/9.png?raw=true)

非常拉垮，圖片完全無法顯示，先去撞牆  

### README_converted.md
![圖片](https://github.com/kenric87/journal/blob/main/markdown筆記/image/10.png?raw=true) 

非常絲滑，要的圖片都出來了