### 注意，已经用uniapp改写前端，原有的网页版暂不支持了

#### 配置
要注意自己配置相应的redis，mysql和邮箱验证功能<br>

#### 内容
1. 背单词软件
2. 单词量大于70w
3. 例句30万（非重复）
4. 162本双语小说
5. 10本单词表（四六级，gre，雅思等等大学以上的单词表）


#### 线上地址:
www.ahuiali.com（暂时无法浏览）

#### 联系
    qq：1170782807
    wechat：a1170782807
    email: 1170782807@qq.com

#### 关于安卓
  由于本项目主要是适配手机端的，所以H5效果会比PC端好。这里有个将网页转为Android的软件：一个木函
  1. 找到里面的网页转应用功能（其它应用栏目）
  2. 应用名称随便填，包名建议填写：com.ahuiali.demo
  3. 链接填写 http://www.ahuiali.com
  4. 去除显示顶栏
  5. 生成应用即可
  
#### 修改（demo版本）

##### 2020/12/12
1. 添加爬取文章功能（后台）
2. 采用MyBatisPlus替代MyBatis

##### 2020/9/26
1. 背词页面添加‘已掌握单词’功能
2. 复习页面添加‘已掌握单词’功能
3. 注册激活邮箱信息里面添加跳转页面功能

##### 2020/9/19 - 2020/9/20
1. 修改同一的返回码
2. 修改阅读页收藏单词的bug
3. 修改登录页的提示的bug
4. 新增邮箱定时提醒复习功能（测试）

##### 2020/9/18
1. 修改NextTimeUtils工具类的方法为static
2. 修改README.MD

##### 2020/6/22
1. 实现了收藏单词的功能
2. 完善了搜索单词的功能
3. 使用了非redis查询单词（原方法仍存在，只是暂时不使用）





    
