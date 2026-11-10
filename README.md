# LaTeX Board

在 Minecraft 中放置一块可编辑的展示板，实时渲染 LaTeX 数学公式。
A Fabric mod for Minecraft that renders LaTeX formulas on an editable in-game board.

## 功能 / Features

- 新增 **LaTeX Board** 方块：8 块木板 + 1 张纸合成
- 右键方块打开编辑界面，输入 LaTeX 即可实时渲染
- 内容通过服务端同步，多人游戏中所有玩家都能看到同一块板
- 内置 JLaTeXMath 渲染，无需在本机安装 LaTeX 环境

## 环境要求 / Requirements

| 项目 | 版本 |
| --- | --- |
| Minecraft | 1.20.1 |
| Fabric Loader | >= 0.16.10 |
| Fabric API | 任意 1.20.1 版本 |
| Java | 17+ |

## 安装 / Installation

1. 在 Releases 页面下载 `latexboard-0.1.0+1.20.1.jar`
2. 安装对应版本的 Fabric Loader
3. 将 jar 放入 `.minecraft/mods/`
4. 同时安装 Fabric API

## 从源码构建 / Build from source

```bash
./gradlew build
```

构建产物位于 `build/libs/`，其中 `latexboard-<version>+1.20.1.jar` 是可直接安装的模组文件。

## 许可证 / License

MIT
