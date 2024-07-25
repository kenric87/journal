## 使用的Markdown編輯器
我是用vs code，她其實有很多插件可以安裝，裝完之後，即可順暢使用
<img src="./image/image.png" width="85%">  

### 常用插件介紹
####    [1.Markdown PDF](https://marketplace.visualstudio.com/items?itemName=yzane.markdown-pdf)
<img src="./image/image-1.png" width="85%">


在寫完文章之後，有時需要將其匯出成pdf供他人使用，這時就需要Markdown PDF來幫忙。  
安裝完之後，可在編輯區直接按右鍵，選擇**Markdown PDF: Export(pdf)**  
即可成功匯出。
<img src="./image/image-2.png" width="25%">

## markdown 語法

>   我個人認為，markdown可以不用一下子全部都會，先用再說，然後大概知道有哪些功能即可(阿其實也不用100%知道)  

相信用久了大家都會  
這邊主要是紀錄一下我常用的  
不一定每個人都適合~~~ 
### 語法整理 
放上幾個我覺得還不錯的語法整理:  
[MarkDown語法大全](https://hackmd.io/@eMP9zQQ0Qt6I8Uqp2Vqy6w/SyiOheL5N/%2FBVqowKshRH246Q7UDyodFA?type=book)

>   若還有不會的話自己Google
### 頁內書籤
1.  ctrl+shift+v = 在vs code 中開啟預覽  
2.  設定頁內書籤
標題部分:``` <h2 id="1">緣起</h2>```  
連結部分:```- [緣起](#1) ```
### 頁外頁內書籤
剛剛在網路上找到另一個更好用的方法  
[youtube 短影片網址](https://youtube.com/shorts/G5580-DxQuw?si=BW4Y6ribnmY752qM)  
簡單來說，除了可以在文章內可以跳轉，還可以跳到另外一篇文章的某個標題裡  
具體作法如下:  
```markdown
[顯示的字](##)  通常在這時候就會跳出選單來讓你選想要的標題  
```
重點就是那兩個##

### 調整圖片大小
HackMD 可以指定圖片的大小，其語法如下：
```
![圖片替代文字](<圖片網址> "選擇性的圖片標題" =寬度x高度)
```
在最後加上=寬度x高度)即可，但這不是我們要的，因為我們不是用HackMD  

---
通常是用這個 **HTML img size 圖片大小設置**
```
第一種
<img src="圖片位址" width="50%">
第二種
<img src="圖片位址" width="200" height="200">
```