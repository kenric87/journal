import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.regex.*;

public class MarkdownImageConverter {
    // 設定你的 GitHub 倉庫資訊
    private static final String GITHUB_USERNAME = "kenric87";   // 你的 GitHub 帳號
    private static final String REPO_NAME = "journal";          // 你的 GitHub 儲存庫名稱
    private static final String BRANCH_NAME = "main";           // 你的分支（通常是 main 或 master）
    private static final String SUB_PATH = "latex%E4%BD%BF%E7%94%A8%E4%BB%8B%E7%B4%B9"; // GitHub 子資料夾（若有）

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

            // 1️⃣ 替換 <img src="image/6.png" width="70%"> → ![圖片](GitHub Raw URL)
            Pattern imgTagPattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"(.*?)>");
            Matcher imgTagMatcher = imgTagPattern.matcher(content);
            StringBuffer sb = new StringBuffer();

            while (imgTagMatcher.find()) {
                String imagePath = imgTagMatcher.group(1); // 擷取圖片路徑
                String imageRawUrl = convertToGithubRawUrl(imagePath);
                imgTagMatcher.appendReplacement(sb, "![圖片](" + imageRawUrl + ")");
            }
            imgTagMatcher.appendTail(sb);
            content = sb.toString();

            // 2️⃣ 替換 ![alt text](image/4.png) → ![圖片](GitHub Raw URL)
            Pattern markdownImgPattern = Pattern.compile("!\\[.*?\\]\\((?!http)([^)]+)\\)");
            Matcher markdownImgMatcher = markdownImgPattern.matcher(content);
            sb = new StringBuffer();

            while (markdownImgMatcher.find()) {
                String imagePath = markdownImgMatcher.group(1); // 擷取圖片路徑
                String imageRawUrl = convertToGithubRawUrl(imagePath);
                markdownImgMatcher.appendReplacement(sb, "![圖片](" + imageRawUrl + ")");
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
