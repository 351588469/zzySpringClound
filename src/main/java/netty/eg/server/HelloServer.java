package netty.eg.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端最重要的设计原则有三条：
 * 	1）服务端只提供最上层的API,不与任何具体协议绑定。
 * 	2）服务端提供给用户的API要尽量屏蔽底层的通信细节，防止底层的变更引起上层的级联变更。
 * 		例如netty3到netty4,整个包路径都发生了改变，如果在底层屏蔽了netty接口，上层不受影响。
 * 	3）服务端功能不要求全，重点在可扩展性。
 */
public class HelloServer {
    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7878;

    public static void main(String[] args) throws InterruptedException {
        /**
         * 1）用于接受客户端链接的线程池：通常被称为boosGroup,它的构造方法与处理I/O读写的线程池相同（workGroup）,
         * 		都是通过new NioEventLoopGroup创建。bossGroup的线程数建议设置为1，因为它仅负责接受客户端的连接，
         * 		不做复杂的逻辑处理，未了尽可能少的占用资源，它的取值越小越好。
         * 2）TCP参数设置：建议在API层面开放TCP参数设置，为一些特殊的私有协议预留扩展点，对于大多数的开发者，使用默认值即可。
         * 		netty的ChannelOption类提供了对TCP参数的封装，由于是常用工具类，未来发生改变的可能性很小，因此可以直接开放给上层使用。
         * 3）编解码框me架的定制：通信框架封装Netty的MessageToByteEncoder和LengthFieldBasedFrameDecoder,
         * 		提供统一、通用的编解码接口或者抽象类，由用户实现编解码接口，实现编解码的灵活定制。
         * 4）通信层业务逻辑的定制：利用netty的ChannelPipeline,将ChannelHandler的链式编排能力以参数或者配置化的方式开放出来，
         * 		由用户灵活扩展，实现协议层逻辑定制。例如示例代码中的超时检测、握手认证等。需要指出的是，
         * 		如果担心底层通信框架API发生变更直接影响用户的代码，可以在Netty的ChannelHandler上面再抽象包装一层，通过Facade模式屏蔽底层的实现细节。
         */
        //配置服务端的NIO线程组
        /**
         * create on 2017.6.3
         * 如果不指定线程池大小，netty会优先使用-Dio.netty.eventLoopThreads指定的值，
         * 如果用户没有设置，默认采用CPU Core*2(Runtime.getRuntime().availableProcessors()*2).
         * 通信框架的服务端只负责客户端接入，不处理I/O读写操作，负载非常轻，因此只需要单线程即可满足客户端接入。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        /**
         * create on 2017.6.3
         * 对于I/O读写线程池workGroup,线程个数依旧需要根据实际情况进行设计，如果并发接入的客户端不是很多，通信框架的负载不重，建议配置值为：1<=N<=CPU Core。
         * 如果并发接入人数非常多，I/O处理逻辑又比较复杂，则建议根据实际性能测试结果逐步调高线程数。总之，尽量不要使用系统默认值。
         *
         * 为避免I/O线程池使用不当导致通信线程膨胀->
         * 1)根据客户端连接数，评估I/O线程数，创建一个大的线程池NioEventLoopGroup，所有客户端连接公用。
         * 2）创建一个包含NioEventLoopGroup的数组，将客户端连接按照Hash算法分组，将所有连接均匀的打散在NioEventLoopGroup中，该方案的优点是降低锁竞争，提升处理效率。
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * netty为了向使用者屏蔽nio通信的底层细节，在和用户交互的边界做了封装，目的就是未了减少用户开发工作量，降低开发难度。
             *ServerBootstrap是Socket服务端的启动辅助类，用户通过ServerBootstrap可以方便的创建netty服务端。
             */

            ServerBootstrap b = new ServerBootstrap();
            //b.option(ChannelOption.SO_BACKLOG,100);
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HelloServerInitializer());


            // 服务器绑定端口监听
            ChannelFuture f = b.bind(portNumber).sync();
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

            // 可以简写为
			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
