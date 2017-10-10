package netty.eg.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloClient {

    public static String host = "127.0.0.1";
    public static int port = 7878;

    /**
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /**
             * 第一步，创建Bootstrap实例，Bootstrap是Socket客户端创建工具类，用户通过Bootstrap可以方便的常见netty的客户端并发起异步TCP连接操作。
             */
            Bootstrap b = new Bootstrap();
            /**
             * 第二步，初始化TCP连接参数，设置编解码Handler和其他业务Handler.
             */
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new HelloClientInitializer());
            /**
             * 第三步，调用Bootstrap的connet方法异步发起连接。
             *
             * connet是异步连接，程序并不会在此等待。底层TCP是否连接成功不得而知。为了让用户后续可以判断连续操作结果，异步连接返回了ChannelFuture对象用于通知连接结果。
             * 连接结果有以下两种判断方法
             *  同步等待连接操作结果：用户线程将在此wait()，直到连接操作完成之后，线程被notify()，用户代码继续执行。；
             *  注册监听器，等待操作完成之后的异步通知：可以新增一个或者多个监听器，用于监听异步连接操作结果，操作完成之后系统会回调监听器的相关方法。
             */
            // 连接服务端
            Channel ch = b.connect(host, port).sync().channel();
            // 控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    continue;
                }
                /*
                 * 向服务端发送在控制台输入的文本 并用"\r\n"结尾
                 * 之所以用\r\n结尾 是因为我们在handler中添加了 DelimiterBasedFrameDecoder 帧解码。
                 * 这个解码器是一个根据\n符号位分隔符的解码器。所以每条消息的最后必须加上\n否则无法识别和解码
                 * */
                ch.writeAndFlush(line + "\r\n");
            }
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}