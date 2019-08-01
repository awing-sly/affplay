# affplay
a simple video player on android platform, which implement based on ffplay of FFMPEG-4.1.3 and SDL2-2.0.9 
affplay是一款简单的android视频播放应用，它基于FFMPEG-4.1.3的ffplay,以及SDL2-2.0.9二次开发而来。
affplay对ffpla.c进行了修改扩展，创建了Ffplayer，它实现了类似于android平台上MediaPlayer功能，建立了native到java层的事件通知机制。
另外，由于ffplay是基于SDL实现的，对SDLActivity.java文件进行修改，创建FfplaySdlActivity扩展继承SDLActivit,来更好的完成自定义功能。
目前affplay实现了基本的播放控制，如暂停切换，停止，滑屏快进/快退，显示播放媒体总时长，获取当前播放位置等功能

联系方式: shily1985@126.com
