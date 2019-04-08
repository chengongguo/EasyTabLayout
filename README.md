# 一个轻量级的tablayout库

**特色：可以轻松实现云端配置tab功能。**

# 接入指南

1. 在Project的build.gradle中添加maven仓库地址
```groovy
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/chengongguo/maven/" }
    }
}
```

2. 在app模块的build.gradle添加依赖:
```groovy
dependencies {
    implementation 'com.cgg.android:easytablayout:0.0.1'
}
```

3. 在布局中添加EasyTabLayout组件
```xml
 <com.cgg.tablayout.EasyTabLayout
   android:id="@+id/easyTabLayout" 
   android:layout_width="match_parent"
   android:layout_height="wrap_content"/>
```

4. 调用EasyTabLayout的init方法完成tab加载
``` java
public void init(List<Tab> tabList, int selectedId)
```

5. 添加如下监听器实现tab切换的监听
``` java
public void setTabListener(TabListener tabListener)
```

6. 添加如下监听器实现加载指定url的tab图标（可选）
``` java
public void setImageListener(ImageListener imageListener)
```

7. 更多配置请参考demo