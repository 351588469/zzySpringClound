package netty.sequence;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by zhaozhengyang on 2017/6/5.
 */
public class SeqExtend {
    /**
     */
    public void initChannel(SocketChannel ch)throws  Exception{
        /**
         * 通过LengthFieldPrepender可以将发送消息的长度写入到ByteBuf的前2个字节，编码后的消息组成"长度字段+原消息"的格式。
         */
        ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(
                65535,0,2,0,2));
        ch.pipeline().addLast("msgpack decoder",new MsgpackDecoder());

        ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
        ch.pipeline().addLast("msgpack encoder",new MsgpackEncoder());

        //ch.pipeline().addLast(new EchoClientHandler(sendNumber));

    }

    /**
     * 首先从数据包arg1中获取需要解码的byte数组，然后调用MessagePack的read方法将其反序列化为Object对象，
     * 将解码后的对象加入到解码列表arg2中，这样就完成了MessagePack的解码操作。
     *
     * 如果不处理半包，netty调用decode方法传递的ByteBuf对象可能就是个半包，反序列化会失败。
     */
    class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{
        @Override
        protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
            final byte[]array;
            final int length=arg1.readableBytes();
            array=new byte[length];
            arg1.getBytes(arg1.readerIndex(),array,0,length);
            MessagePack msgpack=new MessagePack();
            arg2.add(msgpack.read(array));
        }
    }

    /**
     * MsgpackEncoder负责将Objefct类型的POJO对象编码为byte数组，然后写入到ByteBuf中。
     */
    class MsgpackEncoder extends MessageToByteEncoder<Object>{
        @Override
        protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2) throws Exception {
            MessagePack msgpack=new MessagePack();
            byte[]raw=msgpack.write(arg1);
            arg2.writeBytes(raw);
        }
    }

}
